/* Copyright (c) 2024 */
package com.potrt.stats.data.game;

import org.springframework.data.repository.CrudRepository;

/** A {@link CrudRepository} for a {@link Game}. */
public interface GameRepository extends CrudRepository<Game, Integer> {

  Iterable<Game> findByClubID(Integer clubID);
}
