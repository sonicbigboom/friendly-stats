/* Copywrite (c) 2024 */
package com.potrt.stats.repositories;

import com.potrt.stats.entities.Membership;
import com.potrt.stats.entities.Membership.PersonClub;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MembershipRepository extends CrudRepository<Membership, PersonClub> {

  @Query(
      "SELECT personRole IS NOT NULL FROM Membership WHERE personID = :personID AND clubID = :clubID")
  public Boolean isMember(Integer personID, Integer clubID);

  @Query("SELECT personRole FROM Membership WHERE personID = :personID AND clubID = :clubID")
  public String getRole(Integer personID, Integer clubID);

  @Query("SELECT clubID FROM Membership WHERE personID = :personID AND personRole IS NOT NULL")
  public Collection<Integer> getClubIDs(Integer personID);

  @Query("SELECT ISNULL(SUM(cashBalance), 0) FROM Membership WHERE personID = :personID")
  public Integer getCashBalance(Integer personID);

  @Query(
      "SELECT ISNULL(SUM(cashBalance), 0) FROM Membership WHERE personID = :personID AND personRole IS NOT NULL")
  public Integer getMemberedCashBalance(Integer personID);

  @Query("SELECT personID FROM Membership WHERE clubID = :clubID AND personRole IS NOT NULL")
  public Iterable<Integer> getPersonIDs(Integer clubID);
}
