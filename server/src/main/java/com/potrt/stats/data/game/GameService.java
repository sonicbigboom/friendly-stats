/* Copyright (c) 2024 */
package com.potrt.stats.data.game;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.membership.PersonRole;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.security.SecurityService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** The {@link GameService} is a service that allows for {@link Game} management. */
@Service
public class GameService {
  private SecurityService securityService;
  private GameRepository gameRepository;

  /** Autowires a {@link GameService}. */
  @Autowired
  public GameService(SecurityService securityService, GameRepository gameRepository) {
    this.securityService = securityService;
    this.gameRepository = gameRepository;
  }

  /**
   * Gets the games for a {@link Club}.
   *
   * @param clubId The {@link Club} id.
   * @return The {@link List} of {@link Game}s.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Person} of the {@link Club}.
   * @throws NoResourceException Thrown if there is no {@link Club} with this id.
   */
  public List<Game> getGames(Integer clubId)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    securityService.assertHasPermission(clubId, PersonRole.PERSON);

    Iterable<Game> games = gameRepository.findByClubId(clubId);

    List<Game> out = new ArrayList<>();
    for (Game game : games) {
      if (game.isDeleted()) {
        continue;
      }

      out.add(game);
    }

    return out;
  }

  /**
   * Creates a new {@link Game}.
   *
   * @param clubId The {@link Club} id.
   * @param gameTypeId The {@link GameType} id of the game.
   * @param name The name of the game.
   * @param isForCash Whether the game is for real money.
   * @parma seasonId The season id.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Game Admin} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if there is no {@link Club} with this id.
   */
  public void addGame(
      Integer clubId, Integer gameTypeId, String name, boolean isForCash, Integer seasonId)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    securityService.assertHasPermission(clubId, PersonRole.GAME_ADMIN);

    Date now = new Date();
    Game game = new Game(null, clubId, gameTypeId, name, isForCash, seasonId, 0, now, null, false);
    gameRepository.save(game);
  }

  /**
   * Gets a {@link Game} with a id.
   *
   * @param gameId The {@link Game} id.
   * @return The {@link Game}.
   * @throws NoResourceException Thrown if no game with this id exists.
   * @apiNote This service method does not check authorization.
   */
  public Game getGameWithoutAuthCheck(Integer gameId) throws NoResourceException {
    Optional<Game> game = gameRepository.findById(gameId);

    if (game.isEmpty()) {
      throw new NoResourceException("No game with id \"" + gameId + "\" exists.");
    }

    return game.get();
  }
}
