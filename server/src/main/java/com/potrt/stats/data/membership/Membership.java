/* Copyright (c) 2024 */
package com.potrt.stats.data.membership;

import com.potrt.stats.data.club.Club;
import com.potrt.stats.data.person.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A {@link Membership} represents a {@link Person}'s connection to a {@link Club}. */
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(PersonClub.class)
public class Membership {
  @Id
  @Column(nullable = false)
  private Integer personId;

  @Id
  @Column(nullable = false)
  private Integer clubId;

  @Column(nullable = true)
  private String personRole;

  @Column(nullable = false)
  private Integer cashBalance;

  @Column(nullable = true)
  private String firstName;

  @Column(nullable = true)
  private String lastName;

  @Column(nullable = true)
  private String nickname;

  /**
   * Returns a duplicate without data that a basic member cannot see.
   *
   * @return The masked duplicate.
   */
  public Membership mask() {
    return new Membership(personId, clubId, personRole, null, firstName, lastName, nickname);
  }
}
