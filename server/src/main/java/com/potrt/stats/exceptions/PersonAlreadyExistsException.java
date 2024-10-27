/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.person.Person;

/**
 * An {@link Exception} when trying to create a {@link Person} for a {@link Club} when that there is
 * already an activated account.
 *
 * @fs.httpStatus 409 Conflict
 */
public class PersonAlreadyExistsException extends Exception {
  public PersonAlreadyExistsException() {
    super();
  }

  public PersonAlreadyExistsException(Throwable cause) {
    super(cause);
  }

  public PersonAlreadyExistsException(String msg) {
    super(msg);
  }

  public PersonAlreadyExistsException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
