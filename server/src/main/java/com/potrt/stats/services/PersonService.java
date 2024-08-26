/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.entities.Person;
import com.potrt.stats.entities.Person.MaskedPerson;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.repositories.PersonRepository;
import com.potrt.stats.security.SecurityService;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import com.potrt.stats.security.auth.verification.VerificationService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * The {@link PersonService} is a service that allows for getting, creating, editing, and deleting
 * of {@link Person}s.
 */
@Service
@Transactional
public class PersonService {

  private SecurityService securityService;
  private PersonRepository personRepository;

  /** Autowires a {@link PersonService}. */
  public PersonService(SecurityService securityService, PersonRepository personRepository) {
    this.securityService = securityService;
    this.personRepository = personRepository;
  }

  /**
   * Gets a {@link Person} by id.
   *
   * @param id The {@link Person} id.
   * @return The associated {@link Person}.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist or is deleted.
   */
  public Person getPerson(Integer id) throws PersonDoesNotExistException {
    Optional<Person> person = personRepository.findById(id);

    if (person.isEmpty() || person.get().isDeleted()) {
      throw new PersonDoesNotExistException(id);
    }

    return person.get();
  }

  /**
   * Gets a {@link Person} by username or email.
   *
   * @param email The {@link Person}'s username or email.
   * @return The associated {@link Person}.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist or is deleted.
   */
  public Person getPerson(String loginName) throws PersonDoesNotExistException {
    Optional<Person> person = personRepository.findByUsernameOrEmail(loginName);

    if (person.isEmpty() || person.get().isDeleted()) {
      throw new PersonDoesNotExistException("Login name not found: " + loginName);
    }

    return person.get();
  }

  /**
   * Gets a {@link Person} by email.
   *
   * @param email The {@link Person}'s email.
   * @return The associated {@link Person}.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist or is deleted.
   */
  public Person getPersonByEmail(String email) throws PersonDoesNotExistException {
    Optional<Person> person = personRepository.findByEmail(email);

    if (person.isEmpty() || person.get().isDeleted()) {
      throw new PersonDoesNotExistException("Email not found: " + email);
    }

    return person.get();
  }

  /**
   * Gets a {@link Person} by username.
   *
   * @param username The {@link Person}'s username.
   * @return The associated {@link Person}.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist or is deleted.
   */
  public Person getPersonByUsername(String username) throws PersonDoesNotExistException {
    Optional<Person> person = personRepository.findByUsername(username);

    if (person.isEmpty() || person.get().isDeleted()) {
      throw new PersonDoesNotExistException("Username not found: " + username);
    }

    return person.get();
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
      throw new EmailAlreadyExistsException();
    }

    if (isEmailTaken(registerDto.getEmail())) {
      throw new UsernameAlreadyExistsException();
    }

    return personRepository.save(insertablePerson);
  }

  /**
   * Enables a {@link Person}.
   *
   * @param personID The {@link Person}'s id.
   * @implNote This method does not check the caller's authentication/authorization.
   * @apiNote This should only be called by the {@link VerificationService}.
   */
  public void enable(Integer personID) {
    personRepository.enable(personID);
  }

  /**
   * Gets all of the {@link Person}s that match the filter. If the filter is null, gets every {@link
   * Person}.
   *
   * @param filter A string that is compared to all of a {@link Person}'s fields.
   * @return A {@link List} of {@link MaskedPerson}s who contain the field.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   */
  public List<MaskedPerson> getPersons(String filter) throws UnauthenticatedException {
    securityService.getPerson();

    // TODO: Each {@link Person} should have a private field, and should not be included if they are
    // private.

    Iterable<Person> persons;
    if (filter == null) {
      persons = personRepository.findAll();
    } else {
      persons = personRepository.findAllThatContain("%" + filter + "%");
    }

    List<MaskedPerson> maskedPersons = new ArrayList<>();
    for (Person person : persons) {
      if (Boolean.TRUE.equals(person.isDeleted())) {
        continue;
      }
      maskedPersons.add(new MaskedPerson(person));
    }

    return maskedPersons;
  }

  /**
   * Gets all of the {@link Person}s with the collection of {@link Person} ids.
   *
   * @param personIDs The collection of {@link Person} ids.
   * @return A list of {@link MaskedPerson}s.
   * @throws UnauthenticatedException Thrown if the user is not authenticated.
   */
  public List<MaskedPerson> getPersons(Iterable<Integer> personIDs)
      throws UnauthenticatedException {
    securityService.getPerson();

    Iterable<Person> persons = personRepository.findAllById(personIDs);

    List<MaskedPerson> maskedPersons = new ArrayList<>();
    for (Person person : persons) {
      if (person.isDeleted()) {
        continue;
      }
      maskedPersons.add(new MaskedPerson(person));
    }

    return maskedPersons;
  }

  @Deprecated
  public Person createPerson(Person person) {
    return personRepository.save(person);
  }
}
