/* Copyright (c) 2024 */
package com.potrt.stats.data.person;

import com.potrt.stats.data.person.Person.MaskedPerson;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.security.SecurityService;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import com.potrt.stats.security.auth.verification.VerificationService;
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

  private SecurityService securityService;
  private PersonRepository personRepository;

  /** Autowires a {@link PersonService}. */
  public PersonService(SecurityService securityService, PersonRepository personRepository) {
    this.securityService = securityService;
    this.personRepository = personRepository;
  }

  /**
   * Gets a {@link Person} by id and skips authentication/authorization checks.
   *
   * @param id The {@link Person} id.
   * @return The associated {@link Person}.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist or is deleted.
   * @apiNote This method's output should never be sent to the caller.
   */
  public Person getPersonWithoutAuthorization(Integer id) throws PersonDoesNotExistException {
    Optional<Person> person = personRepository.findById(id);

    if (person.isEmpty() || person.get().isDeleted()) {
      throw new PersonDoesNotExistException(id);
    }

    return person.get();
  }

  /**
   * Gets a {@link Person} by email and skips authentication/authorization checks.
   *
   * @param email The {@link Person}'s email.
   * @return The associated {@link Person}.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist or is deleted.
   * @apiNote This method's output should never be sent to the caller.
   */
  public Person getPersonWithoutAuthorization(String email) throws PersonDoesNotExistException {
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
   * Enables a {@link Person} skips authentication/authorization checks.
   *
   * @param personID The {@link Person}'s id.
   * @apiNote This method should only be called by the {@link VerificationService}.
   */
  public void enableWithoutAuthorization(Integer personID) {
    personRepository.enable(personID);
  }

  /**
   * Gets all of the non-private {@link Person}s that match the filter. (If the filter is null,
   * everyone is included).
   *
   * @param filter A string that is compared to all of a {@link Person}'s fields.
   * @return A {@link List} of {@link MaskedPerson}s who contain the field.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   */
  public List<MaskedPerson> getPersons(String filter) throws UnauthenticatedException {
    securityService.getPerson();

    Iterable<Person> persons;
    if (filter == null) {
      persons = personRepository.findAll();
    } else {
      persons = personRepository.findAllThatContain("%" + filter + "%");
    }

    List<MaskedPerson> maskedPersons = new ArrayList<>();
    for (Person person : persons) {
      if (person.isDeleted() || person.isPrivate()) {
        continue;
      }
      maskedPersons.add(new MaskedPerson(person));
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

  /** TODO: CHECK AUTHORIZATION */
  public Person createPerson(String email) {
    return personRepository.save(
        new Person(null, email, UUID.randomUUID().toString(), null, null, null, true, true, false));
  }
}
