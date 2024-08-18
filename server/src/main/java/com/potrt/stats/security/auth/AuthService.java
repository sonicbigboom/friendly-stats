/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;

public interface AuthService {
  public Person registerPerson(RegisterDto registerDto)
      throws EmailAlreadyExistsException, UsernameAlreadyExistsException;
}
