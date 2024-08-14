/* Copywrite (c) 2024 */
package com.potrt.stats.security;

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
public class PersonDto {
  @Email @NotNull @NotEmpty private String email;

  @NotNull @NotEmpty private String username;

  private String firstName;

  private String lastName;

  private String nickname;

  public Person toPerson() {
    if (firstName.isBlank()) {
      firstName = null;
    }
    if (lastName.isBlank()) {
      lastName = null;
    }
    if (nickname.isBlank()) {
      nickname = null;
    }

    return new Person(null, email, username, firstName, lastName, nickname);
  }
}
