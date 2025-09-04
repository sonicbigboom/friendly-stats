/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.verification;

import com.potrt.stats.data.person.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Represents a verifcation token for a new {@link Person} account. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Verification {
  @Id private String token;
  private Integer personId;
  private Timestamp expirationDate;
}
