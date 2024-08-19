/* Copywrite (c) 2024 */
package com.potrt.stats.api.auth.register;

import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.AuthType;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthRegisterApi {

  ApplicationContext applicationContext;

  @Autowired
  public AuthRegisterApi(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @PostMapping("/auth/register")
  public ResponseEntity<Void> registerUserAccount(
      @RequestBody @Valid RegisterDto registerDto, HttpServletRequest request) {

    try {
      AuthService service = AuthType.getAuthService(applicationContext, registerDto.getAuthType());
      service.registerPerson(registerDto);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }
}
