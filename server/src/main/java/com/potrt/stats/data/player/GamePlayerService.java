/* Copyright (c) 2024 */
package com.potrt.stats.data.player;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.club.ClubService;
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
  private ClubService clubService;
  private MembershipService membershipService;
  private GameService gameService;
  private GamePlayerRepository gamePlayerRepository;

  /** Autowires a {@link GamePlayerService}. */
  @Autowired
  public GamePlayerService(
      SecurityService securityService,
      ClubService clubService,
      MembershipService membershipService,
      GameService gameService,
      GamePlayerRepository gamePlayerRepository) {
    this.securityService = securityService;
    this.clubService = clubService;
    this.membershipService = membershipService;
    this.gameService = gameService;
    this.gamePlayerRepository = gamePlayerRepository;
  }

  /**
   * Gets all of the game players for a game.
   *
   * @param gameID The {@link Game} id.
   * @return The {@link List} of {@link MaskedGamePlayer}s.
   * @throws NoResourceException Thrown if the {@link Game} does not exist.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a member of the {@link Club} that the
   *     game is a part of.
   */
  public List<MaskedGamePlayer> getGamePlayers(Integer gameID)
      throws NoResourceException, UnauthenticatedException, UnauthorizedException {
    Game game = gameService.getGameWithoutAuthorization(gameID);
    Integer clubID = game.getClubID();

    clubService.getClub(clubID);

    Iterable<GamePlayer> gamePlayers = gamePlayerRepository.findByGameID(gameID);

    List<MaskedGamePlayer> outGamePlayers = new ArrayList<>();
    for (GamePlayer gamePlayer : gamePlayers) {
      if (gamePlayer.isDeleted()) {
        continue;
      }
      outGamePlayers.add(new MaskedGamePlayer(gamePlayer));
    }

    return outGamePlayers;
  }

  /**
   * Adds a {@link GamePlayer} to a {@link Game}.
   *
   * @param gameID The {@link Game} id.
   * @param gamePlayerDto A {@link GamePlayerDto} for the {@link GamePlayer}.
   * @throws NoResourceException Thrown if the {@link Game} does not exist.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a member of the {@link Club} that the
   *     game is a part of.
   * @throws PersonIsNotMemberException Thrown if the target {@link Person} is not a member of the
   *     {@link Club}.
   */
  public void addGamePlayer(Integer gameID, GamePlayerDto gamePlayerDto)
      throws NoResourceException,
          UnauthenticatedException,
          UnauthorizedException,
          PersonIsNotMemberException {
    Game game = gameService.getGameWithoutAuthorization(gameID);
    Integer clubID = game.getClubID();

    Integer personID = securityService.getPersonID();
    if (!membershipService.hasRole(personID, clubID, PersonRole.GAME_ADMIN)) {
      throw new UnauthorizedException();
    }
    clubService.getClub(clubID);

    if (!membershipService.isMember(gamePlayerDto.getUserID(), clubID)) {
      throw new PersonIsNotMemberException();
    }

    Date now = new Date();
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

  public boolean isPlayer(Integer personID, Integer gameID)
      throws NoResourceException, UnauthenticatedException, UnauthorizedException {
    Game game = gameService.getGameWithoutAuthorization(gameID);
    Integer clubID = game.getClubID();

    clubService.getClub(clubID);

    Optional<GamePlayer> player = gamePlayerRepository.findById(new GamePerson(gameID, personID));

    if (player.isEmpty()) {
      return false;
    }

    return !player.get().isDeleted();
  }
}
