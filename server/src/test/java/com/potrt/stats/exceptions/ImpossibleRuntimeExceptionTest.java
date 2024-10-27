/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import com.potrt.stats.TestConstants;
import org.junit.jupiter.api.Test;

class ImpossibleRuntimeExceptionTest implements TestConstants {

  @Test
  void testImpossibleRuntimeException() {
    assertThat(new ImpossibleRuntimeException(TEST_ERROR_MESSAGE))
        .hasMessageContaining(TEST_ERROR_MESSAGE);
  }
}
