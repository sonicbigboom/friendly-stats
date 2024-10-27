/* Copyright (c) 2024 */
package com.potrt.stats.data.gamerecord;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;

/** A {@link GameRecord} represents a record in a game. */
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false)
  private Integer gameID;

  @Column(nullable = false)
  private Integer personID;

  @Column(nullable = false)
  private Integer scoreChange;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private boolean isDeleted;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTime;

  @Column(nullable = false)
  private Integer createdByPersonID;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date modifiedTime;

  @Column(nullable = false)
  private Integer modifiedByPersonID;

  /** A {@link MaskedGameRecord} with private information hidden. */
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class MaskedGameRecord {
    private Integer id;
    private Integer gameID;
    private Integer personID;
    private Integer scoreChange;
    private Date createdTime;
    private Integer createdByPersonID;
    private Date modifiedTime;
    private Integer modifiedByPersonID;

    /** A {@link MaskedGameRecord} with public information. */
    public MaskedGameRecord(GameRecord gameRecord) {
      this.id = gameRecord.id;
      this.gameID = gameRecord.gameID;
      this.personID = gameRecord.personID;
      this.scoreChange = gameRecord.scoreChange;
      this.createdTime = gameRecord.createdTime;
      this.createdByPersonID = gameRecord.createdByPersonID;
      this.modifiedTime = gameRecord.modifiedTime;
      this.modifiedByPersonID = gameRecord.modifiedByPersonID;
    }
  }
}
