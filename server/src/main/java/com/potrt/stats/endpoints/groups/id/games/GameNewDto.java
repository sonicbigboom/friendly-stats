/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.groups.id.games;

import com.potrt.stats.data.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link GameNewDto} represents a new {@link Game}. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameNewDto {
  private String name;
  private Integer gameTypeId;
  private boolean forCash;
  private Integer seasonId;
}
