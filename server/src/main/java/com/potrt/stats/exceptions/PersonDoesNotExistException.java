/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import com.potrt.stats.entities.Person;

/**
 * An {@link Exception} for attempting to get a {@link Person} that does not exist.
 *
 * @fs.httpStatus 404 Not Found
 */
public class PersonDoesNotExistException extends Exception {

  private static final String PERSON_ID_NOT_FOUND = "Could not find a person with id %d.";

  /**
   * Creates a {@link PersonDoesNotExistException} with the {@link Person} id that cannot be found.
   *
   * @param id The {@link Person} id.
   */
  public PersonDoesNotExistException(Integer id) {
    super(String.format(PERSON_ID_NOT_FOUND, id));
  }

  /**
   * Creates a {@link PersonDoesNotExistException} with the {@link Person} id that cannot be found.
   *
   * @param id The {@link Person} id.
   * @param cause The {@link Throwable} that caused this exception.
   */
  public PersonDoesNotExistException(Integer id, Throwable cause) {
    super(String.format(PERSON_ID_NOT_FOUND, id), cause);
  }

  public PersonDoesNotExistException() {
    super();
  }

  public PersonDoesNotExistException(Throwable cause) {
    super(cause);
  }

  public PersonDoesNotExistException(String msg) {
    super(msg);
  }

  public PersonDoesNotExistException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
