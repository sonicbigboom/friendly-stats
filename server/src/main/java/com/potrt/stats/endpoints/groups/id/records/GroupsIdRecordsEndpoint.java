/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.groups.id.records;

import com.potrt.stats.data.gamerecord.expanded.GameRecordExpanded;
import com.potrt.stats.data.gamerecord.expanded.GameRecordExpandedService;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Creates an endpoint for viewing and adding games. */
@RestController
public class GroupsIdRecordsEndpoint {

  private GameRecordExpandedService gameRecordExpandedService;

  /** Autowires a {@link GroupsIdRecordsEndpoint}. */
  @Autowired
  public GroupsIdRecordsEndpoint(GameRecordExpandedService gameRecordExpandedService) {
    this.gameRecordExpandedService = gameRecordExpandedService;
  }

  /**
   * The <code>/groups/{groupID}/records</code> {@code GET} endpoint returns the records for the
   * target games.
   */
  @GetMapping("/groups/{groupID}/records")
  public ResponseEntity<List<GameRecordExpanded>> getGameRecords(
      @PathVariable(value = "groupID") String groupID,
      @RequestParam(value = "gameTypeID") Optional<String> gameTypeID,
      @RequestParam(value = "forCash") Optional<String> forCash,
      @RequestParam(value = "seasonID") Optional<String> seasonID) {
    try {
      List<GameRecordExpanded> gameRecordExpandeds =
          gameRecordExpandedService.getGameRecordExpandeds(
              Integer.valueOf(groupID), gameTypeID, forCash, seasonID);

      if (gameRecordExpandeds.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(gameRecordExpandeds, HttpStatus.OK);
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
