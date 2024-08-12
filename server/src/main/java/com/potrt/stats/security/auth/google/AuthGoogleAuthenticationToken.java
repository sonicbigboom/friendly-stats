/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.google;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.AbstractPersonAuthentication;

public class AuthGoogleAuthenticationToken extends AbstractPersonAuthentication {

  private String code;

  /**
   * This constructor can be safely used by any code that wishes to create a {@link
   * AuthGoogleAuthenticationToken} with credentials that can be checked.
   *
   * @param code A Google oAuth code.
   */
  public AuthGoogleAuthenticationToken(String code) {
    super();
    this.code = code;
  }

  /**
   * This constructor should only be used by <code>AuthenticationManager</code> or <code>
   * AuthenticationProvider</code> implementations that are satisfied with producing a trusted (i.e.
   * {@link #isAuthenticated()} = <code>true</code>) authentication token.
   *
   * @param person The {@link Person} that has been authenticated.
   */
  public AuthGoogleAuthenticationToken(Person person) {
    super(person);
  }

  @Override
  public String getCredentials() {
    return code;
  }

  @Override
  public void eraseCredentials() {
    code = null;
  }
}
