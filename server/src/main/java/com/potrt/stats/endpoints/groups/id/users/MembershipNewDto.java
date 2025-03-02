/* Copyright (c) 2024 */
package com.potrt.stats.endpoints.groups.id.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A {@link MembershipNewDto} represents a new {@link Membership}. A {@link identifier} can be
 * either a {@link Person} id or email.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipNewDto {
  private String identifier;
  private String personRole;
  private String firstName;
  private String lastName;
  private String nickname;
}
