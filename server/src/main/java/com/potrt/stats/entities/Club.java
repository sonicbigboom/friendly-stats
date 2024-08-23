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
  private String storedCash;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private Boolean isDeleted;

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class MaskedClub {
    private Integer id;
    private String name;
    private Integer ownerPersonID;

    public MaskedClub(Club club) {
      this.id = club.getId();
      this.name = club.getName();
      this.ownerPersonID = club.getOwnerPersonID();
    }
  }
}
