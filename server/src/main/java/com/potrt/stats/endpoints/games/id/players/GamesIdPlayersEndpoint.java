/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.games.id.players;

import com.potrt.stats.data.player.GamePlayer;
import com.potrt.stats.data.player.GamePlayerResponse;
import com.potrt.stats.data.player.GamePlayerService;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonIsNotMemberException;
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

/** Creates an endpoint for viewing and adding players to games. */
@RestController
public class GamesIdPlayersEndpoint {

  private GamePlayerService gamePlayerService;

  /** Autowires a {@link GamesIdPlayersEndpoint}. */
  @Autowired
  public GamesIdPlayersEndpoint(GamePlayerService gamePlayerService) {
    this.gamePlayerService = gamePlayerService;
  }

  /**
   * The <code>/games/{gameId}/players</code> {@code GET} endpoint returns the players for the
   * target games.
   */
  @GetMapping("/games/{gameId}/players")
  public ResponseEntity<List<GamePlayerResponse>> getGamePlayers(
      @PathVariable(value = "gameId") String gameId) {
    try {
      List<GamePlayer> gamePlayers = gamePlayerService.getGamePlayers(Integer.valueOf(gameId));

      if (gamePlayers.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      List<GamePlayerResponse> gamePlayerDto = new ArrayList<>();
      for (GamePlayer gamePlayer : gamePlayers) {
        gamePlayerDto.add(new GamePlayerResponse(gamePlayer));
      }

      return new ResponseEntity<>(gamePlayerDto, HttpStatus.OK);
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
   * The <code>/games/{gameId}/players</code> {@code POST} endpoint adds a player for the target
   * game.
   */
  @PostMapping("/games/{gameId}/players")
  public ResponseEntity<Void> addGamePlayer(
      @PathVariable(value = "gameId") String gameId, @RequestBody GamePlayerNewDto gamePlayerDto) {
    try {
      gamePlayerService.addGamePlayer(
          Integer.valueOf(gameId), gamePlayerDto.getUserId(), gamePlayerDto.getMetadata());
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (NumberFormatException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthenticatedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (NoResourceException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (PersonIsNotMemberException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }
}
