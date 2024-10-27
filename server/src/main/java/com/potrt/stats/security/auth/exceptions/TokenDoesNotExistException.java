/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

import javax.naming.AuthenticationException;

/** An {@link AuthenticationException} for trying to use a non-existant token. */
public class TokenDoesNotExistException extends AuthenticationException {
  public TokenDoesNotExistException() {
    super();
  }

  public TokenDoesNotExistException(String msg) {
    super(msg);
  }
}
