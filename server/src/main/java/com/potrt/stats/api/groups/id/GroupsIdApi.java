/* Copyright (c) 2024 */
package com.potrt.stats.api.groups.id;

import com.potrt.stats.data.club.Club.MaskedClub;
import com.potrt.stats.data.club.ClubService;
import com.potrt.stats.data.person.Person.MaskedPerson;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Creates an endpoint for viewing and editing a target group. */
@RestController
public class GroupsIdApi {

  private ClubService clubService;

  /** Autowires a {@link GroupsIdApi}. */
  @Autowired
  public GroupsIdApi(ClubService clubService) {
    this.clubService = clubService;
  }

  /** The {@code /groups/{groupID}} {@code GET} endpoint returns details of the target group. */
  @GetMapping("/groups/{groupID}")
  public ResponseEntity<MaskedClub> getGroup(@PathVariable(value = "groupID") String groupID) {
    try {
      MaskedClub club = clubService.getClub(Integer.valueOf(groupID));
      return new ResponseEntity<>(club, HttpStatus.OK);
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

  /**
   * TODO: The {@code /groups/{groupID}} {@code PATCH} endpoint updates the calling details and
   * returns the updated caller's details.
   */
  @PatchMapping("/groups/{groupID}")
  public ResponseEntity<MaskedPerson> patchGroup(
      @PathVariable(value = "groupID") String groupID, @RequestBody MaskedClub club) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /** TODO: The {@code /groups/{groupID}} {@code DELETE} endpoint deletes the caller's account. */
  @DeleteMapping("/groups/{groupID}")
  public ResponseEntity<MaskedPerson> deleteGroup(@PathVariable(value = "groupID") String groupID) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
