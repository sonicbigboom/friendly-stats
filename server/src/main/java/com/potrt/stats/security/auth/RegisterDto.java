/* Copyright (c) 2024 */
package com.potrt.stats.security.auth;

import com.potrt.stats.entities.Person;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Represents a new {@link Person} and their credentials. */
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

  /**
   * Gets the {@link Person} to insert.
   *
   * @return The insertable {@link Person}.
   */
  public Person getPerson() {
    if (firstName == null || firstName.isBlank()) {
      firstName = null;
    }
    if (lastName == null || lastName.isBlank()) {
      lastName = null;
    }
    if (nickname == null || nickname.isBlank()) {
      nickname = null;
    }

    return new Person(null, email, username, firstName, lastName, nickname, true, true, false);
  }
}
