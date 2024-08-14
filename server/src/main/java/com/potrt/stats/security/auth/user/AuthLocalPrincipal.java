/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.user;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.PersonPrincipal;
import java.util.Collection;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthLocalPrincipal implements UserDetails, CredentialsContainer, PersonPrincipal {
  private transient AuthLocal authLocal;

  public AuthLocalPrincipal(AuthLocal authLocal) {
    this.authLocal = authLocal;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.NO_AUTHORITIES;
  }

  @Override
  public String getPassword() {
    return authLocal.getPassword();
  }

  @Override
  public String getUsername() {
    return authLocal.getUsername();
  }

  @Override
  public void eraseCredentials() {
    authLocal.eraseCredentials();
  }

  @Override
  public Person getPerson() {
    return authLocal.getPerson();
  }
}
