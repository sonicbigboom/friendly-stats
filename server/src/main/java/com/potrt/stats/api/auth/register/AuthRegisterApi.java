/* Copywrite (c) 2024 */
package com.potrt.stats.api.auth.register;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.AuthType;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import com.potrt.stats.security.auth.verification.VerificationService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthRegisterApi {

  private ApplicationContext applicationContext;
  private VerificationService verificationService;

  @Autowired
  public AuthRegisterApi(
      ApplicationContext applicationContext, VerificationService verificationService) {
    this.applicationContext = applicationContext;
    this.verificationService = verificationService;
  }

  @PostMapping("/auth/register")
  @Transactional
  public ResponseEntity<Void> registerUserAccount(
      @RequestBody @Valid RegisterDto registerDto,
      @RequestParam(value = "verificationUrl") String verificationUrl,
      HttpServletRequest request) {

    try {
      AuthService service = AuthType.getAuthService(applicationContext, registerDto.getAuthType());
      Person person = service.registerPerson(registerDto);
      verificationService.sendVerificationEmail(person, verificationUrl);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (ValidationException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    } catch (MessagingException | UnsupportedEncodingException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
