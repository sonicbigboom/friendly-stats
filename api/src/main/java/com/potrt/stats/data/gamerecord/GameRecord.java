/* Copyright (c) 2024 */
package com.potrt.stats.data.gamerecord;

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

/** A {@link GameRecord} represents a record in a game. */
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false)
  private Integer gameId;

  @Column(nullable = false)
  private Integer personId;

  @Column(nullable = false)
  private Integer scoreChange;

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

  /**
   * Returns a duplicate without data that a basic member cannot see.
   *
   * @return The masked duplicate.
   */
  public GameRecord mask() {
    return new GameRecord(id, gameId, personId, scoreChange, isDeleted, null, null, null, null);
  }
}
