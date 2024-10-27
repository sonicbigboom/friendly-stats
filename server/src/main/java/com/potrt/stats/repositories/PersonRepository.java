/* Copyright (c) 2024 */
package com.potrt.stats.repositories;

import com.potrt.stats.entities.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {

  public Optional<Person> findByEmail(String email);

  @Query(
      "SELECT CASE WHEN EXISTS (SELECT P.email FROM Person P WHERE email = :email ) THEN true ELSE false END")
  public boolean isEmailTaken(String email);

  @Query(
      "SELECT CASE WHEN EXISTS (SELECT P.username FROM Person P WHERE username = :username ) THEN true ELSE false END")
  public boolean isUsernameTaken(String username);

  @Modifying
  @Query("UPDATE Person SET isDisabled = 0 WHERE id = :personID")
  public void enable(Integer personID);

  @Query(
      "SELECT P FROM Person P WHERE username LIKE :filter OR firstName LIKE :filter OR lastName LIKE :filter OR nickname LIKE :filter")
  public Iterable<Person> findAllThatContain(String filter);
}
