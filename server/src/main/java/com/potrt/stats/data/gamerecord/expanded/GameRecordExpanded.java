/* Copyright (c) 2024 */
package com.potrt.stats.data.gamerecord.expanded;

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

/** A {@link GameRecordExpanded} represents a game record with more information. */
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameRecordExpanded {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false)
  private Integer clubID;

  @Column(nullable = false)
  private Integer gameID;

  @Column(nullable = false)
  private Integer gameTypeID;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private Boolean forCash;

  @Column(nullable = false)
  private Integer seasonID;

  @Column(nullable = false)
  private Integer personID;

  @Column(nullable = false)
  private Integer scoreChange;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date date;
}
