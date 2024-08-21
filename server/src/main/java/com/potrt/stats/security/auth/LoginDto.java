/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
  private String loginName;

  @AuthExists private String authType;

  private String code;
}