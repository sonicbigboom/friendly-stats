/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.entities.Person;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
  public Person getPerson() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Person.class.isAssignableFrom(principal.getClass())) {
      return (Person) principal;
    } else if (PersonPrincipal.class.isAssignableFrom(principal.getClass())) {
      PersonPrincipal personPrincipal = (PersonPrincipal) principal;
      return personPrincipal.getPerson();
    } else {
      return null;
    }
  }
}
