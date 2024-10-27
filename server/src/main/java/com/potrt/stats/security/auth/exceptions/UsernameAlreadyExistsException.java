/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

import com.potrt.stats.data.person.Person;

/** An {@link Exception} for registering a {@link Person} with an username that already exists. */
public class UsernameAlreadyExistsException extends Exception {
  public UsernameAlreadyExistsException() {
    super();
  }

  public UsernameAlreadyExistsException(Throwable cause) {
    super(cause);
  }

  public UsernameAlreadyExistsException(String msg) {
    super(msg);
  }

  public UsernameAlreadyExistsException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
