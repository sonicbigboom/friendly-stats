/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.basic;

import com.potrt.stats.data.person.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;

/** An {@AuthBasic} represent SQL view with a {@link Person}'s username and password credentials. */
@Entity
@Data
@NoArgsConstructor
public class AuthBasic implements CredentialsContainer {
  private String email;
  private String username;
  private String password;
  @Id private Integer personId;

  @Transient private Person person;

  public AuthBasic(String email, String username, String password, Integer personId) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.personId = personId;
  }

  @Override
  public void eraseCredentials() {
    password = null;
  }
}
