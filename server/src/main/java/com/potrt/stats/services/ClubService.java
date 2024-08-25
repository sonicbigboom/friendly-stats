/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import com.potrt.stats.entities.Club;
import com.potrt.stats.entities.Club.MaskedClub;
import com.potrt.stats.entities.Person;
import com.potrt.stats.entities.Person.MaskedPerson;
import com.potrt.stats.entities.PersonRole;
import com.potrt.stats.exceptions.ImpossibleRuntimeException;
import com.potrt.stats.exceptions.NoResourceException;
import com.potrt.stats.exceptions.PersonAlreadyExistsException;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.exceptions.UnauthorizedException;
import com.potrt.stats.repositories.ClubRepository;
import com.potrt.stats.security.SecurityService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/** The {@link ClubService} is a service that allows for {@link Club} management. */
@Service
@Transactional
public class ClubService {

  private SecurityService securityService;
  private ClubRepository clubRepository;
  private MembershipService membershipService;
  private PersonService personService;

  /** Autowires a {@link ClubService}. */
  public ClubService(
      SecurityService securityService,
      ClubRepository clubRepository,
      MembershipService membershipService,
      PersonService personService) {
    this.securityService = securityService;
    this.clubRepository = clubRepository;
    this.membershipService = membershipService;
    this.personService = personService;
  }

  /**
   * Gets a {@link Club} by its id.
   *
   * @param clubID The {@link Club} id.
   * @return A {@link MaskedClub}.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller is not authorized to view this club.
   * @throws NoResourceException Thrown if there is no {@link Club} with this id.
   */
  public MaskedClub getClub(Integer clubID)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    Integer personID = securityService.getPersonID();

    if (!membershipService.hasRole(personID, clubID, PersonRole.PERSON)) {
      throw new UnauthorizedException();
    }

    Optional<Club> club = clubRepository.findById(clubID);
    if (club.isEmpty() || club.get().isDeleted()) {
      throw new NoResourceException();
    }
    return new MaskedClub(
        club.get(), membershipService.hasRole(personID, clubID, PersonRole.CASH_ADMIN));
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
   * Gets all of the members of a {@link Club}.
   *
   * @param clubID The {@link Club}'s id.
   * @return The {@link List} of {@link Person}s withing the club.
   * @throws UnauthenticatedException Thrown if the caller is unauthenticated.
   * @throws UnauthorizedException Thrown if the caller is not authorized to view this club.
   * @throws NoResourceException Thrown if the club does not exist.
   */
  public List<MaskedPerson> getPersons(Integer clubID)
      throws UnauthenticatedException, UnauthorizedException, NoResourceException {
    Optional<Club> club = clubRepository.findById(clubID);
    if (club.isEmpty() || Boolean.TRUE.equals(club.get().isDeleted())) {
      throw new NoResourceException();
    }

    Iterable<Integer> personIDs = membershipService.getPersonIDs(clubID);

    return personService.getPersons(personIDs);
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

  /**
   * Adds a {@link Person} to a {@link Club}.
   *
   * @param clubID The {@link Club} id.
   * @param personID The {@link Person} id.
   * @throws UnauthenticatedException Thrown if the caller is not authenticated.
   * @throws UnauthorizedException Thrown if the caller does not have permission to add a member.
   * @throws NoResourceException Thrown if the {@link Club} does not exist.
   * @throws PersonDoesNotExistException Thrown if the {@link Person} does not exist.
   * @throws PersonAlreadyExistsException Thrown if the {@link Person} is already a member of the
   *     {@link Club}.
   */
  public void addPerson(Integer clubID, Integer personID)
      throws UnauthenticatedException,
          UnauthorizedException,
          NoResourceException,
          PersonDoesNotExistException,
          PersonAlreadyExistsException {

    if (!membershipService.hasRole(securityService.getPersonID(), clubID, PersonRole.GAME_ADMIN)) {
      throw new UnauthorizedException();
    }
    Optional<Club> club = clubRepository.findById(clubID);
    if (club.isEmpty() || club.get().isDeleted()) {
      throw new NoResourceException();
    }
    personService.getPerson(personID);
    if (membershipService.isMember(personID, clubID)) {
      throw new PersonAlreadyExistsException();
    }

    membershipService.updatePermissions(personID, clubID, PersonRole.PERSON);
  }

  @Deprecated
  public Person addPerson(Integer clubID, MaskedPerson person)
      throws UnauthenticatedException,
          UnauthorizedException,
          NoResourceException,
          PersonAlreadyExistsException {

    Person personToAdd;
    try {
      personToAdd = personService.getPersonByEmail(person.getEmail());
      if (Boolean.FALSE.equals(personToAdd.isDisabled())) {
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
      throw new ImpossibleRuntimeException(e);
    }

    return personToAdd;
  }
}
