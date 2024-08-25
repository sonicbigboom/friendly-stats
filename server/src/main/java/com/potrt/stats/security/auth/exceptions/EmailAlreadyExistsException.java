/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

import com.potrt.stats.entities.Person;

/** An {@link Exception} for registering a {@link Person} with an email that already exists. */
public class EmailAlreadyExistsException extends Exception {
  public EmailAlreadyExistsException() {
    super();
  }

  public EmailAlreadyExistsException(Throwable cause) {
    super(cause);
  }

  public EmailAlreadyExistsException(String msg) {
    super(msg);
  }

  public EmailAlreadyExistsException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
