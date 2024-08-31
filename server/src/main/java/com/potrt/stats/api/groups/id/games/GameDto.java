/* Copywrite (c) 2024 */
package com.potrt.stats.api.groups.id.games;

import com.potrt.stats.entities.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link GameDto} represents a new {@link Game}. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {
  private Integer gameTypeID;
  private Integer forCash;
  private Integer seasonID;
}
