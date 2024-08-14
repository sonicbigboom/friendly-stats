/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.local.validation;

import com.potrt.stats.security.auth.local.AuthLocalPasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {}

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    AuthLocalPasswordDto pass = (AuthLocalPasswordDto) obj;
    return pass.getPassword().equals(pass.getPasswordConfirm());
  }
}
