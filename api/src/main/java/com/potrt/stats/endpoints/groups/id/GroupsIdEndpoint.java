/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.groups.id;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.club.ClubResponse;
import com.potrt.stats.data.club.ClubService;
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
public class GroupsIdEndpoint {

  private ClubService clubService;

  /** Autowires a {@link GroupsIdEndpoint}. */
  @Autowired
  public GroupsIdEndpoint(ClubService clubService) {
    this.clubService = clubService;
  }

  /** The {@code /groups/{groupId}} {@code GET} endpoint returns details of the target group. */
  @GetMapping("/groups/{groupId}")
  public ResponseEntity<ClubResponse> getGroup(@PathVariable(value = "groupId") String groupId) {
    try {
      Club club = clubService.getClub(Integer.valueOf(groupId));

      return new ResponseEntity<>(new ClubResponse(club), HttpStatus.OK);
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
   * TODO: The {@code /groups/{groupId}} {@code PATCH} endpoint updates the calling details and
   * returns the updated caller's details.
   */
  @PatchMapping("/groups/{groupId}")
  public ResponseEntity<Void> patchGroup(
      @PathVariable(value = "groupId") String groupId, @RequestBody ClubResponse club) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /** TODO: The {@code /groups/{groupId}} {@code DELETE} endpoint deletes the caller's account. */
  @DeleteMapping("/groups/{groupId}")
  public ResponseEntity<Void> deleteGroup(@PathVariable(value = "groupId") String groupId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
