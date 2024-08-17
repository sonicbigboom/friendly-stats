/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.entities.Club;
import com.potrt.stats.entities.masked.MaskedClub;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.repositories.ClubRepository;
import com.potrt.stats.repositories.MembershipRepository;
import com.potrt.stats.security.SecurityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ClubService {

  SecurityService securityService;
  ClubRepository clubRepository;
  MembershipRepository membershipRepository;

  public ClubService(
      SecurityService securityService,
      ClubRepository clubRepository,
      MembershipRepository membershipRepository) {
    this.securityService = securityService;
    this.clubRepository = clubRepository;
    this.membershipRepository = membershipRepository;
  }

  public List<MaskedClub> getClubs() throws UnauthenticatedException, NoResourceException {
    Integer personID = securityService.getPersonID();
    Collection<Integer> clubIDs = membershipRepository.getClubIDs(personID);

    List<MaskedClub> clubs = new ArrayList<>();
    for (Club club : clubRepository.findAllById(clubIDs)) {
      clubs.add(new MaskedClub(club));
    }

    if (clubs.isEmpty()) {
      throw new NoResourceException();
    }

    return clubs;
  }

  public MaskedClub getClub(Integer clubID)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    Integer personID = securityService.getPersonID();

    if (!Boolean.TRUE.equals(membershipRepository.isMember(personID, clubID))) {
      throw new UnauthorizedException();
    }

    Optional<Club> club = clubRepository.findById(clubID);
    if (club.isEmpty()) {
      throw new NoResourceException();
    }
    return new MaskedClub(club.get());
  }
}
