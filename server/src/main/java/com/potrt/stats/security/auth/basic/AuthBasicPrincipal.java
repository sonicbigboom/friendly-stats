/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.basic;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.PersonPrincipal;
import java.util.Collection;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * An {@link AuthBasicPrincipal} represents a {@link UserDetails} implementation that is also a
 * {@link PersonPrincipal}.
 */
public class AuthBasicPrincipal implements UserDetails, PersonPrincipal, CredentialsContainer {
  private transient AuthBasic authBasic;

  /**
   * Creates a new {@link AuthBasicPrincipal} with the {@link Person}'s current {@link AuthBasic}
   * credentials.
   *
   * @param authBasic The {@link Person}'s current {@link AuthBasic} credentials.
   */
  public AuthBasicPrincipal(AuthBasic authBasic) {
    this.authBasic = authBasic;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.NO_AUTHORITIES;
  }

  @Override
  public String getPassword() {
    return authBasic.getPassword();
  }

  @Override
  public String getUsername() {
    return authBasic.getUsername();
  }

  @Override
  public void eraseCredentials() {
    authBasic.eraseCredentials();
  }

  @Override
  public Person getPerson() {
    return authBasic.getPerson();
  }

  @Override
  public boolean isEnabled() {
    Person person = getPerson();
    return !person.isDeleted() && !person.isDisabled();
  }
}
