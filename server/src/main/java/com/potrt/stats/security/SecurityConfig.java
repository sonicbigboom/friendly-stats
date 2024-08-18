/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.repositories.PersonRepository;
import com.potrt.stats.security.auth.google.AuthGoogleAuthenticationFilter;
import com.potrt.stats.security.auth.google.AuthGoogleAuthenticationProvider;
import com.potrt.stats.security.auth.google.AuthGoogleRepository;
import com.potrt.stats.security.auth.local.AuthLocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

  AuthLocalService authLocalService;
  PersonRepository personRepository;
  AuthGoogleRepository authGoogleRepository;

  @Autowired
  public SecurityConfig(
      AuthLocalService authLocalService,
      PersonRepository personRepository,
      AuthGoogleRepository authGoogleRepository) {
    this.authLocalService = authLocalService;
    this.personRepository = personRepository;
    this.authGoogleRepository = authGoogleRepository;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    http.csrf(chain -> chain.disable())
        .authorizeHttpRequests(
            chain -> chain.requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
        .formLogin(
            chain ->
                chain
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/auth/login")
                    .defaultSuccessUrl("/me")
                    .failureUrl("/auth/login?error=true"))
        .logout(logout -> logout.logoutUrl("/auth/logout"))
        .addFilterAfter(
            new AuthGoogleAuthenticationFilter(authenticationManager),
            BasicAuthenticationFilter.class)
        .authenticationManager(authenticationManager);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(authLocalService);

    AuthGoogleAuthenticationProvider authGoogleProvider =
        new AuthGoogleAuthenticationProvider(authGoogleRepository, personRepository);

    return new ProviderManager(authProvider, authGoogleProvider);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
          @Override
          public void addCorsMappings(CorsRegistry registry) {
              registry.addMapping("/**");
          }
      };
  }
}
