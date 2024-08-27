/* Copywrite (c) 2024 */
package com.potrt.stats.repositories;

import com.potrt.stats.entities.Club;
import com.potrt.stats.entities.Membership;
import com.potrt.stats.entities.Membership.PersonClub;
import com.potrt.stats.entities.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/** The {@link CrudRepository} for a {@link Membership}. */
public interface MembershipRepository extends CrudRepository<Membership, PersonClub> {

  /**
   * Determines if a {@link Person} is a member of {@link Club}.
   *
   * @param personID The {@link Person}'s id.
   * @param clubID The {@link Club}'s id.
   * @return Whether the {@link Person} is a member of the {@link Club}.
   */
  @Query(
      "SELECT CASE WHEN EXISTS \n"
          + "\t(SELECT personRole FROM Membership WHERE personID = :personID AND clubID = :clubID AND personRole IS NOT NULL) \n"
          + "THEN true ELSE false END")
  public boolean isMember(Integer personID, Integer clubID);

  /**
   * Gets the role a {@link Person} in a {@link Club}.
   *
   * @param personID The {@link Person}'s id.
   * @param clubID The {@link Club}'s id.
   * @return The {@link Person}'s role in the {@link Club}.
   */
  @Query("SELECT personRole FROM Membership WHERE personID = :personID AND clubID = :clubID")
  public String getRole(Integer personID, Integer clubID);

  /**
   * Gets the total combined cash balance for a {@link Person}.
   *
   * @param personID The {@link Person}'s id.
   * @return The {@link Person}'s total combined cash balance.
   */
  @Query("SELECT ISNULL(SUM(cashBalance), 0) FROM Membership WHERE personID = :personID")
  public Integer getCashBalance(Integer personID);

  /**
   * Gets the combined cash balance for a {@link Person} for {@link Club}s the {@link Person} is a
   * member of.
   *
   * @param personID The {@link Person}'s id.
   * @return The {@link Person}'s combined member cash balance.
   */
  @Query(
      "SELECT ISNULL(SUM(cashBalance), 0) FROM Membership WHERE personID = :personID AND personRole IS NOT NULL")
  public Integer getMemberedCashBalance(Integer personID);

  /**
   * Gets all of the {@link Club}s that the {@link Person} is a member of.
   *
   * @param personID The {@link Person}'s id.
   * @return A {@link Iterable} collection of {@link Club} ids.
   */
  @Query("SELECT clubID FROM Membership WHERE personID = :personID AND personRole IS NOT NULL")
  public Iterable<Integer> getClubIDs(Integer personID);

  public Iterable<Membership> getByClubID();
}
