/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import com.potrt.stats.data.person.Person;

/**
 * An {@link Exception} when the {@link Person} is not authorized to make this request.
 *
 * @fs.httpStatus 403 Forbidden
 */
public class UnauthorizedException extends Exception {
  public UnauthorizedException() {
    super();
  }

  public UnauthorizedException(Throwable cause) {
    super(cause);
  }

  public UnauthorizedException(String msg) {
    super(msg);
  }

  public UnauthorizedException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
