/* Copyright (c) 2024 */
package com.potrt.stats.api.groups.id.users.id;

import com.potrt.stats.entities.Membership;
import com.potrt.stats.services.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Creates an endpoint for viewing a users details within a group. */
@RestController
public class GroupsIdUsersIdApi {

  private MembershipService membershipService;

  /** Autowires a {@link GroupsIdUsersIdApi}. */
  @Autowired
  public GroupsIdUsersIdApi(MembershipService membershipService) {
    this.membershipService = membershipService;
  }

  /**
   * TODO: The {@code /groups/{groupID}/users/{userID}} {@code GET} endpoint returns user details
   * for a group.
   */
  @GetMapping("/groups/{groupID}/users/{userID}")
  public ResponseEntity<Membership> getMembership(
      @PathVariable(value = "groupID") String groupID,
      @PathVariable(value = "userID") String userID) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * TODO: The {@code /groups/{groupID}/users/{userID}} {@code POST} endpoint adds a user to a
   * group.
   */
  @PostMapping("/groups/{groupID}/users/{userID}")
  public ResponseEntity<Void> createMembership(
      @PathVariable(value = "groupID") String groupID,
      @PathVariable(value = "userID") String userID) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * TODO: The {@code /groups/{groupID}/users/{userID}} {@code PATCH} endpoint updates a user's
   * permission within a group.
   */
  @PatchMapping("/groups/{groupID}/users/{userID}")
  public ResponseEntity<Void> updateMembership(
      @PathVariable(value = "groupID") String groupID,
      @PathVariable(value = "userID") String userID,
      @RequestBody Membership membership) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
