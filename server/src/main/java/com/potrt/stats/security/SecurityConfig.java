/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.security.auth.basic.AuthBasicService;
import com.potrt.stats.security.auth.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The {@link SecurityConfig} sets the configuration for security and access to endpoints.
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

  private AuthBasicService authBasicService;
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * Autowires the security config.
   */
  @Autowired
  public SecurityConfig(
      AuthBasicService authBasicService, JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.authBasicService = authBasicService;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  /**
   * Configures the security filter chain.
   * @return The configured {@link SecurityFilterChain}.
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        httpSecurity.csrf(chain -> chain.disable())
        .authorizeHttpRequests(
            chain -> chain.requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
        .httpBasic(chain -> chain.disable())
        .formLogin(chain -> chain.disable())
        .sessionManagement(chain -> chain.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authenticationManager(authenticationManager);

    return httpSecurity.build();
  }

  /**
   * Creates the {@link ProviderManager} that is used for authentication.
   * @return The configured {@link ProviderManager}.
   */
  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(authBasicService);

    return new ProviderManager(authProvider);
  }

  /**
   * Configures CORS for web security.
   * @return The configured {@link WebMvcConfigurer}.
   */
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
