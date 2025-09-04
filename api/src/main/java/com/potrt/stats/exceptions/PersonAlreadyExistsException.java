/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.person.Person;
import lombok.experimental.StandardException;

/**
 * An {@link Exception} when trying to create a {@link Person} for a {@link Club} when that there is
 * already an activated account.
 *
 * @fs.httpStatus 409 Conflict
 */
@StandardException
public class PersonAlreadyExistsException extends Exception {}
