/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

import com.potrt.stats.data.person.Person;
import lombok.experimental.StandardException;

/** An {@link Exception} for registering a {@link Person} with an email that already exists. */
@StandardException
public class EmailAlreadyExistsException extends Exception {}
