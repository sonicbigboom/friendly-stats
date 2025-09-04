/* Copyright (c) 2024 */
package com.potrt.stats.data.club;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link ClubResponse} represents a group in the application. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClubResponse {
  private Integer id;
  private String name;
  private Integer ownerUserId;
  private Integer storedCash;

  public ClubResponse(Club club) {
    this(club.getId(), club.getName(), club.getOwnerPersonId(), club.getStoredCash());
  }
}
