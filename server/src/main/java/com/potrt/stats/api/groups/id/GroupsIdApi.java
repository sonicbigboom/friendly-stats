/* Copywrite (c) 2024 */
package com.potrt.stats.api.groups.id;

import com.potrt.stats.entities.Club.MaskedClub;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupsIdApi {

  private ClubService clubService;

  @Autowired
  public GroupsIdApi(ClubService clubService) {
    this.clubService = clubService;
  }

  @GetMapping("/groups/{groupID}")
  public ResponseEntity<MaskedClub> getGroupId(@PathVariable(value = "groupID") String id) {
    try {
      MaskedClub club = clubService.getClub(Integer.valueOf(id));
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
}
