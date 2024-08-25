/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.entities.PersonRole;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.repositories.MembershipRepository;
import com.potrt.stats.security.SecurityService;
import jakarta.transaction.Transactional;
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

  /** Autowires a {@link MembershipService}. */
  @Autowired
  public MembershipService(
      MembershipRepository membershipRepository, SecurityService securityService) {
    this.membershipRepository = membershipRepository;
    this.securityService = securityService;
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
}
