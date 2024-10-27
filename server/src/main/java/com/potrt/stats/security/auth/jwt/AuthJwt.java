/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.jwt;

import com.potrt.stats.entities.Person;
import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class AuthJwt extends AbstractAuthenticationToken {
  private transient String jwtToken;
  private final transient Person person;

  public AuthJwt(String jwtToken, Person person) {
    super(AuthorityUtils.NO_AUTHORITIES);
    this.jwtToken = jwtToken;
    this.person = person;
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }

  @Override
  public Object getCredentials() {
    return jwtToken;
  }

  @Override
  public Object getPrincipal() {
    return person;
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    jwtToken = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this.getClass() != obj.getClass()) {
      return false;
    }

    AuthJwt authJwt = (AuthJwt) obj;

    return this.jwtToken.equals(authJwt.jwtToken) && this.person.equals(authJwt.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwtToken, person);
  }
}
