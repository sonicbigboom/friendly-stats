/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

import javax.naming.AuthenticationException;

/** An {@link AuthenticationException} for trying to use an expired token. */
public class TokenExpiredException extends AuthenticationException {
  public TokenExpiredException() {
    super();
  }

  public TokenExpiredException(String msg) {
    super(msg);
  }
}
