/* Copyright (c) 2024 */
package com.potrt.stats.data.gamerecord;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link GameRecordResponse} represents a record in a game. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameRecordResponse {
  private Integer id;
  private Integer gameId;
  private Integer userId;
  private Integer scoreChange;
  private Date createdTime;
  private Integer createdByUserId;
  private Date modifiedTime;
  private Integer modifiedByUserId;

  public GameRecordResponse(GameRecord gameRecord) {
    this(
        gameRecord.getId(),
        gameRecord.getGameId(),
        gameRecord.getPersonId(),
        gameRecord.getScoreChange(),
        gameRecord.getCreatedTime(),
        gameRecord.getCreatedByPersonId(),
        gameRecord.getModifiedTime(),
        gameRecord.getModifiedByPersonId());
  }
}
