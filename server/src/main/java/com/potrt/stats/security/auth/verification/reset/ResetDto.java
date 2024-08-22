/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.verification.reset;

import com.potrt.stats.security.auth.AuthExists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetDto {
  private String email;

  private String token;

  @AuthExists private String authType;

  private String code;
}
