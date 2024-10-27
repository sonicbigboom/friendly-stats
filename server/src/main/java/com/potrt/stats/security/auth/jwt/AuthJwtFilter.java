/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.jwt;

import com.potrt.stats.entities.Person;
import com.potrt.stats.exceptions.ImpossibleRuntimeException;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.services.PersonService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A {@link AuthJwtFilter} authenticates request with a valid Jwt bearer token in the Authorization
 * header.
 */
@Component
public class AuthJwtFilter extends OncePerRequestFilter {

  private JwtTokenService jwtTokenProvider;

  private PersonService personService;

  /** Autowires a {@link AuthJwtFilter} */
  @Autowired
  public AuthJwtFilter(JwtTokenService jwtTokenProvider, PersonService personService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.personService = personService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String token = getTokenFromRequest(request);

    if (!StringUtils.hasText(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      jwtTokenProvider.validateToken(token);
    } catch (JwtException e) {
      filterChain.doFilter(request, response);
      return;
    }

    Integer id = jwtTokenProvider.getId(token);
    Person person;
    try {
      person = personService.getPersonWithoutAuthorization(id);
    } catch (PersonDoesNotExistException e) {
      throw new ImpossibleRuntimeException(e);
    }

    AuthService.checkAccountStatus(person);

    AuthJwt authenticationToken = new AuthJwt(token, person);
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    filterChain.doFilter(request, response);
  }

  /**
   * Get the token from the bearer token.
   *
   * @param request The http request.
   * @return The token if the request had a bearer token. Otherwise, null.
   */
  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }

    return null;
  }
}
