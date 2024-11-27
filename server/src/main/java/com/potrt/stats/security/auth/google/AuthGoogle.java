/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.google;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A {@link AuthGoogle} represents the connection from a Google account to a application person
 * account.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthGoogle {
  @Id private String authId;
  private Integer personId;
}
