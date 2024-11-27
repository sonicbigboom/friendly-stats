/* Copyright (c) 2024 */
package com.potrt.stats.data.person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link PersonResponse} represents a person in the application. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonResponse {
  private Integer id;
  private String email;
  private String username;
  private String firstName;
  private String lastName;
  private String nickname;
  private boolean isPrivate;

  public PersonResponse(Person person) {
    this(
        person.getId(),
        person.getEmail(),
        person.getUsername(),
        person.getFirstName(),
        person.getLastName(),
        person.getNickname(),
        person.isPrivate());
  }
}
