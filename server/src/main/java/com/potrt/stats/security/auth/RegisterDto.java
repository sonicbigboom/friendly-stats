/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import com.potrt.stats.entities.Person;
import jakarta.validation.constraints.Email;
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
public class RegisterDto {
  @Email @NotNull @NotEmpty private String email;

  @NotNull @NotEmpty private String username;

  private String firstName;

  private String lastName;

  private String nickname;

  @AuthExists private String authType;

  private String code;

  public Person getPerson() {
    if (firstName.isBlank()) {
      firstName = null;
    }
    if (lastName.isBlank()) {
      lastName = null;
    }
    if (nickname.isBlank()) {
      nickname = null;
    }

    // TODO: Disable account on creation, and use verification email.
    return new Person(null, email, username, firstName, lastName, nickname, false, false);
  }
}
