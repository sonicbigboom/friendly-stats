/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.games.id.record;

import com.potrt.stats.data.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link GameRecordDto} represents a new {@link Game}. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRecordDto {
  private Integer userID;
  private Integer scoreChange;
}
