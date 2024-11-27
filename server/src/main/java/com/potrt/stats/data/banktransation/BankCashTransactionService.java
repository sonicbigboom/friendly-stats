/* Copyright (c) 2024 */
package com.potrt.stats.data.banktransation;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.membership.MembershipService;
import com.potrt.stats.data.membership.PersonRole;
import com.potrt.stats.data.person.Person;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonIsNotMemberException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.security.SecurityService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The {@link BankCashTransactionService} is a service that allows for {@link BankCashTransaction}
 * management.
 */
@Service
@Transactional
public class BankCashTransactionService {

  private SecurityService securityService;
  private MembershipService membershipService;
  private BankCashTransactionRepository bankCashTransactionRepository;

  /** Autowires a {@link BankCashTransactionService}. */
  @Autowired
  public BankCashTransactionService(
      SecurityService securityService,
      MembershipService membershipService,
      BankCashTransactionRepository bankCashTransactionRepository) {
    this.securityService = securityService;
    this.membershipService = membershipService;
    this.bankCashTransactionRepository = bankCashTransactionRepository;
  }

  /**
   * Gets all of the {@link BankCashTransaction}s for a {@link Club}.
   *
   * @param clubId The club id.
   * @return The {@link List} of {@link BankCashTransaction}s.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Cash Admin} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if the {@link Club} does not exist.
   */
  public List<BankCashTransaction> getBankCashTransactions(Integer clubId)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    securityService.assertHasPermission(clubId, PersonRole.CASH_ADMIN);

    Integer personId = securityService.getPersonId();
    Iterable<BankCashTransaction> bankCashTransactions =
        bankCashTransactionRepository.findByPersonIdAndClubId(personId, clubId);

    List<BankCashTransaction> out = new ArrayList<>();
    for (BankCashTransaction bankCashTransaction : bankCashTransactions) {
      // Note: No need for masking, as Cash Admin is already required to call this method at all.
      out.add(bankCashTransaction);
    }

    return out;
  }

  /**
   * Creates a new {@link BankCashTransaction}.
   *
   * @param clubId The {@link Club} id.
   * @param personId The {@link Person}'s id.
   * @param deposit The amount to deposit.
   * @throws UnauthenticatedException Thrown if the caller is unauthenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Cash Admin} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if the {@link Club} does not exist.
   * @throws PersonIsNotMemberException Thrown if the {@link BankCashTransaction} is performed on a
   *     {@link Person} who is not a member of the {@link Club}.
   */
  public void addBankCashTransaction(Integer clubId, Integer personId, Integer deposit)
      throws UnauthenticatedException,
          UnauthorizedException,
          NoResourceException,
          PersonIsNotMemberException {
    securityService.assertHasPermission(clubId, PersonRole.CASH_ADMIN);

    if (!membershipService.isMember(personId, clubId)) {
      throw new PersonIsNotMemberException();
    }

    Date now = new Date();
    Integer callerId = securityService.getPersonId();
    BankCashTransaction bankCashTransaction =
        new BankCashTransaction(
            null, personId, clubId, deposit, false, now, callerId, now, callerId);
    bankCashTransactionRepository.save(bankCashTransaction);
  }
}
