/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.local;

import com.potrt.stats.entities.Person;
import com.potrt.stats.repositories.PersonRepository;
import com.potrt.stats.security.auth.exceptions.PersonDoesNotExistException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthLocalService implements UserDetailsService {

  private AuthLocalRepository authLocalRepository;
  private PersonRepository personRepository;

  @Autowired
  public AuthLocalService(
      AuthLocalRepository authLocalRepository, PersonRepository personRepository) {
    this.authLocalRepository = authLocalRepository;
    this.personRepository = personRepository;
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
}
