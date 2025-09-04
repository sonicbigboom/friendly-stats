/* Copyright (c) 2024 */
package com.potrt.stats.data.gamerecord;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.game.Game;
import com.potrt.stats.data.game.GameService;
import com.potrt.stats.data.membership.MembershipService;
import com.potrt.stats.data.membership.PersonRole;
import com.potrt.stats.data.person.Person;
import com.potrt.stats.data.player.GamePlayerService;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonIsNotPlayerException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.security.SecurityService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** The {@link GameRecordService} is a service that allows for {@link GameRecord} management. */
@Service
public class GameRecordService {
  private SecurityService securityService;
  private GameRecordRepository gameRecordRepository;
  private GameService gameService;
  private GamePlayerService gamePlayerService;
  private MembershipService membershipService;

  /** Autowires a {@link GameRecordService}. */
  @Autowired
  public GameRecordService(
      SecurityService securityService,
      GameRecordRepository gameRecordRepository,
      GameService gameService,
      GamePlayerService gamePlayerService,
      MembershipService membershipService) {
    this.securityService = securityService;
    this.gameRecordRepository = gameRecordRepository;
    this.gameService = gameService;
    this.gamePlayerService = gamePlayerService;
    this.membershipService = membershipService;
  }

  /**
   * Gets all of the game records for a game.
   *
   * @param gameId The {@link Game} id.
   * @return The {@link List} of {@link GameRecord}s.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Person} of the {@link Club}.
   * @throws NoResourceException Thrown if there is no {@link Game} with this id.
   * @apiNote The {@code Cash Admin} role is required to receive unmasked data.
   */
  public List<GameRecord> getGameRecords(Integer gameId)
      throws NoResourceException, UnauthenticatedException, UnauthorizedException {
    Game game = gameService.getGameWithoutAuthCheck(gameId);
    Integer clubId = game.getClubId();
    securityService.assertHasPermission(clubId, PersonRole.PERSON);

    boolean isGameAdmin =
        membershipService.hasPermission(
            securityService.getPersonId(), clubId, PersonRole.CASH_ADMIN);

    List<GameRecord> gameRecords = new ArrayList<>();
    for (GameRecord gameRecord : gameRecordRepository.findByGameId(gameId)) {
      if (gameRecord.isDeleted()) {
        continue;
      }

      if (!isGameAdmin) {
        gameRecord = gameRecord.mask();
      }

      gameRecords.add(gameRecord);
    }

    return gameRecords;
  }

  /**
   * Adds a game record to a game.
   *
   * @param gameId The {@link Game} id.
   * @param personId The {@link Person}'s id.
   * @param scoreChange The score change.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code CASH_ADMIN} of the {@link
   *     Club}.
   * @throws NoResourceException Thrown if there is no {@link Game} with this id.
   * @throws PersonIsNotPlayerException Thrown if the target {@link Person} is not a player of this
   *     game.
   */
  public void addGameRecord(Integer gameId, Integer personId, Integer scoreChange)
      throws NoResourceException,
          UnauthenticatedException,
          UnauthorizedException,
          PersonIsNotPlayerException {
    Game game = gameService.getGameWithoutAuthCheck(gameId);
    Integer clubId = game.getClubId();
    securityService.assertHasPermission(clubId, PersonRole.CASH_ADMIN);

    if (!gamePlayerService.isPlayer(personId, gameId)) {
      throw new PersonIsNotPlayerException();
    }

    Date now = new Date();
    Integer callerId = securityService.getPersonId();
    GameRecord gameRecord =
        new GameRecord(null, gameId, personId, scoreChange, false, now, callerId, now, callerId);
    gameRecordRepository.save(gameRecord);
  }
}
