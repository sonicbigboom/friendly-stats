/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.groups.id.bank;

import com.potrt.stats.data.banktransation.BankCashTransaction;
import com.potrt.stats.data.banktransation.BankCashTransactionResponse;
import com.potrt.stats.data.banktransation.BankCashTransactionService;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonIsNotMemberException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Creates an endpoint for viewing and adding bank transactions. */
@RestController
public class GroupsIdBankEndpoint {

  private BankCashTransactionService bankCashTransactionService;

  /** Autowires a {@link GroupsIdBankEndpoint}. */
  @Autowired
  public GroupsIdBankEndpoint(BankCashTransactionService bankCashTransactionService) {
    this.bankCashTransactionService = bankCashTransactionService;
  }

  /**
   * The {@code /groups/{groupId}/bank} {@code GET} endpoint returns the bank transactions the
   * target group.
   */
  @GetMapping("/groups/{groupId}/bank")
  public ResponseEntity<List<BankCashTransactionResponse>> getBankTransactions(
      @PathVariable(value = "groupId") String groupId) {
    try {
      List<BankCashTransaction> bankCashTransactions =
          bankCashTransactionService.getBankCashTransactions(Integer.valueOf(groupId));

      if (bankCashTransactions.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      List<BankCashTransactionResponse> out = new ArrayList<>();
      for (BankCashTransaction bankCashTransaction : bankCashTransactions) {
        out.add(new BankCashTransactionResponse(bankCashTransaction));
      }

      return new ResponseEntity<>(out, HttpStatus.OK);
    } catch (NumberFormatException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (NoResourceException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * The {@code /groups/{groupId}/bank} {@code POST} endpoint creates a new bank transaction for the
   * target group.
   */
  @PostMapping("/groups/{groupId}/bank")
  public ResponseEntity<Void> addBankTransaction(
      @PathVariable(value = "groupId") String groupId,
      @RequestBody BankCashTransactionNewDto bankCashTransactionDto) {
    try {
      bankCashTransactionService.addBankCashTransaction(
          Integer.valueOf(groupId),
          bankCashTransactionDto.getUserId(),
          bankCashTransactionDto.getDeposit());
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (NumberFormatException | PersonIsNotMemberException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (NoResourceException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
