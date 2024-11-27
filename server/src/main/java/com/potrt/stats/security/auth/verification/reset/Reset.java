/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.verification.reset;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Represents a reset token for a {@link Person}. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reset {
  @Id private String token;
  private Integer personId;
  private Timestamp expirationDate;
}
