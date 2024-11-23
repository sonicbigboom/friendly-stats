/* Copyright (c) 2024 */
package com.potrt.stats.data.game;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.game.Game.MaskedGame;
import com.potrt.stats.data.membership.PersonRole;
import com.potrt.stats.endpoints.groups.id.games.GameDto;
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
   * @param clubID The {@link Club} id.
   * @return The {@link List} of {@link Game}s.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Person} of the {@link Club}.
   * @throws NoResourceException Thrown if there is no {@link Club} with this id.
   */
  public List<MaskedGame> getGames(Integer clubID)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    securityService.assertHasPermission(clubID, PersonRole.PERSON);

    Iterable<Game> games = gameRepository.findByClubID(clubID);

    List<MaskedGame> outGames = new ArrayList<>();
    for (Game game : games) {
      if (game.isDeleted()) {
        continue;
      }
      outGames.add(new MaskedGame(game));
    }

    return outGames;
  }

  /**
   * Creates a new {@link Game}.
   *
   * @param clubID The {@link Club} id.
   * @param gameDto The {@link GameDto}.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Game Admin} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if there is no {@link Club} with this id.
   */
  public void addGame(Integer clubID, GameDto gameDto)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    securityService.assertHasPermission(clubID, PersonRole.GAME_ADMIN);

    Date now = new Date();
    Game game =
        new Game(
            null,
            clubID,
            gameDto.getGameTypeID(),
            gameDto.getName(),
            gameDto.isForCash(),
            gameDto.getSeasonID(),
            0,
            now,
            null,
            false);
    gameRepository.save(game);
  }

  /**
   * Gets a {@link Game} with a id.
   *
   * @param gameID The {@link Game} id.
   * @return The {@link Game}.
   * @throws NoResourceException Thrown if no game with this id exists.
   * @apiNote This service method does not check authorization.
   */
  public Game getGameWithoutAuthCheck(Integer gameID) throws NoResourceException {
    Optional<Game> game = gameRepository.findById(gameID);

    if (game.isEmpty()) {
      throw new NoResourceException("No game with id \"" + gameID + "\" exists.");
    }

    return game.get();
  }
}
