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
  private Integer clubID;

  @Column(nullable = false)
  private Integer gameTypeID;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private boolean forCash;

  @Column(nullable = false)
  private Integer seasonID;

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

  /** A {@link MaskedGame} with private information hidden. */
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class MaskedGame {
    private Integer id;
    private Integer clubID;
    private Integer gameTypeID;
    private String name;
    private boolean forCash;
    private Integer seasonID;
    private Integer netScoreChange;
    private Date startDate;
    private Date endDate;

    /** A {@link MaskedGame} with public information. */
    public MaskedGame(Game game) {
      this.id = game.id;
      this.clubID = game.clubID;
      this.gameTypeID = game.gameTypeID;
      this.name = game.name;
      this.forCash = game.forCash;
      this.seasonID = game.seasonID;
      this.netScoreChange = game.netScoreChange;
      this.startDate = game.startDate;
      this.endDate = game.endDate;
    }
  }
}
