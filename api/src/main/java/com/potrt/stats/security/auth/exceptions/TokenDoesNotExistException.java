/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.exceptions;

import javax.naming.AuthenticationException;
import lombok.experimental.StandardException;

/** An {@link AuthenticationException} for trying to use a non-existant token. */
@StandardException
public class TokenDoesNotExistException extends AuthenticationException {}
