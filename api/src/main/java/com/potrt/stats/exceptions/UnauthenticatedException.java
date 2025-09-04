/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import com.potrt.stats.data.person.Person;
import lombok.experimental.StandardException;

/**
 * An {@link Exception} when no {@link Person} is authenticated for a secure request.
 *
 * @fs.httpStatus 401 Unauthorized
 */
@StandardException
public class UnauthenticatedException extends Exception {}
