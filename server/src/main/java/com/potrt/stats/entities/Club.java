/* Copywrite (c) 2024 */
package com.potrt.stats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;

/** A {@link Club} represents a group in the application. */
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Club {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer ownerPersonID;

  @Column(nullable = false)
  private Integer storedCash;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private boolean isDeleted;

  /** A {@link Club} with private information hidden. */
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class MaskedClub {
    private Integer id;
    private String name;
    private Integer ownerPersonID;
    private Integer storedCash;

    /**
     * A {@link MaskedClub} with only public information.
     *
     * @param club The {@link Club} to mask.
     */
    public MaskedClub(Club club) {
      this(club, false);
    }

    /**
     * A {@link MaskedClub} with public information and potentially email.
     *
     * @param club The {@link Club} to mask.
     * @param showStoredCash Whether the stored cash is included, or masked.
     */
    public MaskedClub(Club club, boolean showStoredCash) {
      this.id = club.getId();
      this.name = club.getName();
      this.ownerPersonID = club.getOwnerPersonID();
      if (showStoredCash) {
        this.storedCash = club.storedCash;
      }
    }
  }
}
