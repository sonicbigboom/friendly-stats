/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.basic;

import com.potrt.stats.entities.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;

@Entity
@Data
@NoArgsConstructor
public class AuthBasic implements CredentialsContainer {
  private String email;
  private String username;
  private String password;
  @Id private Integer personID;

  @Transient private Person person;

  public AuthBasic(String email, String username, String password, Integer personID) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.personID = personID;
  }

  @Override
  public void eraseCredentials() {
    password = null;
  }
}
