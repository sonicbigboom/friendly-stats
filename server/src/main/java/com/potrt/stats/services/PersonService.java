/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.entities.Person;
import com.potrt.stats.entities.Person.MaskedPerson;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.repositories.PersonRepository;
import com.potrt.stats.security.SecurityService;
import com.potrt.stats.security.auth.exceptions.PersonDoesNotExistException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private SecurityService securityService;
  private PersonRepository personRepository;

  public PersonService(SecurityService securityService, PersonRepository personRepository) {
    this.securityService = securityService;
    this.personRepository = personRepository;
  }

  public Person getPerson(Integer id) throws PersonDoesNotExistException {
    Optional<Person> person = personRepository.findById(id);

    if (person.isEmpty()) {
      throw new PersonDoesNotExistException(id);
    }

    return person.get();
  }

  public Person getPerson(String email) throws PersonDoesNotExistException {
    Optional<Person> person = personRepository.findByEmail(email);

    if (person.isEmpty()) {
      throw new PersonDoesNotExistException("Email not found: " + email);
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

  public List<MaskedPerson> getPersons(Optional<String> filter)
      throws UnauthenticatedException, NoResourceException {
    securityService.getPerson();

    Iterable<Person> persons;
    if (filter.isEmpty()) {
      persons = personRepository.findAll();
    } else {
      persons = personRepository.findAllThatContain("%" + filter.get() + "%");
    }

    List<MaskedPerson> maskedPersons = new ArrayList<>();
    for (Person person : persons) {
      maskedPersons.add(new MaskedPerson(person));
    }

    if (maskedPersons.isEmpty()) {
      throw new NoResourceException();
    }

    return maskedPersons;
  }
}
