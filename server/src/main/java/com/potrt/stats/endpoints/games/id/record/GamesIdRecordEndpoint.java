/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.games.id.record;

import com.potrt.stats.data.gamerecord.GameRecord.MaskedGameRecord;
import com.potrt.stats.data.gamerecord.GameRecordService;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonIsNotMemberException;
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

/** Creates an endpoint for viewing and adding games. */
@RestController
public class GamesIdRecordEndpoint {

  private GameRecordService gameRecordService;

  /** Autowires a {@link GamesIdRecordEndpoint}. */
  @Autowired
  public GamesIdRecordEndpoint(GameRecordService gameRecordService) {
    this.gameRecordService = gameRecordService;
  }

  /**
   * The <code>/games/{gameID}/records</code> {@code GET} endpoint returns the records for the
   * target games.
   *
   * <p>TODO: Add query parameter for user id.
   */
  @GetMapping("/games/{gameID}/records")
  public ResponseEntity<List<MaskedGameRecord>> getGameRecord(
      @PathVariable(value = "gameID") String gameID) {
    try {
      List<MaskedGameRecord> gameRecords =
          gameRecordService.getGameRecords(Integer.valueOf(gameID));

      if (gameRecords.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(gameRecords, HttpStatus.OK);
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
   * The <code>/games/{gameID}/records</code> {@code POST} endpoint creates a new record for the
   * target game.
   */
  @PostMapping("/games/{gameID}/records")
  public ResponseEntity<Void> addGameRecord(
      @PathVariable(value = "gameID") String gameID, @RequestBody GameRecordDto gameRecordDto) {
    try {
      gameRecordService.addGameRecord(Integer.valueOf(gameID), gameRecordDto);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (NumberFormatException | PersonIsNotMemberException e) {
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
