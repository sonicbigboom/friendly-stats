/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.entities.PersonRole;
import com.potrt.stats.repositories.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {

  private MembershipRepository membershipRepository;

  @Autowired
  public MembershipService(MembershipRepository membershipRepository) {
    this.membershipRepository = membershipRepository;
  }

  public boolean hasRole(Integer personID, Integer clubID, PersonRole personRole) {
    String role = membershipRepository.getRole(personID, clubID);
    return personRole.permits(role);
  }

  public Integer getCashBalance(Integer personID) {
    return membershipRepository.getCashBalance(personID);
  }

  public Integer getMemberedCashBalance(Integer personID) {
    return membershipRepository.getMemberedCashBalance(personID);
  }
}