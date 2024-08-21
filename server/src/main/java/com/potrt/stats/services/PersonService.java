/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.entities.Person;
import com.potrt.stats.repositories.PersonRepository;
import com.potrt.stats.security.auth.exceptions.PersonDoesNotExistException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Person getPerson(Integer id) throws PersonDoesNotExistException {
    Optional<Person> person = personRepository.findById(id);

    if (person.isEmpty()) {
      throw new PersonDoesNotExistException(id);
    }

    return person.get();
  }

  public Person register(Person person) {
    // TODO: Check for username and email duplicates, and throw error as necessary.
    return personRepository.save(person);
  }

  public void enable(Integer personID) {
    personRepository.enable(personID);
  }
}
