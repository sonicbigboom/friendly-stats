/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.games.id.records;

import com.potrt.stats.data.gamerecord.GameRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link GameRecordNewDto} represents a new {@link GameRecord}. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRecordNewDto {
  private Integer userId;
  private Integer scoreChange;
}
