/* Copyright (c) 2024 */
package com.potrt.stats.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link LoginDto} represents login credentials. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
  private String loginName;

  @AuthExists private String authType;

  private String code;
}
