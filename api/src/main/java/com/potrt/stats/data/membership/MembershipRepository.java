/* Copyright (c) 2024 */
package com.potrt.stats.data.membership;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.person.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/** The {@link CrudRepository} for a {@link Membership}. */
public interface MembershipRepository extends CrudRepository<Membership, PersonClub> {

  /**
   * Determines if a {@link Person} is a member of {@link Club}.
   *
   * @param personId The {@link Person}'s id.
   * @param clubId The {@link Club}'s id.
   * @return Whether the {@link Person} is a member of the {@link Club}.
   */
  @Query(
      "SELECT CASE WHEN EXISTS \n"
          + "\t(SELECT personRole FROM Membership WHERE personId = :personId AND clubId = :clubId AND personRole IS NOT NULL) \n"
          + "THEN true ELSE false END")
  public boolean isMember(Integer personId, Integer clubId);

  /**
   * Gets the role a {@link Person} in a {@link Club}.
   *
   * @param personId The {@link Person}'s id.
   * @param clubId The {@link Club}'s id.
   * @return The {@link Person}'s role in the {@link Club}.
   */
  @Query("SELECT personRole FROM Membership WHERE personId = :personId AND clubId = :clubId")
  public String getRole(Integer personId, Integer clubId);

  /**
   * Gets the total combined cash balance for a {@link Person}.
   *
   * @param personId The {@link Person}'s id.
   * @return The {@link Person}'s total combined cash balance.
   */
  @Query("SELECT ISNULL(SUM(cashBalance), 0) FROM Membership WHERE personId = :personId")
  public Integer getCashBalance(Integer personId);

  /**
   * Gets the combined cash balance for a {@link Person} for {@link Club}s the {@link Person} is a
   * member of.
   *
   * @param personId The {@link Person}'s id.
   * @return The {@link Person}'s combined member cash balance.
   */
  @Query(
      "SELECT ISNULL(SUM(cashBalance), 0) FROM Membership WHERE personId = :personId AND personRole IS NOT NULL")
  public Integer getMemberedCashBalance(Integer personId);

  /**
   * Gets all of the {@link Club}s that the {@link Person} is a member of.
   *
   * @param personId The {@link Person}'s id.
   * @return A {@link Iterable} collection of {@link Club} ids.
   */
  @Query("SELECT clubId FROM Membership WHERE personId = :personId AND personRole IS NOT NULL")
  public Iterable<Integer> getClubIds(Integer personId);

  public Iterable<Membership> getByClubId(Integer clubId);
}
