/* Copyright (c) 2024 */
package com.potrt.stats.repositories;

import com.potrt.stats.entities.Game;
import org.springframework.data.repository.CrudRepository;

/** A {@link CrudRepository} for a {@link Game}. */
public interface GameRepository extends CrudRepository<Game, Integer> {

  Iterable<Game> findByClubID(Integer clubID);
}
