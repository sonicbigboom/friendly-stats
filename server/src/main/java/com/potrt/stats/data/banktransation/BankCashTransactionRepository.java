/* Copyright (c) 2024 */
package com.potrt.stats.data.banktransation;

import org.springframework.data.repository.CrudRepository;

/** A {@link CrudRepository} for a {@link BankCashTransaction}. */
public interface BankCashTransactionRepository
    extends CrudRepository<BankCashTransaction, Integer> {

  public Iterable<BankCashTransaction> findByPersonIDAndClubID(Integer personID, Integer clubID);
}
