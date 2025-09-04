/* Copyright (c) 2024 */
package com.potrt.stats.data.banktransation;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;

/** A {@link BankCashTransaction} represents a deposit or withdrawal from the group's bank. */
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BankCashTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false)
  private Integer personId;

  @Column(nullable = false)
  private Integer clubId;

  @Column(nullable = false)
  private Integer deposit;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private boolean isDeleted;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTime;

  @Column(nullable = false)
  private Integer createdByPersonId;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date modifiedTime;

  @Column(nullable = false)
  private Integer modifiedByPersonId;
}
