/* Copywrite (c) 2024 */
package com.potrt.stats.api.balance;

import com.potrt.stats.entities.Balance;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.security.SecurityService;
import com.potrt.stats.services.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BalanceApi {

  private SecurityService securityService;
  private MembershipService membershipService;

  @Autowired
  public BalanceApi(SecurityService securityService, MembershipService membershipService) {
    this.securityService = securityService;
    this.membershipService = membershipService;
  }

  @GetMapping("/balance")
  public ResponseEntity<Balance> getBalance() {
    try {
      Integer personID = securityService.getPersonID();

      Integer total = membershipService.getCashBalance(personID);
      Integer membered = membershipService.getMemberedCashBalance(personID);

      return new ResponseEntity<>(new Balance(total, membered), HttpStatus.OK);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
