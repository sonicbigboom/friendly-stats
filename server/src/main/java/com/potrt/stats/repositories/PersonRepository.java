/* Copywrite (c) 2024 */
package com.potrt.stats.repositories;

import com.potrt.stats.entities.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {
  @Modifying
  @Query("UPDATE Person SET isDisabled = 0 WHERE id = :personID")
  public void enable(Integer personID);

  public Optional<Person> findByEmail(String email);

  @Query(
      "SELECT P FROM Person P WHERE username LIKE :filter OR firstName LIKE :filter OR lastName LIKE :filter OR nickname LIKE :filter")
  public Iterable<Person> findAllThatContain(String filter);
}
