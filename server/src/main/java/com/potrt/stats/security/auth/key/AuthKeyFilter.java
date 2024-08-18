package com.potrt.stats.security.auth.key;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class AuthKeyFilter extends GenericFilterBean {

  AuthKeyService authKeyService;

  @Autowired
  public AuthKeyFilter(AuthKeyService authKeyService) {
    this.authKeyService = authKeyService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    try {
      Authentication authentication = authKeyService.getAuthentication((HttpServletRequest) request);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (BadCredentialsException e) {
      logger.debug("No api key: ", e);
    }
    filterChain.doFilter(request, response);
  }
}
