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
  private Integer personID;

  @Id
  @Column(nullable = false)
  private Integer clubID;

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

  /** A {@link Person} with private information hidden. */
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class MaskedMembership {
    private Integer personID;
    private Integer clubID;
    private String personRole;
    private Integer cashBalance;
    private String firstName;
    private String lastName;
    private String nickname;

    /**
     * A {@link MaskedMembership} with only public information.
     *
     * @param membership The {@link Membership} to mask.
     */
    public MaskedMembership(Membership membership) {
      this(membership, false);
    }

    /**
     * A {@link MaskedMembership} with public information and potentially sensitive info.
     *
     * @param membership The {@link Membership} to mask.
     * @param includeSensitive Whether the {@code storedCash} is included.
     */
    public MaskedMembership(Membership membership, boolean includeSensitive) {
      this.personID = membership.personID;
      this.clubID = membership.clubID;
      this.personRole = membership.personRole;
      this.firstName = membership.firstName;
      this.lastName = membership.lastName;
      this.nickname = membership.nickname;

      if (includeSensitive) {
        this.cashBalance = membership.cashBalance;
      }
    }
  }
}
