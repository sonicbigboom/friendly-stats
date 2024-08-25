/* Copywrite (c) 2024 */
package com.potrt.stats.api.users;

import com.potrt.stats.entities.Person.MaskedPerson;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.services.PersonService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersApi {

  private PersonService personService;

  public UsersApi(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping("/users")
  public ResponseEntity<List<MaskedPerson>> getUsers(
      @RequestParam(value = "filter") Optional<String> filter) {
    try {
      List<MaskedPerson> persons = personService.getPersons(filter);

      if (persons.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(persons, HttpStatus.OK);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
