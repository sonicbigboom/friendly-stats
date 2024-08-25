/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.basic;

import com.potrt.stats.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthBasicUserDetailsServiceImpl implements UserDetailsService {

  private AuthBasicRepository authBasicRepository;
  private PersonService personService;

  @Autowired
  public AuthBasicUserDetailsServiceImpl(
      AuthBasicRepository authBasicRepository, PersonService personService) {
    this.authBasicRepository = authBasicRepository;
    this.personService = personService;
  }

  @Override
  public AuthBasicPrincipal loadUserByUsername(String loginName) {
    AuthBasic authBasic = authBasicRepository.findByUsername(loginName);
    if (authBasic == null) {
      authBasic = authBasicRepository.findByEmail(loginName);
    }
    if (authBasic == null) {
      throw new UsernameNotFoundException(loginName);
    }

    authBasic.setPerson(personService.getPerson(authBasic.getPersonID()));
    return new AuthBasicPrincipal(authBasic);
  }
}
