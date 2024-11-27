/* Copyright (c) 2024 */
package com.potrt.stats.data.gamerecord.expanded;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.game.Game;
import com.potrt.stats.data.gamerecord.GameRecord;
import com.potrt.stats.data.gamerecord.GameRecordService;
import com.potrt.stats.data.membership.PersonRole;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.security.SecurityService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * The {@link GameRecordExpandedService} is a service that allows for {@link GameRecordExpanded}
 * management.
 */
@Service
public class GameRecordExpandedService {

  private SecurityService securityService;
  private GameRecordExpandedRepository gameRecordExpandedRepository;

  /** Autowires a {@link GameRecordService}. */
  @Autowired
  public GameRecordExpandedService(
      SecurityService securityService, GameRecordExpandedRepository gameRecordExpandedRepository) {
    this.securityService = securityService;
    this.gameRecordExpandedRepository = gameRecordExpandedRepository;
  }

  /**
   * Gets all of the expanded game records for a club.
   *
   * @param clubId The club's id.
   * @param gameTypeId Which {@link GameType} id to track for.
   * @param forCash Whether to track for cash.
   * @param seasonId Which season to track for.
   * @param personId Which {@link Person} to track for.
   * @return The {@link List} of {@link GameRecord}s.
   * @throws NoResourceException Thrown if the {@link Game} does not exist.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a member of the {@link Club} that the
   *     game is a part of.
   */
  public List<GameRecordExpanded> getGameRecordExpandeds(
      Integer clubId,
      Optional<String> gameTypeId,
      Optional<String> forCash,
      Optional<String> seasonId,
      Optional<String> personId)
      throws NoResourceException,
          UnauthenticatedException,
          UnauthorizedException,
          NumberFormatException {

    securityService.assertHasPermission(clubId, PersonRole.PERSON);

    GameRecordExpanded example = new GameRecordExpanded();
    example.setClubId(clubId);

    if (gameTypeId.isPresent()) {
      example.setGameTypeId(Integer.valueOf(gameTypeId.get()));
    }

    if (forCash.isPresent()) {
      example.setForCash(Boolean.parseBoolean(forCash.get()));
    }

    if (seasonId.isPresent()) {
      example.setSeasonId(Integer.valueOf(seasonId.get()));
    }

    if (personId.isPresent()) {
      example.setPersonId(Integer.valueOf(personId.get()));
    }

    Iterable<GameRecordExpanded> gameRecordExpandeds =
        gameRecordExpandedRepository.findAll(Example.of(example));

    List<GameRecordExpanded> outGameRecordExpandeds = new ArrayList<>();
    for (GameRecordExpanded gameRecordExpanded : gameRecordExpandeds) {
      outGameRecordExpandeds.add(gameRecordExpanded);
    }

    return outGameRecordExpandeds;
  }
}
