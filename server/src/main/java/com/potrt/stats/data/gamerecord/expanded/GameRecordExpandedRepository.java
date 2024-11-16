/* Copyright (c) 2024 */
package com.potrt.stats.data.gamerecord.expanded;

import org.springframework.data.jpa.repository.JpaRepository;

/** A {@link CrudRepository} for a {@link GameRecord}. */
public interface GameRecordExpandedRepository extends JpaRepository<GameRecordExpanded, Integer> {}
