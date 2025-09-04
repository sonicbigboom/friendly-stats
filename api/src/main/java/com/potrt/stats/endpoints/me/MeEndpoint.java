/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.me;

import com.potrt.stats.data.person.Person;
import com.potrt.stats.data.person.PersonResponse;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.security.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

/** Creates an endpoint for getting/editing caller's details. */
@Controller
public class MeEndpoint {

  private SecurityService securityService;

  /** Autowires a {@link MeEndpoint}. */
  public MeEndpoint(SecurityService securityService) {
    this.securityService = securityService;
  }

  /** The {@code /auth/me} {@code GET} endpoint returns the caller's details. */
  @GetMapping("/me")
  public ResponseEntity<PersonResponse> getMe() {
    try {
      Person person = securityService.getPerson();
      return new ResponseEntity<>(new PersonResponse(person), HttpStatus.OK);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  /**
   * TODO: The {@code /me} {@code PATCH} endpoint updates the calling details and returns the
   * updated caller's details.
   */
  @PatchMapping("/me")
  public ResponseEntity<PersonResponse> patchMe(@RequestBody Person person) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /** TODO: The {@code /me} {@code DELETE} endpoint deletes the caller's account. */
  @DeleteMapping("/me")
  public ResponseEntity<Void> deleteMe() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
