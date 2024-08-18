/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.security.auth.google.AuthGoogleFilter;
import com.potrt.stats.security.auth.google.AuthGoogleService;
import com.potrt.stats.security.auth.key.AuthKeyFilter;
import com.potrt.stats.security.auth.key.AuthKeyService;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

  private AuthKeyService authKeyService;
  private AuthLocalService authLocalService;
  private AuthGoogleService authGoogleService;

  @Autowired
  public SecurityConfig(
      AuthKeyService authKeyService,
      AuthLocalService authLocalService,
      AuthGoogleService authGoogleService) {
    this.authKeyService = authKeyService;
    this.authLocalService = authLocalService;
    this.authGoogleService = authGoogleService;
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
        .addFilterBefore(
            new AuthKeyFilter(authKeyService), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(
            new AuthGoogleFilter(authGoogleService), UsernamePasswordAuthenticationFilter.class)
        .authenticationManager(authenticationManager);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(authLocalService);

    return new ProviderManager(authProvider);
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
