/* Copywrite (c) 2024 */
package com.potrt.stats.repositories;

import com.potrt.stats.entities.Membership;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MembershipRepository extends CrudRepository<Membership, Integer> {

  @Query("SELECT M.isMember FROM Membership M WHERE personID = :personID AND clubID = :clubID")
  public Boolean isMember(Integer personID, Integer clubID);

  @Query("SELECT M.clubID FROM Membership M WHERE personID = :personID AND isMember = 1")
  public Collection<Integer> getClubIDs(Integer personID);
}
