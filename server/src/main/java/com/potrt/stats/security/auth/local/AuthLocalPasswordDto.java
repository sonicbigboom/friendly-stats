/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.local;

import com.potrt.stats.security.auth.local.validation.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class AuthLocalPasswordDto {
  private String password;
  private String passwordConfirm;

  public AuthLocalPassword toAuthLocalPassword(Integer id) {
    return new AuthLocalPassword(id, "{bcrypt}" + new BCryptPasswordEncoder(14).encode(password));
  }
}
