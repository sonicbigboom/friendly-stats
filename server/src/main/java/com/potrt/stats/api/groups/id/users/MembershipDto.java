/* Copywrite (c) 2024 */
package com.potrt.stats.api.groups.id.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A {@link MembershipDto} represents a new {@link Membership}. A {@link identifier} can be either a
 * {@link Person} id or email.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDto {
  private String identifier;
  private String personRole;
  private String firstName;
  private String lastName;
  private String nickname;
}
