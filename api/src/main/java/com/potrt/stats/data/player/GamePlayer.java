/* Copyright (c) 2024 */
package com.potrt.stats.data.player;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;

/** A {@link GamePlayer} represents a player in a game. */
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(GamePerson.class)
public class GamePlayer {

  @Id
  @Column(nullable = false)
  private Integer gameId;

  @Id
  @Column(nullable = false)
  private Integer personId;

  @Column(nullable = false)
  private String metadata;

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
  public GamePlayer mask() {
    return new GamePlayer(gameId, personId, metadata, isDeleted, null, null, null, null);
  }
}
