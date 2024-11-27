/* Copyright (c) 2024 */
package com.potrt.stats.data.player;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link GamePlayerResponse} represents a player in a game. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GamePlayerResponse {
  private Integer gameId;
  private Integer userId;
  private String metadata;
  private Date createdTime;
  private Integer createdByUserId;
  private Date modifiedTime;
  private Integer modifiedByUserId;

  public GamePlayerResponse(GamePlayer gamePlayer) {
    this(
        gamePlayer.getGameId(),
        gamePlayer.getPersonId(),
        gamePlayer.getMetadata(),
        gamePlayer.getCreatedTime(),
        gamePlayer.getCreatedByPersonId(),
        gamePlayer.getModifiedTime(),
        gamePlayer.getModifiedByPersonId());
  }
}
