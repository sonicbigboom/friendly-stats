/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.person.Person;
import lombok.experimental.StandardException;

/**
 * An {@link Exception} when trying to perform an action on a {@link Person} for a {@link Club} that
 * the {@link Person} is not a member of.
 *
 * @fs.httpStatus 400 Bad Request
 */
@StandardException
public class PersonIsNotMemberException extends Exception {}
