/* Copyright (c) 2024 */
package com.potrt.stats.api.groups.id.bank;

import com.potrt.stats.data.banktransation.BankCashTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link BankCashTransactionDto} represents a new {@link BankCashTransaction}. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankCashTransactionDto {
  private Integer userID;
  private Integer deposit;
}
