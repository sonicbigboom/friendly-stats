/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.api.groups.id.users.MembershipDto;
import com.potrt.stats.entities.Club;
import com.potrt.stats.entities.Membership;
import com.potrt.stats.entities.Membership.MaskedMembership;
import com.potrt.stats.entities.Person;
import com.potrt.stats.entities.desc.PersonClub;
import com.potrt.stats.entities.desc.PersonRole;
import com.potrt.stats.exceptions.PersonAlreadyExistsException;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.repositories.MembershipRepository;
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

  private MembershipRepository membershipRepository;
  private SecurityService securityService;
  private PersonService personService;

  /** Autowires a {@link MembershipService}. */
  @Autowired
  public MembershipService(
      MembershipRepository membershipRepository,
      SecurityService securityService,
      PersonService personService) {
    this.membershipRepository = membershipRepository;
    this.securityService = securityService;
    this.personService = personService;
  }

  /**
   * Determines if a {@link Person} has the given {@link PersonRole} permissions for a club.
   *
   * @param personID The {@link Person} id.
   * @param clubID The {@link Club} id.
   * @param personRole The {@link PersonRole} permission level requested.
   * @return Whether the {@link Person} has at least that {@link PersonRole} for the club.
   */
  public boolean hasRole(Integer personID, Integer clubID, PersonRole personRole)
      throws UnauthenticatedException, UnauthorizedException {
    if (!personID.equals(securityService.getPersonID())
        && !membershipRepository.isMember(securityService.getPersonID(), clubID)) {
      throw new UnauthorizedException();
    }

    String role = membershipRepository.getRole(personID, clubID);
    return personRole.permits(role);
  }

  /**
   * Gets the total combined cash balance of the calling {@link Person}.
   *
   * @return The total cash balance of the {@link Person}.
   * @throws UnauthenticatedException Thrown if the caller is unauthenticated.
   */
  public Integer getCashBalance() throws UnauthenticatedException {
    return membershipRepository.getCashBalance(securityService.getPersonID());
  }

  /**
   * Gets the combined cash balance of the calling {@link Person}, without clubs that have removed
   * this {@link Person}.
   *
   * @return The total member cash balance of the {@link Person}.
   * @throws UnauthenticatedException Thrown if the caller is unauthenticated.
   */
  public Integer getMemberedCashBalance() throws UnauthenticatedException {
    return membershipRepository.getMemberedCashBalance(securityService.getPersonID());
  }

  /**
   * Gets all of the {@link Club} ids that the calling {@link Person} is a member of.
   *
   * @return The collection of {@link Club} ids that the caller is a member of.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   */
  public Iterable<Integer> getClubIDs() throws UnauthenticatedException {
    Integer personID = securityService.getPersonID();
    return membershipRepository.getClubIDs(personID);
  }

  /**
   * Updates the permissions of a {@link Person} in a {@link Club}.
   *
   * @param personID The {@link Person}'s id.
   * @param clubID The {@link Club}'s id.
   * @param personRole The {@link PersonRole} new role.
   * @apiNote This can be used to add or remove a person from a {@link Club}.
   */
  public void updatePermissions(Integer personID, Integer clubID, PersonRole personRole) {
    Optional<Membership> oldMembership =
        membershipRepository.findById(new PersonClub(personID, clubID));
    Membership membership;
    if (oldMembership.isPresent()) {
      membership = oldMembership.get();
      membership.setPersonRole(personRole.getIdentifier());
    } else {
      membership =
          new Membership(personID, clubID, personRole.getIdentifier(), 0, null, null, null);
    }
    membershipRepository.save(membership);
  }

  /**
   * Gets all of the {@link MaskedMembership}s for a {@link Club}.
   *
   * @param clubID The {@link Club} id.
   * @return The list of {@link MaskedMembership}.
   * @throws UnauthenticatedException Thrown if the caller is unauthenticated.
   * @throws UnauthorizedException Thrown if the caller is not a member of the {@link Club}.
   */
  public List<MaskedMembership> getMemberships(Integer clubID)
      throws UnauthenticatedException, UnauthorizedException {
    String role = membershipRepository.getRole(securityService.getPersonID(), clubID);
    if (!PersonRole.PERSON.permits(role)) {
      throw new UnauthorizedException();
    }
    boolean isCashAdmin = PersonRole.CASH_ADMIN.permits(role);

    Iterable<Membership> memberships = membershipRepository.getByClubID(clubID);

    List<MaskedMembership> maskedMemberships = new ArrayList<>();
    for (Membership membership : memberships) {
      if (membership.getPersonRole() == null) {
        continue;
      }

      maskedMemberships.add(new MaskedMembership(membership, isCashAdmin));
    }

    return maskedMemberships;
  }

  /**
   * Determines if a {@link Person} is a member of a {@link Club}.
   *
   * @param personID The {@link Person} id.
   * @param clubID The {@link Club} id.
   * @return Whether the {@link Person} is a member of the {@link Club}.
   * @throws UnauthorizedException Thrown if the user is unauthenticated.
   * @throws UnauthenticatedException Thrown is the user is not a member of the {@link Club}.
   */
  public boolean isMember(Integer personID, Integer clubID)
      throws UnauthenticatedException, UnauthorizedException {
    if (!personID.equals(securityService.getPersonID())
        && !membershipRepository.isMember(securityService.getPersonID(), clubID)) {
      throw new UnauthorizedException();
    }

    return membershipRepository.isMember(personID, clubID);
  }

  /**
   * Creates a {@link Membership} for a {@link Person} and a {@link Club}.
   *
   * @param clubID The {@link Club} id.
   * @param membershipDto The {@link MembershipDto}.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller does not have permission to add a member.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist.
   * @throws PersonAlreadyExistsException Thrown if the {@link Person} is already a member of the
   *     {@link Club}.
   */
  public void addMember(Integer clubID, MembershipDto membershipDto)
      throws UnauthenticatedException,
          UnauthorizedException,
          PersonDoesNotExistException,
          PersonAlreadyExistsException {
    if (!hasRole(securityService.getPersonID(), clubID, PersonRole.GAME_ADMIN)) {
      throw new UnauthorizedException();
    }

    Integer personID;
    if (StringUtils.isNumeric(membershipDto.getIdentifier())) {
      personID = Integer.parseInt(membershipDto.getIdentifier());
    } else {
      personID = personService.getPersonWithoutAuthorization(membershipDto.getIdentifier()).getId();
    }

    if (isMember(personID, clubID)) {
      throw new PersonAlreadyExistsException();
    }

    Optional<Membership> oldMembership =
        membershipRepository.findById(new PersonClub(personID, clubID));
    Membership membership;
    if (oldMembership.isPresent()) {
      membership = oldMembership.get();
      membership.setPersonRole(membershipDto.getPersonRole());
      membership.setFirstName(membershipDto.getFirstName());
      membership.setLastName(membershipDto.getLastName());
      membership.setNickname(membershipDto.getNickname());
    } else {
      membership =
          new Membership(
              personID,
              clubID,
              membershipDto.getPersonRole(),
              0,
              membershipDto.getFirstName(),
              membershipDto.getLastName(),
              membershipDto.getNickname());
    }

    membershipRepository.save(membership);
  }
}
