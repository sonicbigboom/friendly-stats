/* Copywrite (c) 2024 */
package com.potrt.stats.api.groups.id.bank;

import com.potrt.stats.entities.BankCashTransaction.MaskedBankCashTransaction;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonIsNotMemberException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.services.BankCashTransactionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Creates an endpoint for viewing and editing bank transactions. */
@RestController
public class GroupsIdBankApi {

  private BankCashTransactionService bankCashTransactionService;

  /** Autowires a {@link GroupsIdBankApi}. */
  @Autowired
  public GroupsIdBankApi(BankCashTransactionService bankCashTransactionService) {
    this.bankCashTransactionService = bankCashTransactionService;
  }

  /**
   * The {@code /groups/{groupID}/bank} {@code GET} endpoint returns the bank transactions the
   * target group.
   */
  @GetMapping("/groups/{groupID}/bank")
  public ResponseEntity<List<MaskedBankCashTransaction>> getBankTransactions(
      @PathVariable(value = "groupID") String groupID) {
    try {
      List<MaskedBankCashTransaction> bankCashTransactions =
          bankCashTransactionService.getBankCashTransactions(Integer.valueOf(groupID));

      if (bankCashTransactions.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(bankCashTransactions, HttpStatus.OK);
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
   * The {@code /groups/{groupID}/bank} {@code POST} endpoint creates a new bank transaction for the
   * target group.
   */
  @PostMapping("/groups/{groupID}/bank")
  public ResponseEntity<Void> addBankTransaction(
      @PathVariable(value = "groupID") String groupID,
      @RequestBody BankCashTransactionDto bankCashTransactionDto) {
    try {
      bankCashTransactionService.addBankCashTransaction(
          Integer.valueOf(groupID), bankCashTransactionDto);
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
