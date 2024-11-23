/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.users;

import com.potrt.stats.data.person.Person.MaskedPerson;
import com.potrt.stats.data.person.PersonService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Creates an endpoint for finding users. */
@RestController
public class UsersEndpoint {

  private PersonService personService;

  /** Autowires a {@link UsersEndpoint}. */
  public UsersEndpoint(PersonService personService) {
    this.personService = personService;
  }

  /** The {@code /users} {@code GET} endpoint returns all of the users matching the filter. */
  @GetMapping("/users")
  public ResponseEntity<List<MaskedPerson>> getUsers(
      @RequestParam(value = "filter") Optional<String> filter) {
    List<MaskedPerson> persons = personService.getPersons(filter.orElseGet(() -> null));

    if (persons.isEmpty()) {
      return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(persons, HttpStatus.OK);
  }
}
