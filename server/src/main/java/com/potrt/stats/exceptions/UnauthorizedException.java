/* Copywrite (c) 2024 */
package com.potrt.stats.exceptions;

public class UnauthorizedException extends Exception {
  public UnauthorizedException() {
    super();
  }

  public UnauthorizedException(Throwable cause) {
    super(cause);
  }

  public UnauthorizedException(String msg) {
    super(msg);
  }

  public UnauthorizedException(String msg, Throwable cause) {
    super(msg, cause);
  }
}