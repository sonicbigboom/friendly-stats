/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import com.potrt.stats.entities.Club;
import com.potrt.stats.entities.Person;

/**
 * An {@link Exception} when trying to perform an action on a {@link Person} for a {@link Club} that
 * the {@link Person} is not a member of.
 *
 * @fs.httpStatus 409 Conflict
 */
public class PersonIsNotMemberException extends Exception {
  public PersonIsNotMemberException() {
    super();
  }

  public PersonIsNotMemberException(Throwable cause) {
    super(cause);
  }

  public PersonIsNotMemberException(String msg) {
    super(msg);
  }

  public PersonIsNotMemberException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
