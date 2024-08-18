/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthExistsValidator implements ConstraintValidator<AuthExists, Object> {

  @Override
  public void initialize(AuthExists constraintAnnotation) {
    /* Impl */
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    RegisterDto register = (RegisterDto) obj;
    return AuthType.authExists(register.getAuthType());
  }
}