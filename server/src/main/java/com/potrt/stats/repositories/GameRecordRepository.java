/* Copywrite (c) 2024 */
package com.potrt.stats.repositories;

import com.potrt.stats.entities.GameRecord;
import org.springframework.data.repository.CrudRepository;

/** A {@link CrudRepository} for a {@link GameRecord}. */
public interface GameRecordRepository extends CrudRepository<GameRecord, Integer> {

  public Iterable<GameRecord> findByGameID(Integer gameID);
}
