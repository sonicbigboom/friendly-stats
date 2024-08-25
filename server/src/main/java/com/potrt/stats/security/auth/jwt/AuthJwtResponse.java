/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthJwtResponse {
  private String accessToken;
  private String tokenType = "Bearer";
}
