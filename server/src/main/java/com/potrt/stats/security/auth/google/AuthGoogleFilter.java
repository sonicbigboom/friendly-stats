/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.google;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class AuthGoogleFilter extends GenericFilterBean {

  AuthGoogleService authGoogleService;

  @Autowired
  public AuthGoogleFilter(AuthGoogleService authGoogleService) {
    this.authGoogleService = authGoogleService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    try {
      Authentication authentication =
          authGoogleService.getAuthentication((HttpServletRequest) request);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (BadCredentialsException e) {
      logger.debug("No api key: ", e);
    }
    filterChain.doFilter(request, response);
  }
}
