/* Copyright (c) 2024 */
package com.potrt.stats.security.auth;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This string has to be a recognized authentication type. */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuthExistsValidator.class)
@Documented
public @interface AuthExists {
  String message() default "Given code type is not supported.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
