/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.local;

import com.potrt.stats.entities.Person;
import com.potrt.stats.repositories.PersonRepository;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.LoginDto;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.security.auth.exceptions.PersonDoesNotExistException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Optional;
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
public class AuthLocalService implements UserDetailsService, AuthService {

  private AuthLocalRepository authLocalRepository;
  private PersonRepository personRepository;
  private AuthLocalPasswordRepository authLocalPasswordRepository;

  @Autowired
  public AuthLocalService(
      AuthLocalRepository authLocalRepository,
      AuthLocalPasswordRepository authLocalPasswordRepository,
      PersonRepository personRepository) {
    this.authLocalRepository = authLocalRepository;
    this.personRepository = personRepository;
    this.authLocalPasswordRepository = authLocalPasswordRepository;
  }

  @Override
  public AuthLocalPrincipal loadUserByUsername(String loginName) {
    AuthLocal authLocal = authLocalRepository.findByUsername(loginName);
    if (authLocal == null) {
      authLocal = authLocalRepository.findByEmail(loginName);
    }
    if (authLocal == null) {
      throw new UsernameNotFoundException(loginName);
    }

    Optional<Person> person = personRepository.findById(authLocal.getPersonID());
    if (person.isEmpty()) {
      throw new PersonDoesNotExistException(authLocal.getPersonID());
    }
    authLocal.setPerson(person.get());
    return new AuthLocalPrincipal(authLocal);
  }

  @Override
  @Transactional
  public Person registerPerson(RegisterDto registerDto) {
    Person person = registerDto.getPerson();
    person = personRepository.save(person);
    String encodedPassword =
        "{bcrypt}" + new BCryptPasswordEncoder(14).encode(registerDto.getCode());
    AuthLocalPassword authLocalPassword = new AuthLocalPassword(person.getId(), encodedPassword);
    authLocalPasswordRepository.save(authLocalPassword);
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
