/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.user;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.google.AuthGoogle;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;

/**
 * A {@link AuthGoogle} represents the connection from a Google account to a application person
 * account.
 */
@Entity
@Data
@NoArgsConstructor
public class AuthLocal implements CredentialsContainer {
  @Id private String email;
  private String password;
  private Integer personID;

  public AuthLocal(String email, String password, Integer personID) {
    this.email = email;
    this.password = password;
    this.personID = personID;
  }

  @Transient private Person person;

  @Override
  public void eraseCredentials() {
    password = null;
  }
}
