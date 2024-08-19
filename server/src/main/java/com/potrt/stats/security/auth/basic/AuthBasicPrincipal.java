/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.basic;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.PersonPrincipal;
import java.util.Collection;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthBasicPrincipal implements UserDetails, CredentialsContainer, PersonPrincipal {
  private transient AuthBasic authBasic;

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
}
