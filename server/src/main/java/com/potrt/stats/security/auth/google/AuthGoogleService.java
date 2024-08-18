/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.google;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.RegisterDto;
import org.springframework.stereotype.Service;

@Service
public class AuthGoogleService implements AuthService {

  @Override
  public Person registerPerson(RegisterDto registerDto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'registerPerson'");
  }
}
