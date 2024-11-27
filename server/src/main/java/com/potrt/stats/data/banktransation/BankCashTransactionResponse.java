/* Copyright (c) 2024 */
package com.potrt.stats.data.banktransation;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A {@link BankCashTransactionResponse} represents a deposit or withdrawal from the group's bank.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BankCashTransactionResponse {
  private Integer id;
  private Integer userId;
  private Integer groupId;
  private Integer deposit;
  private Date createdTime;
  private Integer createdByUserId;
  private Date modifiedTime;
  private Integer modifiedByUserId;

  public BankCashTransactionResponse(BankCashTransaction bankCashTransaction) {
    this(
        bankCashTransaction.getId(),
        bankCashTransaction.getPersonId(),
        bankCashTransaction.getClubId(),
        bankCashTransaction.getDeposit(),
        bankCashTransaction.getCreatedTime(),
        bankCashTransaction.getCreatedByPersonId(),
        bankCashTransaction.getModifiedTime(),
        bankCashTransaction.getModifiedByPersonId());
  }
}
