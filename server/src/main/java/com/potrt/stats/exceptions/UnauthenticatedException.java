/* Copywrite (c) 2024 */
package com.potrt.stats.exceptions;

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
