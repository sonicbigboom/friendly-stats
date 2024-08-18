package com.potrt.stats.security.auth.key;

import java.util.Objects;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import com.potrt.stats.entities.Person;

public class AuthKeyAuthentication extends AbstractAuthenticationToken {
  private transient String apiKey;
  private final transient Person person;

  public AuthKeyAuthentication(String apiKey, Person person) {
    super(AuthorityUtils.NO_AUTHORITIES);
    this.apiKey = apiKey;
    this.person = person;
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }

  @Override
  public Object getCredentials() {
    return apiKey;
  }

  @Override
  public Object getPrincipal() {
    return person;
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    apiKey = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this.getClass() != obj.getClass()) {
      return false;
    }

    AuthKeyAuthentication authKey = (AuthKeyAuthentication) obj;
    
    return this.apiKey.equals(authKey.apiKey) && this.person.equals(authKey.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apiKey, person);
  }
}
