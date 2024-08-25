/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.entities.Club;
import com.potrt.stats.entities.Club.MaskedClub;
import com.potrt.stats.entities.Membership;
import com.potrt.stats.entities.Membership.PersonClub;
import com.potrt.stats.entities.Person;
import com.potrt.stats.entities.Person.MaskedPerson;
import com.potrt.stats.entities.PersonRole;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonAlreadyExistsException;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.repositories.ClubRepository;
import com.potrt.stats.repositories.MembershipRepository;
import com.potrt.stats.security.SecurityService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClubService {

  private SecurityService securityService;
  private ClubRepository clubRepository;
  private MembershipRepository membershipRepository;
  private PersonService personService;

  public ClubService(
      SecurityService securityService,
      ClubRepository clubRepository,
      MembershipRepository membershipRepository,
      PersonService personService) {
    this.securityService = securityService;
    this.clubRepository = clubRepository;
    this.membershipRepository = membershipRepository;
    this.personService = personService;
  }

  public List<MaskedClub> getClubs() throws UnauthenticatedException {
    Integer personID = securityService.getPersonID();
    Collection<Integer> clubIDs = membershipRepository.getClubIDs(personID);

    List<MaskedClub> clubs = new ArrayList<>();
    for (Club club : clubRepository.findAllById(clubIDs)) {
      if (Boolean.TRUE.equals(club.getIsDeleted())) {
        continue;
      }

      clubs.add(new MaskedClub(club));
    }

    return clubs;
  }

  public MaskedClub getClub(Integer clubID)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    Integer personID = securityService.getPersonID();

    String role = membershipRepository.getRole(personID, clubID);

    if (role == null) {
      throw new UnauthorizedException();
    }

    Optional<Club> club = clubRepository.findById(clubID);
    if (club.isEmpty() || Boolean.TRUE.equals(club.get().getIsDeleted())) {
      throw new NoResourceException();
    }
    return new MaskedClub(club.get(), PersonRole.CASH_ADMIN.permits(role));
  }

  public MaskedClub createClub(String name) throws UnauthenticatedException {
    Integer personID = securityService.getPersonID();
    Club club = new Club(null, name, personID, 0, false);
    club = clubRepository.save(club);

    Membership membership = new Membership(personID, club.getId(), 0, "Owner");
    membershipRepository.save(membership);
    return new MaskedClub(club, true);
  }

  public List<MaskedPerson> getPersons(Integer clubID)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    Integer personID = securityService.getPersonID();

    String role = membershipRepository.getRole(personID, clubID);

    if (role == null) {
      throw new UnauthorizedException();
    }

    Optional<Club> club = clubRepository.findById(clubID);
    if (club.isEmpty() || Boolean.TRUE.equals(club.get().getIsDeleted())) {
      throw new NoResourceException();
    }

    Iterable<Integer> personIDs = membershipRepository.getPersonIDs(clubID);

    return personService.getPersons(personIDs);
  }

  public void addPerson(Integer clubID, Integer personID)
      throws UnauthenticatedException,
          UnauthorizedException,
          NoResourceException,
          PersonDoesNotExistException,
          PersonAlreadyExistsException {
    Integer callerID = securityService.getPersonID();

    String role = membershipRepository.getRole(callerID, clubID);

    if (!PersonRole.GAME_ADMIN.permits(role)) {
      throw new UnauthorizedException();
    }

    Optional<Club> club = clubRepository.findById(clubID);
    if (club.isEmpty() || Boolean.TRUE.equals(club.get().getIsDeleted())) {
      throw new NoResourceException();
    }

    personService.getPerson(personID);

    Optional<Membership> optionalMembership =
        membershipRepository.findById(new PersonClub(personID, clubID));
    if (optionalMembership.isPresent() && optionalMembership.get().getPersonRole() != null) {
      throw new PersonAlreadyExistsException();
    }

    Membership membership = new Membership(personID, clubID, 0, PersonRole.PERSON.getIdentifier());
    membershipRepository.save(membership);
  }

  public Person addPerson(Integer clubID, MaskedPerson person)
      throws UnauthenticatedException,
          UnauthorizedException,
          NoResourceException,
          PersonAlreadyExistsException {

    Person personToAdd;
    try {
      personToAdd = personService.getPerson(person.getEmail());
      if (Boolean.FALSE.equals(personToAdd.getIsDisabled())) {
        throw new PersonAlreadyExistsException();
      }
    } catch (PersonDoesNotExistException e) {
      personToAdd =
          new Person(
              null,
              person.getEmail(),
              person.getUsername(),
              person.getFirstName(),
              person.getLastName(),
              person.getNickname(),
              true,
              false);
      personToAdd = personService.createPerson(personToAdd);
    }

    try {
      addPerson(clubID, personToAdd.getId());
    } catch (PersonDoesNotExistException e) {
      throw new RuntimeException(e);
    }

    return personToAdd;
  }
}
