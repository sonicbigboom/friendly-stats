/* Copywrite (c) 2024 */
package com.potrt.stats.api.groups.id.users;

import com.potrt.stats.entities.Person.MaskedPerson;
import com.potrt.stats.exceptions.AlreadyExistsException;
import com.potrt.stats.exceptions.NoContentException;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.security.auth.exceptions.PersonDoesNotExistException;
import com.potrt.stats.services.ClubService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupsIdUsersApi {

  private ClubService clubService;

  @Autowired
  public GroupsIdUsersApi(ClubService clubService) {
    this.clubService = clubService;
  }

  @GetMapping("/groups/{groupID}/users")
  public ResponseEntity<List<MaskedPerson>> getGroupUsers(
      @PathVariable(value = "groupID") String id) {
    try {
      List<MaskedPerson> maskedPersons = clubService.getPersons(Integer.valueOf(id));
      return new ResponseEntity<>(maskedPersons, HttpStatus.OK);
    } catch (NoContentException e) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (NumberFormatException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (NoResourceException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/groups/{groupID}/users")
  public ResponseEntity<Void> addGroupUser(
      @PathVariable(value = "groupID") String groupID,
      @RequestParam(value = "userID") String userID) {
    try {
      clubService.addPerson(Integer.valueOf(groupID), Integer.valueOf(userID));
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (NumberFormatException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (NoResourceException | PersonDoesNotExistException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (AlreadyExistsException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }
}
