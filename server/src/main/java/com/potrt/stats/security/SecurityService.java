/* Copyright (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.club.ClubRepository;
import com.potrt.stats.data.membership.MembershipRepository;
import com.potrt.stats.data.membership.PersonRole;
import com.potrt.stats.data.person.Person;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/** The {@link SecurityService} provides information on the authenticated {@link Person}. */
@Service
public class SecurityService {

  private MembershipRepository membershipRepository;
  private ClubRepository clubRepository;

  /**
   * Autowires the {@link SecurityService}.
   *
   * @implNote The {@link SecurityService} breaks the rule of only the appropriate service can
   *     access the repository. This is to enable the quick checking of permissions.
   */
  @Autowired
  public SecurityService(MembershipRepository membershipRepository, ClubRepository clubRepository) {
    this.membershipRepository = membershipRepository;
    this.clubRepository = clubRepository;
  }

  /**
   * Gets the authenticated {@link Person}.
   *
   * @return The authenticated {@link Person}.
   * @throws UnauthenticatedException Thrown when no {@link Person} is authenticated.
   */
  public Person getPerson() throws UnauthenticatedException {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Person.class.isAssignableFrom(principal.getClass())) {
      return (Person) principal;
    } else if (PersonPrincipal.class.isAssignableFrom(principal.getClass())) {
      PersonPrincipal personPrincipal = (PersonPrincipal) principal;
      return personPrincipal.getPerson();
    } else {
      throw new UnauthenticatedException("The caller is not authenticated.");
    }
  }

  /**
   * Gets the authenticated {@link Person}'s id.
   *
   * @return The authenticated {@link Person}'s id.
   * @throws UnauthenticatedException Thrown when no {@link Person} is authenticated.
   */
  public Integer getPersonId() throws UnauthenticatedException {
    return getPerson().getId();
  }

  /**
   * Throws an exception if the authenticated {@link Person} does not have the required role for the
   * given club, or the club does not exists.
   *
   * @param clubId The club id.
   * @param role The {@link PersonRole role} that the {@link Person} must have.
   * @throws UnauthenticatedException Thrown when no {@link Person} is authenticated.
   * @throws UnauthorizedException Thrown when the no {@link Person} is authenticated.
   * @throws NoResourceException Thrown if the clube does not exist.
   */
  public void assertHasPermission(Integer clubId, PersonRole role)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    Integer personId = getPerson().getId();
    String r = membershipRepository.getRole(personId, clubId);
    if (!role.permits(r)) {
      throw new UnauthorizedException(
          "The caller does not have the \""
              + role.getIdentifier()
              + "\" role required for the service.");
    }

    Optional<Club> club = clubRepository.findById(clubId);
    if (club.isEmpty() || club.get().isDeleted()) {
      throw new NoResourceException("This club does not exist.");
    }
  }
}
