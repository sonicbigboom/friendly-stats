/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.basic;

import com.potrt.stats.data.person.Person;
import com.potrt.stats.data.person.PersonService;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A {@link UserDetailsService} implementation using this application's {@link AuthBasic} system.
 */
@Service
public class AuthBasicUserDetailsServiceImpl implements UserDetailsService {

  private AuthBasicRepository authBasicRepository;
  private PersonService personService;

  /** Autowires an {@link AuthBasicUserDetailsServiceImpl}. */
  @Autowired
  public AuthBasicUserDetailsServiceImpl(
      AuthBasicRepository authBasicRepository, PersonService personService) {
    this.authBasicRepository = authBasicRepository;
    this.personService = personService;
  }

  /**
   * Loads a {@link Person} by either their username or email.
   *
   * @param loginName The username or password of the {@link Person}.
   */
  @Override
  public AuthBasicPrincipal loadUserByUsername(String loginName) throws UsernameNotFoundException {
    AuthBasic authBasic = authBasicRepository.findByUsername(loginName);
    if (authBasic == null) {
      authBasic = authBasicRepository.findByEmail(loginName);
    }
    if (authBasic == null) {
      throw new UsernameNotFoundException(loginName);
    }

    try {
      authBasic.setPerson(personService.getPersonWithoutAuthCheck(authBasic.getPersonID()));
      return new AuthBasicPrincipal(authBasic);
    } catch (PersonDoesNotExistException e) {
      throw new UsernameNotFoundException("Username or password not found.", e);
    }
  }
}
