/* Copyright (c) 2024 */
package com.potrt.stats.data.membership;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.person.Person;
import com.potrt.stats.data.person.PersonService;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonAlreadyExistsException;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.security.SecurityService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The {@link MembershipService} is a service that gets, creates, and removes {@link Membership}s.
 */
@Service
@Transactional
public class MembershipService {

  private SecurityService securityService;
  private MembershipRepository membershipRepository;
  private PersonService personService;

  /** Autowires a {@link MembershipService}. */
  @Autowired
  public MembershipService(
      SecurityService securityService,
      MembershipRepository membershipRepository,
      PersonService personService) {
    this.securityService = securityService;
    this.membershipRepository = membershipRepository;
    this.personService = personService;
  }

  /**
   * Determines if a {@link Person} has the given {@link PersonRole} permissions for a club.
   *
   * @param personId The {@link Person} id.
   * @param clubId The {@link Club} id.
   * @param personRole The {@link PersonRole} permission level requested.
   * @return Whether the {@link Person} has at least that {@link PersonRole} for the club.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Person} of the {@link Club}.
   * @throws NoResourceException Thrown if there is no {@link Club} with this id.
   */
  public boolean hasPermission(Integer personId, Integer clubId, PersonRole personRole)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    securityService.assertHasPermission(clubId, PersonRole.PERSON);

    String role = membershipRepository.getRole(personId, clubId);
    return personRole.permits(role);
  }

  /**
   * Gets the total combined cash balance of the calling {@link Person}.
   *
   * @return The total cash balance of the {@link Person}.
   * @throws UnauthenticatedException Thrown if the caller is unauthenticated.
   */
  public Integer getCashBalance() throws UnauthenticatedException {
    return membershipRepository.getCashBalance(securityService.getPersonId());
  }

  /**
   * Gets the combined cash balance of the calling {@link Person}, without clubs that have removed
   * this {@link Person}.
   *
   * @return The total member cash balance of the {@link Person}.
   * @throws UnauthenticatedException Thrown if the caller is unauthenticated.
   */
  public Integer getMemberedCashBalance() throws UnauthenticatedException {
    return membershipRepository.getMemberedCashBalance(securityService.getPersonId());
  }

  /**
   * Gets all of the {@link Club} ids that the calling {@link Person} is a member of.
   *
   * @return The collection of {@link Club} ids that the caller is a member of.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   */
  public Iterable<Integer> getClubIds() throws UnauthenticatedException {
    Integer personId = securityService.getPersonId();
    return membershipRepository.getClubIds(personId);
  }

  /**
   * Updates the permissions of a {@link Person} in a {@link Club}.
   *
   * @param personId The {@link Person}'s id.
   * @param clubId The {@link Club}'s id.
   * @param personRole The {@link PersonRole} new role.
   * @apiNote This can be used to add or remove a person from a {@link Club}.
   */
  public void updatePermissions(Integer personId, Integer clubId, PersonRole personRole) {
    Optional<Membership> oldMembership =
        membershipRepository.findById(new PersonClub(personId, clubId));
    Membership membership;
    if (oldMembership.isPresent()) {
      membership = oldMembership.get();
      membership.setPersonRole(personRole.getIdentifier());
    } else {
      membership =
          new Membership(personId, clubId, personRole.getIdentifier(), 0, null, null, null);
    }
    membershipRepository.save(membership);
  }

  /**
   * Gets all of the {@link MaskedMembership}s for a {@link Club}.
   *
   * @param clubId The {@link Club} id.
   * @return The list of {@link MaskedMembership}.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Person} of the {@link Club}.
   * @throws NoResourceException Thrown if there is no {@link Club} with this id.
   */
  public List<Membership> getMemberships(Integer clubId)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    securityService.assertHasPermission(clubId, PersonRole.PERSON);

    String role = membershipRepository.getRole(securityService.getPersonId(), clubId);
    boolean isCashAdmin = PersonRole.CASH_ADMIN.permits(role);

    List<Membership> out = new ArrayList<>();
    for (Membership membership : membershipRepository.getByClubId(clubId)) {
      if (membership.getPersonRole() == null) {
        continue;
      }

      if (!isCashAdmin) {
        membership = membership.mask();
      }

      out.add(membership);
    }

    return out;
  }

  /**
   * Determines if a {@link Person} is a member of a {@link Club}.
   *
   * @param personId The {@link Person} id.
   * @param clubId The {@link Club} id.
   * @return Whether the {@link Person} is a member of the {@link Club}.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Person} of the {@link Club}.
   * @throws NoResourceException Thrown if there is no {@link Club} with this id.
   */
  public boolean isMember(Integer personId, Integer clubId)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    securityService.assertHasPermission(clubId, PersonRole.PERSON);

    return membershipRepository.isMember(personId, clubId);
  }

  /**
   * Creates a {@link Membership} for a {@link Person} and a {@link Club}.
   *
   * @param clubId The {@link Club} id.
   * @param identifier A {@link identifier} can be either a {@link Person} id or email.
   * @param personRole The {@link PersonRole} for the person.
   * @param firstName The person's first name.
   * @param lastName The person's last name.
   * @param nickname The person's nickname.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Game Admin} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if there is no {@link Club} with this id.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist.
   * @throws PersonAlreadyExistsException Thrown if the {@link Person} is already a member of the
   *     {@link Club}.
   */
  public void addMember(
      Integer clubId,
      String identifier,
      String personRole,
      String firstName,
      String lastName,
      String nickname)
      throws UnauthenticatedException,
          UnauthorizedException,
          NoResourceException,
          PersonAlreadyExistsException {
    securityService.assertHasPermission(clubId, PersonRole.GAME_ADMIN);

    Integer personId;
    if (StringUtils.isNumeric(identifier)) {
      personId = Integer.parseInt(identifier);
    } else {
      String email = identifier;
      try {
        personId = personService.getPersonWithoutAuthCheck(email).getId();
      } catch (PersonDoesNotExistException e) {
        personId = personService.createPersonWithoutAuthCheck(email).getId();
      }
    }

    if (isMember(personId, clubId)) {
      throw new PersonAlreadyExistsException();
    }

    Optional<Membership> oldMembership =
        membershipRepository.findById(new PersonClub(personId, clubId));
    Membership membership;
    if (oldMembership.isPresent()) {
      membership = oldMembership.get();
      membership.setPersonRole(personRole);
      membership.setFirstName(firstName);
      membership.setLastName(lastName);
      membership.setNickname(nickname);
    } else {
      membership = new Membership(personId, clubId, personRole, 0, firstName, lastName, nickname);
    }

    membershipRepository.save(membership);
  }
}
