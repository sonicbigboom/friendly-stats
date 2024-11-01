/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.auth.register;

import com.potrt.stats.data.person.Person;
import com.potrt.stats.exceptions.BadExternalCommunicationException;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.AuthType;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import com.potrt.stats.security.auth.verification.VerificationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/** Creates an endpoint for registering a new account. */
@Controller
public class AuthRegisterEndpoint {

  private VerificationService verificationService;

  /** Autowires a {@link AuthRegisterEndpoint} */
  @Autowired
  public AuthRegisterEndpoint(VerificationService verificationService) {
    this.verificationService = verificationService;
  }

  /**
   * The {@code /auth/register} {@code POST} endpoint receives user details and credentials to
   * create a new account.
   */
  @PostMapping("/auth/register")
  @Transactional
  public ResponseEntity<Void> registerUserAccount(
      @RequestBody @Valid RegisterDto registerDto,
      @RequestParam(required = false, value = "verificationUrl") String verificationUrl) {

    try {
      AuthService service = AuthType.getAuthService(registerDto.getAuthType());
      Person person = service.register(registerDto);
      verificationService.sendVerificationEmail(person, verificationUrl);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (ValidationException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    } catch (BadExternalCommunicationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
