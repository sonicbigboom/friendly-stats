/* Copyright (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.api.groups.id.games.GameDto;
import com.potrt.stats.entities.Club;
import com.potrt.stats.entities.Game;
import com.potrt.stats.entities.Game.MaskedGame;
import com.potrt.stats.entities.desc.PersonRole;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.repositories.GameRepository;
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
  private ClubService clubService;
  private MembershipService membershipService;
  private GameRepository gameRepository;

  /** Autowires a {@link GameService}. */
  @Autowired
  public GameService(
      SecurityService securityService,
      ClubService clubService,
      MembershipService membershipService,
      GameRepository gameRepository) {
    this.securityService = securityService;
    this.clubService = clubService;
    this.membershipService = membershipService;
    this.gameRepository = gameRepository;
  }

  /**
   * Gets the games for a {@link Club}.
   *
   * @param clubID The {@link Club} id.
   * @return The {@link List} of {@link Game}s.
   * @throws UnauthenticatedException Thrown if the user is not authenticated.
   * @throws UnauthorizedException Thrown if the user is not a member of the club.
   * @throws NoResourceException Thrown if the club doesn't exist.
   */
  public List<MaskedGame> getGames(Integer clubID)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    clubService.getClub(clubID);

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
   * @throws UnauthenticatedException Thrown if the caller is unauthenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Game Admin} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if the {@link Club} does not exist.
   */
  public void addGame(Integer clubID, GameDto gameDto)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    Integer personID = securityService.getPersonID();
    if (!membershipService.hasRole(personID, clubID, PersonRole.CASH_ADMIN)) {
      throw new UnauthorizedException();
    }
    clubService.getClub(clubID);

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
   */
  public Game getGameWithoutAuthorization(Integer gameID) throws NoResourceException {
    Optional<Game> game = gameRepository.findById(gameID);

    if (game.isEmpty()) {
      throw new NoResourceException();
    }

    return game.get();
  }
}
