/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.auth.login;

import com.potrt.stats.exceptions.BadExternalCommunicationException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.AuthType;
import com.potrt.stats.security.auth.LoginDto;
import com.potrt.stats.security.auth.jwt.AuthJwtResponse;
import com.potrt.stats.security.auth.jwt.JwtTokenService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/** Creates an endpoint for logging in and getting a JWT authentication token. */
@Controller
public class AuthLoginEndpoint {

  private JwtTokenService jwtTokenService;

  /** Autowires the {@link AuthLoginEndpoint}. */
  @Autowired
  public AuthLoginEndpoint(JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  /**
   * The {@code /auth/login} {@code POST} endpoint receives user credentials and returns a JWT
   * token.
   */
  @PostMapping("/auth/login")
  public ResponseEntity<AuthJwtResponse> registerUserAccount(
      @RequestBody @Valid LoginDto loginDto) {

    try {
      AuthService authService = AuthType.getAuthService(loginDto.getAuthType());

      Authentication authentication = authService.login(loginDto);
      String token = jwtTokenService.generateToken(authentication);

      AuthJwtResponse jwtAuthResponse = new AuthJwtResponse();
      jwtAuthResponse.setAccessToken(token);

      return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    } catch (ValidationException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (BadCredentialsException | UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (BadExternalCommunicationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
