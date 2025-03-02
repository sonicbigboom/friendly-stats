/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.groups.id.bank;

import com.potrt.stats.data.banktransation.BankCashTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link BankCashTransactionNewDto} represents a new {@link BankCashTransaction}. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankCashTransactionNewDto {
  private Integer userId;
  private Integer deposit;
}
