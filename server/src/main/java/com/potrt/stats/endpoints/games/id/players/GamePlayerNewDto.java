/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.games.id.players;

import com.potrt.stats.data.player.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link GamePlayerNewDto} represents a new {@link GamePlayer}. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GamePlayerNewDto {
  private Integer userId;
  private String metadata;
}
