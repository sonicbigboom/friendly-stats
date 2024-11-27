/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import lombok.experimental.StandardException;

/**
 * An {@link Exception} when an external communication fails.
 *
 * @fs.httpStatus 500 Internal Server Error
 */
@StandardException
public class BadExternalCommunicationException extends Exception {}
