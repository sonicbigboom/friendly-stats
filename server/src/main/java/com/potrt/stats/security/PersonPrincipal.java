/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.entities.Person;

public interface PersonPrincipal {
  public Person getPerson();

  public default Integer getId() {
    return getPerson().getId();
  }
}
