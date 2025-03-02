/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.games.id.records;

import com.potrt.stats.data.gamerecord.GameRecord;
import com.potrt.stats.data.gamerecord.GameRecordResponse;
import com.potrt.stats.data.gamerecord.GameRecordService;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonIsNotPlayerException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Creates an endpoint for viewing and adding games. */
@RestController
public class GamesIdRecordsEndpoint {

  private GameRecordService gameRecordService;

  /** Autowires a {@link GamesIdRecordsEndpoint}. */
  @Autowired
  public GamesIdRecordsEndpoint(GameRecordService gameRecordService) {
    this.gameRecordService = gameRecordService;
  }

  /**
   * The <code>/games/{gameId}/records</code> {@code GET} endpoint returns the records for the
   * target games.
   *
   * <p>TODO: Add query parameter for user id.
   */
  @GetMapping("/games/{gameId}/records")
  public ResponseEntity<List<GameRecordResponse>> getGameRecords(
      @PathVariable(value = "gameId") String gameId) {
    try {
      List<GameRecord> gameRecords = gameRecordService.getGameRecords(Integer.valueOf(gameId));

      if (gameRecords.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      List<GameRecordResponse> out = new ArrayList<>();
      for (GameRecord gameRecord : gameRecords) {
        out.add(new GameRecordResponse(gameRecord));
      }

      return new ResponseEntity<>(out, HttpStatus.OK);
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
   * The <code>/games/{gameId}/records</code> {@code POST} endpoint creates a new record for the
   * target game.
   */
  @PostMapping("/games/{gameId}/records")
  public ResponseEntity<Void> addGameRecord(
      @PathVariable(value = "gameId") String gameId, @RequestBody GameRecordNewDto gameRecordDto) {
    try {
      gameRecordService.addGameRecord(
          Integer.valueOf(gameId), gameRecordDto.getUserId(), gameRecordDto.getScoreChange());
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (NumberFormatException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (NoResourceException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (PersonIsNotPlayerException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }
}
