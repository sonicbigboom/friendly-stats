/* Copywrite (c) 2024 */
package com.potrt.stats.api.groups.id.users.add;

import com.potrt.stats.entities.Person;
import com.potrt.stats.entities.Person.MaskedPerson;
import com.potrt.stats.exceptions.AlreadyExistsException;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupsIdUsersAddApi {

  private ClubService clubService;

  @Autowired
  public GroupsIdUsersAddApi(ClubService clubService) {
    this.clubService = clubService;
  }

  @PostMapping("/groups/{groupID}/users/add")
  public ResponseEntity<Person> addNewGroupUser(
      @PathVariable(value = "groupID") String groupID, @RequestBody MaskedPerson maskedPerson) {
    try {
      Person person = clubService.addPerson(Integer.valueOf(groupID), maskedPerson);
      return new ResponseEntity<>(person, HttpStatus.CREATED);
    } catch (NumberFormatException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (NoResourceException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (AlreadyExistsException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }
}
