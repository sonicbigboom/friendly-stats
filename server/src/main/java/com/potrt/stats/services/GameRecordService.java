/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.api.games.id.record.GameRecordDto;
import com.potrt.stats.entities.Game;
import com.potrt.stats.entities.GameRecord;
import com.potrt.stats.entities.GameRecord.MaskedGameRecord;
import com.potrt.stats.entities.desc.PersonRole;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonIsNotMemberException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.repositories.GameRecordRepository;
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
  private ClubService clubService;
  private MembershipService membershipService;
  private GameService gameService;
  private GameRecordRepository gameRecordRepository;

  /** Autowires a {@link GameRecordService}. */
  @Autowired
  public GameRecordService(
      SecurityService securityService,
      ClubService clubService,
      MembershipService membershipService,
      GameService gameService,
      GameRecordRepository gameRecordRepository) {
    this.securityService = securityService;
    this.clubService = clubService;
    this.membershipService = membershipService;
    this.gameService = gameService;
    this.gameRecordRepository = gameRecordRepository;
  }

  /**
   * Gets all of the game records for a game.
   *
   * @param gameID The {@link Game} id.
   * @return The {@link List} of {@link GameRecord}s.
   * @throws NoResourceException Thrown if the {@link Game} does not exist.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a member of the {@link Club} that the
   *     game is a part of.
   */
  public List<MaskedGameRecord> getGameRecords(Integer gameID)
      throws NoResourceException, UnauthenticatedException, UnauthorizedException {
    Game game = gameService.getGameWithoutAuthorization(gameID);
    Integer clubID = game.getClubID();

    clubService.getClub(clubID);

    Iterable<GameRecord> gameRecords = gameRecordRepository.findByGameID(gameID);

    List<MaskedGameRecord> outGameRecords = new ArrayList<>();
    for (GameRecord gameRecord : gameRecords) {
      if (gameRecord.isDeleted()) {
        continue;
      }
      outGameRecords.add(new MaskedGameRecord(gameRecord));
    }

    return outGameRecords;
  }

  public void addGameRecord(Integer gameID, GameRecordDto gameRecordDto)
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

    if (!membershipService.isMember(gameRecordDto.getUserID(), clubID)) {
      throw new PersonIsNotMemberException();
    }

    Date now = new Date();
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
