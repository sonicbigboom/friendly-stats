/* Copyright (c) 2024 */
package com.potrt.stats.data.person;

import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * The {@link PersonService} is a service that allows for getting, creating, editing, and deleting
 * of {@link Person}s.
 */
@Service
@Transactional
public class PersonService {

  private PersonRepository personRepository;

  /** Autowires a {@link PersonService}. */
  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  /**
   * Gets a {@link Person} by id.
   *
   * @param id The {@link Person} id.
   * @return The associated {@link Person}.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist or is deleted.
   * @apiNote This method does not check for authorization.
   */
  public Person getPersonWithoutAuthCheck(Integer id) throws PersonDoesNotExistException {
    Optional<Person> person = personRepository.findById(id);

    if (person.isEmpty() || person.get().isDeleted()) {
      throw new PersonDoesNotExistException(id);
    }

    return person.get();
  }

  /**
   * Gets a {@link Person} by email.
   *
   * @param email The {@link Person}'s email.
   * @return The associated {@link Person}.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist or is deleted.
   * @apiNote This method does not check for authorization.
   */
  public Person getPersonWithoutAuthCheck(String email) throws PersonDoesNotExistException {
    Optional<Person> person = personRepository.findByEmail(email);

    if (person.isEmpty() || person.get().isDeleted()) {
      throw new PersonDoesNotExistException("Email not found: " + email);
    }

    return person.get();
  }

  /**
   * Determines whether a username has been taken.
   *
   * @param username The username.
   * @return Whether the username is taken.
   */
  public boolean isUsernameTaken(String username) {
    return personRepository.isUsernameTaken(username);
  }

  /**
   * Registers a new {@link Person}.
   *
   * @param registerDto The new {@link Person}'s info inside of a {@link RegisterDto}.
   * @return The newly registered {@link Person}.
   */
  public Person register(RegisterDto registerDto)
      throws EmailAlreadyExistsException, UsernameAlreadyExistsException {
    Person insertablePerson = registerDto.getPerson();

    if (isUsernameTaken(registerDto.getUsername())) {
      throw new UsernameAlreadyExistsException();
    }

    if (isEmailTaken(registerDto.getEmail())) {
      throw new EmailAlreadyExistsException();
    }

    return personRepository.save(insertablePerson);
  }

  /**
   * Enables a {@link Person} account.
   *
   * @param personId The {@link Person} to enable's id.
   * @apiNote This method does not check authorization.
   */
  public void enableWithoutAuthCheck(Integer personId) {
    personRepository.enable(personId);
  }

  /**
   * Gets all of the non-private {@link Person}s that match the filter. (If the filter is null,
   * everyone is included).
   *
   * @param filter A string that is compared to all of a {@link Person}'s fields.
   * @return A {@link List} of {@link Person}s who contain the field.
   */
  public List<Person> getPersons(String filter) {
    Iterable<Person> persons;
    if (filter == null) {
      persons = personRepository.findAll();
    } else {
      persons = personRepository.findAllThatContain("%" + filter + "%");
    }

    List<Person> maskedPersons = new ArrayList<>();
    for (Person person : persons) {
      if (person.isDeleted() || person.isPrivate()) {
        continue;
      }
      maskedPersons.add(person);
    }

    return maskedPersons;
  }

  /**
   * Determines whether a email has been taken.
   *
   * @param email The email.
   * @return Whether the email is taken.
   */
  private boolean isEmailTaken(String email) {
    return personRepository.isEmailTaken(email);
  }

  /**
   * Creates a person from the given email.
   *
   * @param email The email of the new person.
   * @return The created {@link Person}.
   */
  public Person createPersonWithoutAuthCheck(String email) {
    return personRepository.save(
        new Person(null, email, UUID.randomUUID().toString(), null, null, null, true, true, false));
  }
}
