/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.entities.Person;
import com.potrt.stats.repositories.PersonRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Person getPerson(Integer id) {
    Optional<Person> person = personRepository.findById(id);

    if (person.isEmpty()) {
      return null;
    }

    return person.get();
  }
}
