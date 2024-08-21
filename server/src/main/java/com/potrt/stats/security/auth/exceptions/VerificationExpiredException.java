/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

public class VerificationExpiredException extends Exception {
  public VerificationExpiredException() {
    super();
  }

  public VerificationExpiredException(Throwable cause) {
    super(cause);
  }

  public VerificationExpiredException(String msg) {
    super(msg);
  }

  public VerificationExpiredException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
