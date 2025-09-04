/* Copyright (c) 2024 */
package com.potrt.stats.data.player;

import org.springframework.data.repository.CrudRepository;

/** A {@link CrudRepository} for a {@link GamePlayer}. */
public interface GamePlayerRepository extends CrudRepository<GamePlayer, GamePerson> {

  public Iterable<GamePlayer> findByGameId(Integer gameId);
}
