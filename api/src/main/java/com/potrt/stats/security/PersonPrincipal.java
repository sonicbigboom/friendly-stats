/* Copyright (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.data.person.Person;
import org.springframework.security.core.Authentication;

/** A {@link PersonPrincipal} represents an {@link Authentication} for a {@link Person}. */
public interface PersonPrincipal {

  /**
   * Gets the authenticated {@link Person} contained in the principal.
   *
   * @return The authenticated {@link Person}.
   */
  public Person getPerson();

  /**
   * Gets the id of the authenticated {@link Person}.
   *
   * @return The id of the authenticated {@link Person}.
   */
  public default Integer getId() {
    return getPerson().getId();
  }
}
