/* Copywrite (c) 2024 */
package com.potrt.stats.entities.masked;

import com.potrt.stats.entities.Club;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MaskedClub {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer ownerPersonID;

  public MaskedClub(Club club) {
    this.id = club.getId();
    this.name = club.getName();
    this.ownerPersonID = club.getOwnerPersonID();
  }
}
