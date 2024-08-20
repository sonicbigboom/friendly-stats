/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

public interface AuthService {
  @Transactional
  public Person registerPerson(RegisterDto registerDto)
      throws EmailAlreadyExistsException, UsernameAlreadyExistsException;

  public Authentication login(
      @Valid LoginDto loginDto, AuthenticationManager authenticationManager);

  static void checkAccountStatus(Person person) throws BadCredentialsException {
    if (Boolean.TRUE.equals(person.getIsDisabled())) {
      throw new BadCredentialsException("Account is disabled.");
    }

    if (Boolean.TRUE.equals(person.getIsDisabled())) {
      throw new BadCredentialsException("Account is deleted.");
    }
  }
}
