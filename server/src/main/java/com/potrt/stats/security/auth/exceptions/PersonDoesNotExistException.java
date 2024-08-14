/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class PersonDoesNotExistException extends AuthenticationException {

  private static final String PERSON_ID_NOT_FOUND = "Could not find a person with id %d.";

  public PersonDoesNotExistException(Integer id) {
    super(String.format(PERSON_ID_NOT_FOUND, id));
  }

  public PersonDoesNotExistException(Integer id, Throwable cause) {
    super(String.format(PERSON_ID_NOT_FOUND, id), cause);
  }

  public PersonDoesNotExistException(String msg) {
    super(msg);
  }

  public PersonDoesNotExistException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
