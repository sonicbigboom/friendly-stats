/* Copywrite (c) 2024 */
package com.potrt.stats.api.auth.verify;

import com.potrt.stats.security.auth.exceptions.TokenDoesNotExistException;
import com.potrt.stats.security.auth.exceptions.TokenExpiredException;
import com.potrt.stats.security.auth.verification.VerificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthVerifyApi {
  private VerificationService verificationService;

  public AuthVerifyApi(VerificationService verificationService) {
    this.verificationService = verificationService;
  }

  @GetMapping("/auth/verify")
  public ResponseEntity<String> registerUserAccount(
      @RequestParam("token") String token, HttpServletRequest request) {

    try {
      verificationService.verify(token);
      return new ResponseEntity<>("Successfully verified!", HttpStatus.OK);
    } catch (TokenDoesNotExistException | TokenExpiredException e) {
      return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }
}
