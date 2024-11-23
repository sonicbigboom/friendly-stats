/* Copyright (c) 2024 */
package com.potrt.stats.data.gamerecord;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.game.Game;
import com.potrt.stats.data.game.GameService;
import com.potrt.stats.data.gamerecord.GameRecord.MaskedGameRecord;
import com.potrt.stats.data.membership.PersonRole;
import com.potrt.stats.data.person.Person;
import com.potrt.stats.data.player.GamePlayerService;
import com.potrt.stats.endpoints.games.id.records.GameRecordDto;
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
  private GameService gameService;
  private GamePlayerService gamePlayerService;
  private GameRecordRepository gameRecordRepository;

  /** Autowires a {@link GameRecordService}. */
  @Autowired
  public GameRecordService(
      SecurityService securityService,
      GameService gameService,
      GamePlayerService gamePlayerService,
      GameRecordRepository gameRecordRepository) {
    this.securityService = securityService;
    this.gameService = gameService;
    this.gamePlayerService = gamePlayerService;
    this.gameRecordRepository = gameRecordRepository;
  }

  /**
   * Gets all of the game records for a game.
   *
   * @param gameID The {@link Game} id.
   * @return The {@link List} of {@link GameRecord}s.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Person} of the {@link Club}.
   * @throws NoResourceException Thrown if there is no {@link Game} with this id.
   */
  public List<MaskedGameRecord> getGameRecords(Integer gameID)
      throws NoResourceException, UnauthenticatedException, UnauthorizedException {
    Game game = gameService.getGameWithoutAuthCheck(gameID);
    Integer clubID = game.getClubID();
    securityService.assertHasPermission(clubID, PersonRole.PERSON);

    List<MaskedGameRecord> gameRecords = new ArrayList<>();
    for (GameRecord gameRecord : gameRecordRepository.findByGameID(gameID)) {
      if (gameRecord.isDeleted()) {
        continue;
      }
      gameRecords.add(new MaskedGameRecord(gameRecord));
    }

    return gameRecords;
  }

  /**
   * Adds a game record to a game.
   *
   * @param gameID The {@link Game} id.
   * @param gameRecordDto
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Person} of the {@link Club}.
   * @throws NoResourceException Thrown if there is no {@link Game} with this id.
   * @throws PersonIsNotPlayerException Thrown if the target {@link Person} is not a player of this
   *     game.
   */
  public void addGameRecord(Integer gameID, GameRecordDto gameRecordDto)
      throws NoResourceException,
          UnauthenticatedException,
          UnauthorizedException,
          PersonIsNotPlayerException {
    Game game = gameService.getGameWithoutAuthCheck(gameID);
    Integer clubID = game.getClubID();
    securityService.assertHasPermission(clubID, PersonRole.PERSON);

    if (!gamePlayerService.isPlayer(gameRecordDto.getUserID(), gameID)) {
      throw new PersonIsNotPlayerException();
    }

    Date now = new Date();
    Integer personID = securityService.getPersonID();
    GameRecord gameRecord =
        new GameRecord(
            null,
            gameID,
            gameRecordDto.getUserID(),
            gameRecordDto.getScoreChange(),
            false,
            now,
            personID,
            now,
            personID);
    gameRecordRepository.save(gameRecord);
  }
}
