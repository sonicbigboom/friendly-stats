/* Copyright (c) 2024 */
package com.potrt.stats.api.balance;

import com.potrt.stats.data.membership.MembershipService;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Creates an endpoint to get information on a user's total and membered cash balance. */
@Controller
public class BalanceApi {

  private MembershipService membershipService;

  /** Autowires the {@link BalanceApi} */
  @Autowired
  public BalanceApi(SecurityService securityService, MembershipService membershipService) {
    this.membershipService = membershipService;
  }

  /** The {@code /balance} {@code GET} endpoint return the balance of the caller. */
  @GetMapping("/balance")
  public ResponseEntity<BalanceResponse> getBalance() {
    try {
      Integer total = membershipService.getCashBalance();
      Integer membered = membershipService.getMemberedCashBalance();

      return new ResponseEntity<>(new BalanceResponse(total, membered), HttpStatus.OK);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
