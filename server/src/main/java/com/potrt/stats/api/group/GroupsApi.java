/* Copywrite (c) 2024 */
package com.potrt.stats.api.group;

import com.potrt.stats.entities.Club.MaskedClub;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.services.ClubService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupsApi {

  private ClubService clubService;

  public GroupsApi(ClubService clubService) {
    this.clubService = clubService;
  }

  @GetMapping("/groups")
  public ResponseEntity<List<MaskedClub>> getGroup() {
    try {
      List<MaskedClub> clubs = clubService.getClubs();
      return new ResponseEntity<>(clubs, HttpStatus.OK);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (NoResourceException e) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  }
}
