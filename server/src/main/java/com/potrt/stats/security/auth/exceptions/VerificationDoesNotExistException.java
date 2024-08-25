/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

import javax.naming.AuthenticationException;

/** An {@link AuthenticationException} for trying to use a non-existant verification token. */
public class VerificationDoesNotExistException extends AuthenticationException {
  public VerificationDoesNotExistException() {
    super();
  }

  public VerificationDoesNotExistException(String msg) {
    super(msg);
  }
}
