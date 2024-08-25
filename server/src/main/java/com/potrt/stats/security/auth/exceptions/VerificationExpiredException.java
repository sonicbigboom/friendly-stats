/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

import javax.naming.AuthenticationException;

/** An {@link AuthenticationException} for trying to use an expired verification token. */
public class VerificationExpiredException extends AuthenticationException {
  public VerificationExpiredException() {
    super();
  }

  public VerificationExpiredException(String msg) {
    super(msg);
  }
}
