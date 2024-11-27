/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import com.potrt.stats.data.person.Person;
import lombok.experimental.StandardException;

/**
 * An {@link Exception} when the {@link Person} is not authorized to make this request.
 *
 * @fs.httpStatus 403 Forbidden
 */
@StandardException
public class UnauthorizedException extends Exception {}
