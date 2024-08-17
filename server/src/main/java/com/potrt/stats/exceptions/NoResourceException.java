/* Copywrite (c) 2024 */
package com.potrt.stats.exceptions;

public class NoResourceException extends Exception {
  public NoResourceException() {
    super();
  }

  public NoResourceException(Throwable cause) {
    super(cause);
  }

  public NoResourceException(String msg) {
    super(msg);
  }

  public NoResourceException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
