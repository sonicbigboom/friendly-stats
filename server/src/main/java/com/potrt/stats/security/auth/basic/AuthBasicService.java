/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.basic;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.AuthType;
import com.potrt.stats.security.auth.LoginDto;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import com.potrt.stats.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/** The {@link AuthService} for the username + password (basic) {@link AuthType}. */
@Service
public class AuthBasicService implements AuthService {

  private PersonService personService;
  private AuthBasicPasswordRepository authBasicPasswordRepository;
  private AuthenticationManager authenticationManager;

  /** Autowires the {@link AuthBasicService}. */
  @Autowired
  public AuthBasicService(
      AuthBasicPasswordRepository authBasicPasswordRepository,
      PersonService personService,
      AuthenticationManager authenticationManager) {
    this.personService = personService;
    this.authBasicPasswordRepository = authBasicPasswordRepository;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Person register(RegisterDto registerDto)
      throws EmailAlreadyExistsException, UsernameAlreadyExistsException {
    Person person = personService.register(registerDto);
    String encodedPassword =
        "{bcrypt}" + new BCryptPasswordEncoder(14).encode(registerDto.getCode());
    AuthBasicPassword authBasicPassword = new AuthBasicPassword(person.getId(), encodedPassword);
    authBasicPasswordRepository.save(authBasicPassword);
    return person;
  }

  @Override
  public Authentication login(@Valid LoginDto loginDto)
      throws BadCredentialsException, DisabledException {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getLoginName(), loginDto.getCode()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return authentication;
  }

  @Override
  public void updateCredentials(Integer personID, String code) {
    String encodedPassword = "{bcrypt}" + new BCryptPasswordEncoder(14).encode(code);
    AuthBasicPassword authBasicPassword = new AuthBasicPassword(personID, encodedPassword);
    authBasicPasswordRepository.save(authBasicPassword);
  }
}
