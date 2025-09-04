/* Copyright (c) 2024 */
package com.potrt.stats.data.game;

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

/** A {@link Game} represents a single night/session of a type of game. */
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false)
  private Integer clubId;

  @Column(nullable = false)
  private Integer gameTypeId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private boolean forCash;

  @Column(nullable = false)
  private Integer seasonId;

  @Column(nullable = false)
  private Integer netScoreChange;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date startDate;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private boolean isDeleted;
}
