/* Copywrite (c) 2024 */
package com.potrt.stats.entities;

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
@IdClass(com.potrt.stats.entities.Membership.PersonClub.class)
public class Membership {
  @Id
  @Column(nullable = false)
  private Integer personID;

  @Id
  @Column(nullable = false)
  private Integer clubID;

  @Column(nullable = false)
  private Integer cashBalance;

  @Column(nullable = true)
  private String personRole;

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PersonClub {
    private Integer personID;
    private Integer clubID;
  }
}
