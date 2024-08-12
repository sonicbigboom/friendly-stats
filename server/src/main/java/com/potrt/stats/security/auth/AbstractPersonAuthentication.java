/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import com.potrt.stats.entities.Person;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public abstract class AbstractPersonAuthentication implements Authentication, CredentialsContainer {

  private transient Person person;

  /** Creates an unauthenticated {@link AbstractPersonAuthentication}. */
  protected AbstractPersonAuthentication() {
    this.person = null;
  }

  /**
   * Creates an authenticated {@link AbstractPersonAuthentication} with the authenticated {@link
   * Person}.
   *
   * @param person The authenticated {@link Person}.
   */
  protected AbstractPersonAuthentication(Person person) {
    this.person = person;
  }

  @Override
  public String getName() {
    if (person == null) {
      return null;
    }

    return person.getUsername();
  }

  @Override
  public Person getPrincipal() {
    return person;
  }

  @Override
  public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.NO_AUTHORITIES;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public boolean isAuthenticated() {
    return person != null;
  }

  @Override
  public void setAuthenticated(boolean authenticated) {
    if (!authenticated) {
      this.person = null;
    }
  }
}
