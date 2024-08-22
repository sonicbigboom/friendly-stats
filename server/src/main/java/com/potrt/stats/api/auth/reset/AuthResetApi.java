/* Copywrite (c) 2024 */
package com.potrt.stats.api.auth.reset;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.AuthType;
import com.potrt.stats.security.auth.exceptions.PersonDoesNotExistException;
import com.potrt.stats.security.auth.exceptions.VerificationDoesNotExistException;
import com.potrt.stats.security.auth.exceptions.VerificationExpiredException;
import com.potrt.stats.security.auth.verification.reset.ResetDto;
import com.potrt.stats.security.auth.verification.reset.ResetService;
import com.potrt.stats.services.PersonService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthResetApi {
  private ApplicationContext applicationContext;
  private ResetService resetService;
  private PersonService personService;

  public AuthResetApi(
      ApplicationContext applicationContext,
      ResetService resetService,
      PersonService personService) {
    this.applicationContext = applicationContext;
    this.resetService = resetService;
    this.personService = personService;
  }

  @GetMapping("/auth/reset")
  @Transactional
  public ResponseEntity<String> sendResetToken(
      @RequestParam String email, HttpServletRequest request) {
    try {
      Person person = personService.getPerson(email);
      resetService.sendResetEmail(person);
      return new ResponseEntity<>("Sent reset code!", HttpStatus.OK);
    } catch (PersonDoesNotExistException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (UnsupportedEncodingException | MessagingException e) {
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
  }

  @PostMapping("/auth/reset")
  @Transactional
  public ResponseEntity<Void> resetCredentials(
      @RequestBody @Valid ResetDto resetDto, HttpServletRequest request) {
    try {
      Person person = resetService.checkToken(resetDto.getEmail(), resetDto.getToken());
      AuthService service = AuthType.getAuthService(applicationContext, resetDto.getAuthType());
      service.setAuthentication(person.getId(), resetDto.getCode());
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (PersonDoesNotExistException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (VerificationDoesNotExistException | VerificationExpiredException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }
}
