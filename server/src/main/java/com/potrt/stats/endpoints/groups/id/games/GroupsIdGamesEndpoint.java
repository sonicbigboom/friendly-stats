/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.groups.id.games;

import com.potrt.stats.data.game.Game;
import com.potrt.stats.data.game.GameResponse;
import com.potrt.stats.data.game.GameService;
import com.potrt.stats.exceptions.NoResourceException;
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
public class GroupsIdGamesEndpoint {

  private GameService gameService;

  /** Autowires a {@link GroupsIdGamesEndpoint}. */
  @Autowired
  public GroupsIdGamesEndpoint(GameService gameService) {
    this.gameService = gameService;
  }

  /**
   * The {@code /groups/{groupId}/games} {@code GET} endpoint returns the games for the target
   * group.
   *
   * <p>TODO: Add query params for types of games, and closed games.
   */
  @GetMapping("/groups/{groupId}/games")
  public ResponseEntity<List<GameResponse>> getGame(
      @PathVariable(value = "groupId") String groupId) {
    try {
      List<Game> games = gameService.getGames(Integer.valueOf(groupId));

      if (games.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      List<GameResponse> responses = new ArrayList<>();
      for (Game game : games) {
        responses.add(new GameResponse(game));
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

  /**
   * The {@code /groups/{groupId}/games} {@code POST} endpoint creates a new game for the target
   * group.
   */
  @PostMapping("/groups/{groupId}/games")
  public ResponseEntity<Void> addGame(
      @PathVariable(value = "groupId") String groupId, @RequestBody GameNewDto gameDto) {
    try {
      gameService.addGame(
          Integer.valueOf(groupId),
          gameDto.getGameTypeId(),
          gameDto.getName(),
          gameDto.isForCash(),
          gameDto.getSeasonId());
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
