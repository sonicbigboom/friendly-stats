/* Copywrite (c) 2024 */
package com.potrt.stats.api.auth.google.grantcode;

import com.potrt.stats.entities.Person;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.security.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthGoogleGrantcodeApi {

  private SecurityService securityService;

  public AuthGoogleGrantcodeApi(SecurityService securityService) {
    this.securityService = securityService;
  }

  @GetMapping("/auth/google/grantcode")
  public ResponseEntity<Person> getMe() {
    try {
      Person person = securityService.getPerson();
      return new ResponseEntity<>(person, HttpStatus.OK);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
