/* Copyright (c) 2024 */
package com.potrt.stats.data.game;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link Game} represents a single night/session of a type of game. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameResponse {
  private Integer id;
  private Integer groupId;
  private Integer gameTypeId;
  private String name;
  private boolean forCash;
  private Integer seasonId;
  private Integer netScoreChange;
  private Date startDate;
  private Date endDate;

  public GameResponse(Game game) {
    this(
        game.getId(),
        game.getClubId(),
        game.getGameTypeId(),
        game.getName(),
        game.isForCash(),
        game.getSeasonId(),
        game.getNetScoreChange(),
        game.getStartDate(),
        game.getEndDate());
  }
}
