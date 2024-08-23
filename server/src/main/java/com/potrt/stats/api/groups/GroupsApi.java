/* Copywrite (c) 2024 */
package com.potrt.stats.api.groups;

import com.potrt.stats.entities.Club.MaskedClub;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.services.ClubService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupsApi {

  private ClubService clubService;

  public GroupsApi(ClubService clubService) {
    this.clubService = clubService;
  }

  @GetMapping("/groups")
  public ResponseEntity<List<MaskedClub>> getGroups() {
    try {
      List<MaskedClub> clubs = clubService.getClubs();
      return new ResponseEntity<>(clubs, HttpStatus.OK);
    } catch (NoResourceException e) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/groups")
  public ResponseEntity<MaskedClub> createGroup(
      @RequestParam(value = "name") String name, HttpServletRequest request) {
    try {
      return new ResponseEntity<>(clubService.createClub(name), HttpStatus.CREATED);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
