/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.repositories.PersonRepository;
import com.potrt.stats.security.auth.google.AuthGoogleAuthenticationProvider;
import com.potrt.stats.security.auth.google.AuthGoogleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

  PersonRepository personRepository;
  AuthGoogleRepository authGoogleRepository;

  @Autowired
  public SecurityConfig(
      PersonRepository personRepository, AuthGoogleRepository authGoogleRepository) {
    this.personRepository = personRepository;
    this.authGoogleRepository = authGoogleRepository;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(chain -> chain.disable())
        .authorizeHttpRequests(
            chain -> chain.requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
        .formLogin(
            chain ->
                chain
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/auth/login")
                    .defaultSuccessUrl("/")
                    .failureUrl("/auth/login?error=true"))
        .logout((logout) -> logout.logoutUrl("/auth/logout"));

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    AuthGoogleAuthenticationProvider authGoogleProvider =
        new AuthGoogleAuthenticationProvider(authGoogleRepository, personRepository);
    return new ProviderManager(authGoogleProvider);
  }
}
