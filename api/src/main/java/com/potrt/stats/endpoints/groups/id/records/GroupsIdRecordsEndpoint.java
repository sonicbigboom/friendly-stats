/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.groups.id.records;

import com.potrt.stats.data.gamerecord.expanded.GameRecordExpanded;
import com.potrt.stats.data.gamerecord.expanded.GameRecordExpandedResponse;
import com.potrt.stats.data.gamerecord.expanded.GameRecordExpandedService;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import java.util.ArrayList;
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
   * The <code>/groups/{groupId}/records</code> {@code GET} endpoint returns the records for the
   * target games.
   */
  @GetMapping("/groups/{groupId}/records")
  public ResponseEntity<List<GameRecordExpandedResponse>> getGameRecords(
      @PathVariable(value = "groupId") String groupId,
      @RequestParam(value = "gameTypeId") Optional<String> gameTypeId,
      @RequestParam(value = "forCash") Optional<String> forCash,
      @RequestParam(value = "seasonId") Optional<String> seasonId,
      @RequestParam(value = "userId") Optional<String> userId) {
    try {
      List<GameRecordExpanded> gameRecordExpandeds =
          gameRecordExpandedService.getGameRecordExpandeds(
              Integer.valueOf(groupId), gameTypeId, forCash, seasonId, userId);

      if (gameRecordExpandeds.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      List<GameRecordExpandedResponse> responses = new ArrayList<>();
      for (GameRecordExpanded gameRecordExpanded : gameRecordExpandeds) {
        responses.add(new GameRecordExpandedResponse(gameRecordExpanded));
      }

      return new ResponseEntity<>(responses, HttpStatus.OK);
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
