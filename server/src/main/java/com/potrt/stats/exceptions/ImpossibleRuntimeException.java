/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a {@link RuntimeException} that should not be possible, if it occurs there is a major
 * logical error.
 */
public class ImpossibleRuntimeException extends RuntimeException {

  public static final Logger logger = LogManager.getLogger(ImpossibleRuntimeException.class);

  public ImpossibleRuntimeException() {
    super();
    logger.error("ImpossibleRuntimeException thrown!");
  }

  public ImpossibleRuntimeException(Throwable cause) {
    super(cause);
    logger.error("ImpossibleRuntimeException thrown!", cause);
  }

  public ImpossibleRuntimeException(String msg) {
    super(msg);
    logger.error("ImpossibleRuntimeException thrown: %s", msg);
  }

  public ImpossibleRuntimeException(String msg, Throwable cause) {
    super(msg, cause);
    logger.error("ImpossibleRuntimeException thrown: %s, %e", msg, cause);
  }
}
