/* Copywrite (c) 2024 */
package com.potrt.stats.exceptions;

import com.potrt.stats.entities.Person;

/**
 * An {@link Exception} when no {@link Person} is authenticated for a secure request.
 *
 * @fs.httpStatus 401 Unauthorized
 */
public class UnauthenticatedException extends Exception {
  public UnauthenticatedException() {
    super();
  }

  public UnauthenticatedException(Throwable cause) {
    super(cause);
  }

  public UnauthenticatedException(String msg) {
    super(msg);
  }

  public UnauthenticatedException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
