/* Copywrite (c) 2024 */
package com.potrt.stats.api.groups.id.users;

import com.potrt.stats.entities.Person.MaskedPerson;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonAlreadyExistsException;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
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

/** Creates an endpoint for viewing and adding users to a group. */
@RestController
public class GroupsIdUsersApi {

  private ClubService clubService;

  /** Autowires a {@link GroupsIdUsersApi}. */
  @Autowired
  public GroupsIdUsersApi(ClubService clubService) {
    this.clubService = clubService;
  }

  /**
   * The {@code /groups/{groupID}/users} {@code GET} endpoint returns all of the users that are a
   * member of this group.
   */
  @GetMapping("/groups/{groupID}/users")
  public ResponseEntity<List<MaskedPerson>> getGroupUsers(
      @PathVariable(value = "groupID") String groupID) {
    try {
      List<MaskedPerson> maskedPersons = clubService.getPersons(Integer.valueOf(groupID));

      if (maskedPersons.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(maskedPersons, HttpStatus.OK);
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

  @Deprecated // TODO: This endpoint should take a new user.
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
    } catch (PersonAlreadyExistsException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }
}
