/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthLocalService implements UserDetailsService {

  private AuthLocalRepository authLocalRepository;

  @Autowired
  public AuthLocalService(AuthLocalRepository authLocalRepository) {
    this.authLocalRepository = authLocalRepository;
  }

  @Override
  public AuthLocalPrincipal loadUserByUsername(String email) {
    AuthLocal authLocal = authLocalRepository.findByEmail(email);
    if (authLocal == null) {
      throw new UsernameNotFoundException(email);
    }
    return new AuthLocalPrincipal(authLocal);
  }
}
