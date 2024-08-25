/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates that a field decorated with {@link AuthExists} contains a recognized authentication
 * type.
 */
public class AuthExistsValidator implements ConstraintValidator<AuthExists, Object> {

  /** Checks that the field object is a string with a recognized authentication type. */
  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    String authType = (String) obj;
    return AuthType.authExists(authType);
  }
}
