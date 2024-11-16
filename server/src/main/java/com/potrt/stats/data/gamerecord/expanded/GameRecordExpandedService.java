/* Copyright (c) 2024 */
package com.potrt.stats.data.gamerecord.expanded;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.club.ClubService;
import com.potrt.stats.data.game.Game;
import com.potrt.stats.data.gamerecord.GameRecord;
import com.potrt.stats.data.gamerecord.GameRecordService;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
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
  private ClubService clubService;
  private GameRecordExpandedRepository gameRecordExpandedRepository;

  /** Autowires a {@link GameRecordService}. */
  @Autowired
  public GameRecordExpandedService(
      ClubService clubService, GameRecordExpandedRepository gameRecordExpandedRepository) {
    this.clubService = clubService;
    this.gameRecordExpandedRepository = gameRecordExpandedRepository;
  }

  /**
   * Gets all of the expanded game records for a club.
   *
   * @param gameID The {@link Game} id.
   * @return The {@link List} of {@link GameRecord}s.
   * @throws NoResourceException Thrown if the {@link Game} does not exist.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a member of the {@link Club} that the
   *     game is a part of.
   */
  public List<GameRecordExpanded> getGameRecordExpandeds(
      Integer clubID,
      Optional<String> gameTypeID,
      Optional<String> forCash,
      Optional<String> seasonID,
      Optional<String> personID)
      throws NoResourceException,
          UnauthenticatedException,
          UnauthorizedException,
          NumberFormatException {

    clubService.getClub(clubID);

    GameRecordExpanded example = new GameRecordExpanded();
    example.setClubID(clubID);

    if (gameTypeID.isPresent()) {
      example.setGameTypeID(Integer.valueOf(gameTypeID.get()));
    }

    if (forCash.isPresent()) {
      example.setForCash(Boolean.parseBoolean(forCash.get()));
    }

    if (seasonID.isPresent()) {
      example.setSeasonID(Integer.valueOf(seasonID.get()));
    }

    if (personID.isPresent()) {
      example.setPersonID(Integer.valueOf(personID.get()));
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
