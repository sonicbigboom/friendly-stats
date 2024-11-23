/* Copyright (c) 2024 */
package com.potrt.stats.data.player;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.game.Game;
import com.potrt.stats.data.game.GameService;
import com.potrt.stats.data.membership.MembershipService;
import com.potrt.stats.data.membership.PersonRole;
import com.potrt.stats.data.person.Person;
import com.potrt.stats.data.player.GamePlayer.MaskedGamePlayer;
import com.potrt.stats.endpoints.games.id.players.GamePlayerDto;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonIsNotMemberException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.security.SecurityService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** The {@link GamePlayerService} is a service that allows for {@link GamePlayer} management. */
@Service
public class GamePlayerService {
  private SecurityService securityService;
  private GamePlayerRepository gamePlayerRepository;
  private MembershipService membershipService;
  private GameService gameService;

  /** Autowires a {@link GamePlayerService}. */
  @Autowired
  public GamePlayerService(
      SecurityService securityService,
      GamePlayerRepository gamePlayerRepository,
      MembershipService membershipService,
      GameService gameService) {
    this.securityService = securityService;
    this.gamePlayerRepository = gamePlayerRepository;
    this.membershipService = membershipService;
    this.gameService = gameService;
  }

  /**
   * Gets all of the game players for a game.
   *
   * @param gameID The {@link Game} id.
   * @return The {@link List} of {@link MaskedGamePlayer}s.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Game Admin} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if there is no {@link Game} with this id.
   */
  public List<MaskedGamePlayer> getGamePlayers(Integer gameID)
      throws NoResourceException, UnauthenticatedException, UnauthorizedException {
    Game game = gameService.getGameWithoutAuthCheck(gameID);
    Integer clubID = game.getClubID();
    securityService.assertHasPermission(clubID, PersonRole.PERSON);

    List<MaskedGamePlayer> gamePlayers = new ArrayList<>();
    for (GamePlayer gamePlayer : gamePlayerRepository.findByGameID(gameID)) {
      if (gamePlayer.isDeleted()) {
        continue;
      }
      gamePlayers.add(new MaskedGamePlayer(gamePlayer));
    }

    return gamePlayers;
  }

  /**
   * Adds a {@link GamePlayer} to a {@link Game}.
   *
   * @param gameID The {@link Game} id.
   * @param gamePlayerDto A {@link GamePlayerDto} for the {@link GamePlayer}.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Game Admin} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if there is no {@link Game} with this id.
   * @throws PersonIsNotMemberException Thrown if the target {@link Person} is not a member of the
   *     {@link Club}.
   */
  public void addGamePlayer(Integer gameID, GamePlayerDto gamePlayerDto)
      throws NoResourceException,
          UnauthenticatedException,
          UnauthorizedException,
          PersonIsNotMemberException {
    Game game = gameService.getGameWithoutAuthCheck(gameID);
    Integer clubID = game.getClubID();
    securityService.assertHasPermission(clubID, PersonRole.GAME_ADMIN);

    if (!membershipService.isMember(gamePlayerDto.getUserID(), clubID)) {
      throw new PersonIsNotMemberException();
    }

    Date now = new Date();
    Integer personID = securityService.getPersonID();
    GamePlayer gamePlayer =
        new GamePlayer(
            gameID,
            gamePlayerDto.getUserID(),
            gamePlayerDto.getMetadata(),
            false,
            now,
            personID,
            now,
            personID);
    gamePlayerRepository.save(gamePlayer);
  }

  /**
   * Checks if a {@link Person} is a player of a game.
   *
   * @param personID The target {@link Person}'s id.
   * @param gameID The target {@link Game} id.
   * @return Whether the {@link Person} is a player in the game.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Person} of the {@link Club}.
   * @throws NoResourceException Thrown if there is no {@link Game} with this id.
   */
  public boolean isPlayer(Integer personID, Integer gameID)
      throws NoResourceException, UnauthenticatedException, UnauthorizedException {
    Game game = gameService.getGameWithoutAuthCheck(gameID);
    Integer clubID = game.getClubID();
    securityService.assertHasPermission(clubID, PersonRole.PERSON);

    Optional<GamePlayer> player = gamePlayerRepository.findById(new GamePerson(gameID, personID));
    if (player.isEmpty()) {
      return false;
    }
    return !player.get().isDeleted();
  }
}
