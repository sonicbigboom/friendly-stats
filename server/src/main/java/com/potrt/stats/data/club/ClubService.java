/* Copyright (c) 2024 */
package com.potrt.stats.data.club;

import com.potrt.stats.data.club.Club.MaskedClub;
import com.potrt.stats.data.membership.MembershipService;
import com.potrt.stats.data.membership.PersonRole;
import com.potrt.stats.data.person.Person;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.security.SecurityService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/** The {@link ClubService} is a service that allows for {@link Club} management. */
@Service
@Transactional
public class ClubService {

  private SecurityService securityService;
  private ClubRepository clubRepository;
  private MembershipService membershipService;

  /** Autowires a {@link ClubService}. */
  public ClubService(
      SecurityService securityService,
      ClubRepository clubRepository,
      MembershipService membershipService) {
    this.securityService = securityService;
    this.clubRepository = clubRepository;
    this.membershipService = membershipService;
  }

  /**
   * Gets a {@link Club} by its id.
   *
   * @param clubID The {@link Club} id.
   * @return A {@link MaskedClub}.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not a {@code Person} of the {@link Club}.
   * @throws NoResourceException Thrown if there is no {@link Club} with this id.
   */
  public MaskedClub getClub(Integer clubID)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    securityService.assertHasPermission(clubID, PersonRole.PERSON);

    Club club = clubRepository.findById(clubID).get();
    return new MaskedClub(
        club,
        membershipService.hasRole(securityService.getPersonID(), clubID, PersonRole.CASH_ADMIN));
  }

  /**
   * Creates a new {@link Club}.
   *
   * @param name The name of the new {@link Club}.
   * @return The new {@link MaskedClub}.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   */
  public MaskedClub createClub(String name) throws UnauthenticatedException {
    Integer personID = securityService.getPersonID();
    Club club = new Club(null, name, personID, 0, false);
    club = clubRepository.save(club);
    membershipService.updatePermissions(personID, club.getId(), PersonRole.OWNER);
    return new MaskedClub(club, true);
  }

  /**
   * Gets all of the clubs that the caller {@link Person} is a member of.
   *
   * @return A {@link List} of {@link MaskedClub}s that the caller is a member of.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   */
  public List<MaskedClub> getClubs() throws UnauthenticatedException {
    Iterable<Integer> clubIDs = membershipService.getClubIDs();

    List<MaskedClub> clubs = new ArrayList<>();
    for (Club club : clubRepository.findAllById(clubIDs)) {
      if (club.isDeleted()) {
        continue;
      }

      clubs.add(new MaskedClub(club));
    }

    return clubs;
  }
}
