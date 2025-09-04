/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.basic;

import org.springframework.data.repository.CrudRepository;

/** A {@link CrudRepository} interface for a {@link AuthBasic}. */
public interface AuthBasicRepository extends CrudRepository<AuthBasic, String> {

  /**
   * Gets an {@link AuthBasic} by email.
   *
   * @param email The {@link Person}'s email.
   * @return The {@link AuthBasic} for the {@link Person}.
   */
  AuthBasic findByEmail(String email);

  /**
   * Gets an {@link AuthBasic} by username.
   *
   * @param username The {@link Person}'s username.
   * @return The {@link AuthBasic} for the {@link Person}.
   */
  AuthBasic findByUsername(String username);
}
