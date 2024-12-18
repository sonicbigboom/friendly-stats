/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.groups;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.club.ClubResponse;
import com.potrt.stats.data.club.ClubService;
import com.potrt.stats.exceptions.UnauthenticatedException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Creates an endpoint for viewing and creating groups. */
@RestController
public class GroupsEndpoint {

  private ClubService clubService;

  /** Autowires a {@link GroupsEndpoint}. */
  public GroupsEndpoint(ClubService clubService) {
    this.clubService = clubService;
  }

  /**
   * The {@code /groups} {@code GET} endpoint returns all of the groups that the caller is a member
   * of.
   */
  @GetMapping("/groups")
  public ResponseEntity<List<ClubResponse>> getGroups() {
    try {
      List<Club> clubs = clubService.getClubs();

      if (clubs.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      List<ClubResponse> responses = new ArrayList<>();
      for (Club club : clubs) {
        responses.add(new ClubResponse(club));
      }

      return new ResponseEntity<>(responses, HttpStatus.OK);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  /** The {@code /groups} {@code POST} endpoint creates a new group. */
  @PostMapping("/groups")
  public ResponseEntity<ClubResponse> createGroup(
      @RequestParam(value = "name") String name, HttpServletRequest request) {
    try {
      return new ResponseEntity<>(
          new ClubResponse(clubService.createClub(name)), HttpStatus.CREATED);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
