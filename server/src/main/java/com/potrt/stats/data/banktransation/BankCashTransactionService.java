/* Copyright (c) 2024 */
package com.potrt.stats.data.banktransation;

import com.potrt.stats.data.banktransation.BankCashTransaction.MaskedBankCashTransaction;
import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.club.ClubService;
import com.potrt.stats.data.membership.MembershipService;
import com.potrt.stats.data.membership.PersonRole;
import com.potrt.stats.data.person.Person;
import com.potrt.stats.endpoints.groups.id.bank.BankCashTransactionDto;
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
  private ClubService clubService;
  private MembershipService membershipService;
  private BankCashTransactionRepository bankCashTransactionRepository;

  /** Autowires a {@link BankCashTransactionService}. */
  @Autowired
  public BankCashTransactionService(
      SecurityService securityService,
      ClubService clubService,
      MembershipService membershipService,
      BankCashTransactionRepository bankCashTransactionRepository) {
    this.securityService = securityService;
    this.clubService = clubService;
    this.membershipService = membershipService;
    this.bankCashTransactionRepository = bankCashTransactionRepository;
  }

  /**
   * Gets all of the {@link BankCashTransaction}s for a {@link Club}.
   *
   * @param clubID The club id.
   * @return The {@link List} of {@link BankCashTransaction}s.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Cash Admin} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if the {@link Club} does not exist.
   */
  public List<MaskedBankCashTransaction> getBankCashTransactions(Integer clubID)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    Integer personID = securityService.getPersonID();
    if (!membershipService.hasRole(personID, clubID, PersonRole.CASH_ADMIN)) {
      throw new UnauthorizedException();
    }

    clubService.getClub(clubID);

    Iterable<BankCashTransaction> bankCashTransactions =
        bankCashTransactionRepository.findByPersonIDAndClubID(personID, clubID);

    List<MaskedBankCashTransaction> outBankCashTransaction = new ArrayList<>();
    for (BankCashTransaction bankCashTransaction : bankCashTransactions) {
      outBankCashTransaction.add(new MaskedBankCashTransaction(bankCashTransaction));
    }

    return outBankCashTransaction;
  }

  /**
   * Creates a new {@link BankCashTransaction}.
   *
   * @param clubID The {@link Club} id.
   * @param bankCashTransactionDto The {@link BankCashTransactionDto}.
   * @throws UnauthenticatedException Thrown if the caller is unauthenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Cash Admin} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if the {@link Club} does not exist.
   * @throws PersonIsNotMemberException Thrown if the {@link BankCashTransaction} is performed on a
   *     {@link Person} who is not a member of the {@link Club}.
   */
  public void addBankCashTransaction(Integer clubID, BankCashTransactionDto bankCashTransactionDto)
      throws UnauthenticatedException,
          UnauthorizedException,
          NoResourceException,
          PersonIsNotMemberException {
    Integer personID = securityService.getPersonID();
    if (!membershipService.hasRole(personID, clubID, PersonRole.CASH_ADMIN)) {
      throw new UnauthorizedException();
    }
    clubService.getClub(clubID);

    if (!membershipService.isMember(bankCashTransactionDto.getUserID(), clubID)) {
      throw new PersonIsNotMemberException();
    }

    Date now = new Date();
    BankCashTransaction bankCashTransaction =
        new BankCashTransaction(
            null,
            bankCashTransactionDto.getUserID(),
            clubID,
            bankCashTransactionDto.getDeposit(),
            false,
            now,
            personID,
            now,
            personID);
    bankCashTransactionRepository.save(bankCashTransaction);
  }
}
