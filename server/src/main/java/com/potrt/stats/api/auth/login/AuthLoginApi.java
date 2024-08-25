/* Copywrite (c) 2024 */
package com.potrt.stats.api.auth.login;

import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.AuthType;
import com.potrt.stats.security.auth.LoginDto;
import com.potrt.stats.security.auth.jwt.AuthJwtResponse;
import com.potrt.stats.security.auth.jwt.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
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

@Controller
public class AuthLoginApi {

  private JwtTokenService jwtTokenProvider;

  @Autowired
  public AuthLoginApi(JwtTokenService jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @PostMapping("/auth/login")
  public ResponseEntity<AuthJwtResponse> registerUserAccount(
      @RequestBody @Valid LoginDto loginDto, HttpServletRequest request) {

    try {
      AuthService authService = AuthType.getAuthService(loginDto.getAuthType());

      Authentication authentication = authService.login(loginDto);
      String token = jwtTokenProvider.generateToken(authentication);

      AuthJwtResponse jwtAuthResponse = new AuthJwtResponse();
      jwtAuthResponse.setAccessToken(token);

      return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    } catch (ValidationException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (BadCredentialsException | UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
