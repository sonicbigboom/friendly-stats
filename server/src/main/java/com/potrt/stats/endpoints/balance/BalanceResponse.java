/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.balance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A response to hold the total cash balance and membered cash balance. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BalanceResponse {
  private Integer total;
  private Integer membered;
}
