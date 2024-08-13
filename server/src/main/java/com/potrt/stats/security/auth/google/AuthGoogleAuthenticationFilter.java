/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.google;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class AuthGoogleAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final String CODE_PARAMETER = "code";
  private static final String PATH = "/auth/google/grantcode";

  public AuthGoogleAuthenticationFilter() {
    super(PATH);
  }

  @Autowired
  public AuthGoogleAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(PATH, authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {

    String code = request.getParameter(CODE_PARAMETER);
    if (code == null) {
      throw new BadCredentialsException("Google code not found.");
    }

    AuthGoogleAuthenticationToken authRequest = new AuthGoogleAuthenticationToken(code);

    return this.getAuthenticationManager().authenticate(authRequest);
  }
}
