/* Copyright (c) 2024 */
package com.potrt.stats.data.membership;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.person.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link MembershipResponse} represents a {@link Person}'s connection to a {@link Club}. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MembershipResponse {
  private Integer userId;
  private Integer groupId;
  private String userRole;
  private Integer cashBalance;
  private String firstName;
  private String lastName;
  private String nickname;

  public MembershipResponse(Membership membership) {
    this(
        membership.getPersonId(),
        membership.getClubId(),
        membership.getPersonRole(),
        membership.getCashBalance(),
        membership.getFirstName(),
        membership.getLastName(),
        membership.getNickname());
  }
}
