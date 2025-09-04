/* Copyright (c) 2024 */
package com.potrt.stats.data.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** A {@link GamePerson} represents the compounded id. */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GamePerson {
  private Integer gameId;
  private Integer personId;
}
