/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.local;

import com.potrt.stats.entities.Person;
import com.potrt.stats.repositories.PersonRepository;
import com.potrt.stats.security.PersonDto;
import com.potrt.stats.security.auth.exceptions.PersonDoesNotExistException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthLocalService implements UserDetailsService {

  private AuthLocalRepository authLocalRepository;
  private PersonRepository personRepository;
  private AuthLocalPasswordRepository authLocalPasswordRepository;

  @Autowired
  public AuthLocalService(
      AuthLocalRepository authLocalRepository,
      AuthLocalPasswordRepository authLocalPasswordRepository,
      PersonRepository personRepository) {
    this.authLocalRepository = authLocalRepository;
    this.personRepository = personRepository;
    this.authLocalPasswordRepository = authLocalPasswordRepository;
  }

  @Override
  public AuthLocalPrincipal loadUserByUsername(String loginName) {
    AuthLocal authLocal = authLocalRepository.findByUsername(loginName);
    if (authLocal == null) {
      authLocal = authLocalRepository.findByEmail(loginName);
    }
    if (authLocal == null) {
      throw new UsernameNotFoundException(loginName);
    }

    Optional<Person> person = personRepository.findById(authLocal.getPersonID());
    if (person.isEmpty()) {
      throw new PersonDoesNotExistException(authLocal.getPersonID());
    }
    authLocal.setPerson(person.get());
    return new AuthLocalPrincipal(authLocal);
  }

  @Transactional
  public Person registerNewPerson(PersonDto personDto, AuthLocalPasswordDto authLocalDto) {
    Person person = personDto.toPerson();
    person = personRepository.save(person);
    AuthLocalPassword authLocalPassword = authLocalDto.toAuthLocalPassword(person.getId());
    authLocalPasswordRepository.save(authLocalPassword);
    return person;
  }
}
