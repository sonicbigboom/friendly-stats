/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.groups.id.users;

import com.potrt.stats.data.membership.Membership.MaskedMembership;
import com.potrt.stats.data.membership.MembershipService;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonAlreadyExistsException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Creates an endpoint for viewing and adding users to a group. */
@RestController
public class GroupsIdUsersEndpoint {

  private MembershipService membershipService;

  /** Autowires a {@link GroupsIdUsersEndpoint}. */
  @Autowired
  public GroupsIdUsersEndpoint(MembershipService membershipService) {
    this.membershipService = membershipService;
  }

  /**
   * The {@code /groups/{groupID}/users} {@code GET} endpoint returns all of the users that are a
   * member of this group.
   */
  @GetMapping("/groups/{groupID}/users")
  public ResponseEntity<List<MaskedMembership>> getGroupUsers(
      @PathVariable(value = "groupID") String groupID) {
    try {
      List<MaskedMembership> maskedMemberships =
          membershipService.getMemberships(Integer.valueOf(groupID));

      if (maskedMemberships.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(maskedMemberships, HttpStatus.OK);
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
      @PathVariable(value = "groupID") String groupID, @RequestBody MembershipDto membership) {
    try {
      membershipService.addMember(Integer.valueOf(groupID), membership);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (NumberFormatException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (NoResourceException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (PersonAlreadyExistsException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }
}
