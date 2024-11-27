/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import lombok.experimental.StandardException;

/**
 * An {@link Exception} when the requested resource does not exists.
 *
 * @fs.httpStatus 404 Not Found
 */
@StandardException
public class NoResourceException extends Exception {}
