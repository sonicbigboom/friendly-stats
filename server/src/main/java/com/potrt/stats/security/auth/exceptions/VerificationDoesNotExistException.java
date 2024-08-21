/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

public class VerificationDoesNotExistException extends Exception {
  public VerificationDoesNotExistException() {
    super();
  }

  public VerificationDoesNotExistException(Throwable cause) {
    super(cause);
  }

  public VerificationDoesNotExistException(String msg) {
    super(msg);
  }

  public VerificationDoesNotExistException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
