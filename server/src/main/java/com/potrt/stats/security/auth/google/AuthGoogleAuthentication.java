/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.google;

import com.potrt.stats.entities.Person;
import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class AuthGoogleAuthentication extends AbstractAuthenticationToken {
  private transient String accessToken;
  private final transient Person person;

  public AuthGoogleAuthentication(String accessToken, Person person) {
    super(AuthorityUtils.NO_AUTHORITIES);
    this.accessToken = accessToken;
    this.person = person;
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }

  @Override
  public Object getCredentials() {
    return accessToken;
  }

  @Override
  public Object getPrincipal() {
    return person;
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    accessToken = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this.getClass() != obj.getClass()) {
      return false;
    }

    AuthGoogleAuthentication authGoogle = (AuthGoogleAuthentication) obj;

    return this.accessToken.equals(authGoogle.accessToken) && this.person.equals(authGoogle.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, person);
  }
}
