/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

public class UsernameAlreadyExistsException extends Exception {
  public UsernameAlreadyExistsException() {
    super();
  }

  public UsernameAlreadyExistsException(Throwable cause) {
    super(cause);
  }

  public UsernameAlreadyExistsException(String msg) {
    super(msg);
  }

  public UsernameAlreadyExistsException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
