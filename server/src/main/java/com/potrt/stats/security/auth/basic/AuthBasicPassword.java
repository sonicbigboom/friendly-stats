/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.basic;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** An {@AuthBasicPassword} represent a SQL table linking a {@link Person} id with a password. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthBasicPassword {
  @Id private Integer personID;
  private String password;
}
