/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

public interface AuthService {
  @Transactional
  public Person registerPerson(RegisterDto registerDto)
      throws EmailAlreadyExistsException, UsernameAlreadyExistsException;

  public Authentication login(
      @Valid LoginDto loginDto, AuthenticationManager authenticationManager);
}
