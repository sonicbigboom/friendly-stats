/* Copywrite (c) 2024 */
package com.potrt.stats.repositories;

import com.potrt.stats.entities.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {}
