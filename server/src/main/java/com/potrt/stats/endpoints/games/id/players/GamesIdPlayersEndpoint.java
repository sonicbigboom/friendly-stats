/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.games.id.players;

import com.potrt.stats.data.player.GamePlayer.MaskedGamePlayer;
import com.potrt.stats.data.player.GamePlayerService;
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
   * The <code>/games/{gameID}/players</code> {@code GET} endpoint returns the players for the
   * target games.
   *
   * <p>TODO: Add query parameter for user id.
   */
  @GetMapping("/games/{gameID}/players")
  public ResponseEntity<List<MaskedGamePlayer>> getGamePlayers(
      @PathVariable(value = "gameID") String gameID) {
    try {
      List<MaskedGamePlayer> gamePlayers =
          gamePlayerService.getGamePlayers(Integer.valueOf(gameID));

      if (gamePlayers.isEmpty()) {
        return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(gamePlayers, HttpStatus.OK);
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
   * The <code>/games/{gameID}/players</code> {@code POST} endpoint adds a player for the target
   * game.
   */
  @PostMapping("/games/{gameID}/players")
  public ResponseEntity<Void> addGamePlayer(
      @PathVariable(value = "gameID") String gameID, @RequestBody GamePlayerDto gamePlayerDto) {
    try {
      gamePlayerService.addGamePlayer(Integer.valueOf(gameID), gamePlayerDto);
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
