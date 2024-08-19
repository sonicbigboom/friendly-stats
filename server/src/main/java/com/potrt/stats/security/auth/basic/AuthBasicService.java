/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.basic;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.LoginDto;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthBasicService implements UserDetailsService, AuthService {

  private AuthBasicRepository authBasicRepository;
  private PersonService personService;
  private AuthBasicPasswordRepository authBasicPasswordRepository;

  @Autowired
  public AuthBasicService(
      AuthBasicRepository authBasicRepository,
      AuthBasicPasswordRepository authBasicPasswordRepository,
      PersonService personService) {
    this.authBasicRepository = authBasicRepository;
    this.personService = personService;
    this.authBasicPasswordRepository = authBasicPasswordRepository;
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

  @Override
  public Person registerPerson(RegisterDto registerDto) {
    Person person = registerDto.getPerson();
    person = personService.register(person);
    String encodedPassword =
        "{bcrypt}" + new BCryptPasswordEncoder(14).encode(registerDto.getCode());
    AuthBasicPassword authBasicPassword = new AuthBasicPassword(person.getId(), encodedPassword);
    authBasicPasswordRepository.save(authBasicPassword);
    return person;
  }

  @Override
  public Authentication login(
      @Valid LoginDto loginDto, AuthenticationManager authenticationManager) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getLoginName(), loginDto.getCode()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return authentication;
  }
}
