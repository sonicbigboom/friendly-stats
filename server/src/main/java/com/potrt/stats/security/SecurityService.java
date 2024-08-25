/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.entities.Person;
import com.potrt.stats.exceptions.UnauthenticatedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/** The {@link SecurityService} provides information on the authenticated {@link Person}. */
@Service
public class SecurityService {

  /**
   * Gets the authenticated {@link Person}.
   *
   * @return The authenticated {@link Person}.
   * @throws UnauthenticatedException Thrown when no {@link Person} is authenticated.
   */
  public Person getPerson() throws UnauthenticatedException {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Person.class.isAssignableFrom(principal.getClass())) {
      return (Person) principal;
    } else if (PersonPrincipal.class.isAssignableFrom(principal.getClass())) {
      PersonPrincipal personPrincipal = (PersonPrincipal) principal;
      return personPrincipal.getPerson();
    } else {
      throw new UnauthenticatedException();
    }
  }

  /**
   * Gets the authenticated {@link Person}'s id.
   *
   * @return The authenticated {@link Person}'s id.
   * @throws UnauthenticatedException Thrown when no {@link Person} is authenticated.
   */
  public Integer getPersonID() throws UnauthenticatedException {
    return getPerson().getId();
  }
}
