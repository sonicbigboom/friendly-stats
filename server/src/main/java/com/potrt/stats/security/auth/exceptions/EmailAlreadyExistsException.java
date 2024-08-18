/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

public class EmailAlreadyExistsException extends Exception {
  public EmailAlreadyExistsException() {
    super();
  }

  public EmailAlreadyExistsException(Throwable cause) {
    super(cause);
  }

  public EmailAlreadyExistsException(String msg) {
    super(msg);
  }

  public EmailAlreadyExistsException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
