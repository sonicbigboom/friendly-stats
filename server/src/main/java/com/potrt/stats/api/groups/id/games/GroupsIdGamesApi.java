/* Copyright (c) 2024 */
package com.potrt.stats.api.groups.id.games;

import com.potrt.stats.entities.Game.MaskedGame;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.services.GameService;
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
public class GroupsIdGamesApi {

  private GameService gameService;

  /** Autowires a {@link GroupsIdGamesApi}. */
  @Autowired
  public GroupsIdGamesApi(GameService gameService) {
    this.gameService = gameService;
  }

  /**
   * The {@code /groups/{groupID}/games} {@code GET} endpoint returns the games for the target
   * group.
   *
   * <p>TODO: Add query params for types of games, and closed games.
   */
  @GetMapping("/groups/{groupID}/games")
  public ResponseEntity<List<MaskedGame>> getGame(@PathVariable(value = "groupID") String groupID) {
    try {
      List<MaskedGame> games = gameService.getGames(Integer.valueOf(groupID));

      if (games.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(games, HttpStatus.OK);
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
   * The {@code /groups/{groupID}/games} {@code POST} endpoint creates a new game for the target
   * group.
   */
  @PostMapping("/groups/{groupID}/games")
  public ResponseEntity<Void> addGame(
      @PathVariable(value = "groupID") String groupID, @RequestBody GameDto gameDto) {
    try {
      gameService.addGame(Integer.valueOf(groupID), gameDto);
      return new ResponseEntity<>(HttpStatus.CREATED);
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
