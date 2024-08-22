/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.verification;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Verification {
  @Id private String token;
  private Integer personID;
  private Timestamp expirationDate;
}