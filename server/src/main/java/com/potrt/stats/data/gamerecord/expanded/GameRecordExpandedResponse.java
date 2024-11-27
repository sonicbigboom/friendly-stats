/* Copyright (c) 2024 */
package com.potrt.stats.data.gamerecord.expanded;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link GameRecordExpanded} represents a game record with more information. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameRecordExpandedResponse {
  private Integer id;
  private Integer groupId;
  private Integer gameId;
  private Integer gameTypeId;
  private boolean forCash;
  private Integer seasonId;
  private Integer userId;
  private Integer scoreChange;
  private Date date;

  public GameRecordExpandedResponse(GameRecordExpanded gameRecordExpanded) {
    this(
        gameRecordExpanded.getId(),
        gameRecordExpanded.getClubId(),
        gameRecordExpanded.getGameId(),
        gameRecordExpanded.getGameTypeId(),
        gameRecordExpanded.isForCash(),
        gameRecordExpanded.getSeasonId(),
        gameRecordExpanded.getPersonId(),
        gameRecordExpanded.getScoreChange(),
        gameRecordExpanded.getDate());
  }
}
