/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(Customizer.withDefaults())
        .authorizeHttpRequests(
            chain -> chain.requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
        .formLogin(chain -> chain.loginPage("/auth/login"));

    return http.build();
  }
}
