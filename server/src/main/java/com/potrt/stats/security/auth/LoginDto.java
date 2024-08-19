/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
  @NotNull @NotEmpty private String loginName;

  @AuthExists private String authType;

  private String code;
}
