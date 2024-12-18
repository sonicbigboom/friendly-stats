/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.auth.reset;

import com.potrt.stats.data.person.Person;
import com.potrt.stats.data.person.PersonService;
import com.potrt.stats.exceptions.BadExternalCommunicationException;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.AuthType;
import com.potrt.stats.security.auth.exceptions.TokenDoesNotExistException;
import com.potrt.stats.security.auth.exceptions.TokenExpiredException;
import com.potrt.stats.security.auth.verification.reset.ResetDto;
import com.potrt.stats.security.auth.verification.reset.ResetService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/** Creates an endpoint for resetting an accounts credentials. */
@Controller
public class AuthResetEndpoint {
  private ResetService resetService;
  private PersonService personService;

  /** Autowires a {@link AuthResetEndpoint}. */
  public AuthResetEndpoint(ResetService resetService, PersonService personService) {
    this.resetService = resetService;
    this.personService = personService;
  }

  /**
   * The {@code /auth/reset} {@code GET} endpoint receives an email and send an email to with a
   * token to reset the user's credentials.
   */
  @GetMapping("/auth/reset")
  @Transactional
  public ResponseEntity<Void> sendResetToken(@RequestParam String email) {
    try {
      Person person = personService.getPersonWithoutAuthCheck(email);
      resetService.sendResetEmail(person);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (PersonDoesNotExistException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (BadExternalCommunicationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * The {@code /auth/reset} {@code POST} endpoint receives a reset token and new user credentials
   * to reset the user's credentials.
   */
  @PostMapping("/auth/reset")
  @Transactional
  public ResponseEntity<Void> resetCredentials(@RequestBody @Valid ResetDto resetDto) {
    try {
      Person person = resetService.checkToken(resetDto.getEmail(), resetDto.getToken());
      AuthService service = AuthType.getAuthService(resetDto.getAuthType());
      service.updateCredentials(person.getId(), resetDto.getCode());
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (ValidationException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (PersonDoesNotExistException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (TokenDoesNotExistException | TokenExpiredException e) {
      return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    } catch (BadExternalCommunicationException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
