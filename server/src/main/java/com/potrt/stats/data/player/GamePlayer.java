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
  private Integer gameID;

  @Id
  @Column(nullable = false)
  private Integer personID;

  @Column(nullable = false)
  private String metadata;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private boolean isDeleted;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTime;

  @Column(nullable = false)
  private Integer createdByPersonID;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date modifiedTime;

  @Column(nullable = false)
  private Integer modifiedByPersonID;

  /** A {@link MaskedGamePlayer} with private information hidden. */
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class MaskedGamePlayer {
    private Integer gameID;
    private Integer personID;
    private String metadata;
    private Date createdTime;
    private Integer createdByPersonID;
    private Date modifiedTime;
    private Integer modifiedByPersonID;

    /** A {@link MaskedGamePlayer} with public information. */
    public MaskedGamePlayer(GamePlayer gamePlayer) {
      this.gameID = gamePlayer.gameID;
      this.personID = gamePlayer.personID;
      this.metadata = gamePlayer.metadata;
      this.createdTime = gamePlayer.createdTime;
      this.createdByPersonID = gamePlayer.createdByPersonID;
      this.modifiedTime = gamePlayer.modifiedTime;
      this.modifiedByPersonID = gamePlayer.modifiedByPersonID;
    }
  }
}
