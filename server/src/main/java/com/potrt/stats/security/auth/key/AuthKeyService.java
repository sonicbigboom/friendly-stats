/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.key;

import com.potrt.stats.entities.Person;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthKeyService {
  private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
  private static final String AUTH_TOKEN = "Friendly Stats";

  public Authentication getAuthentication(HttpServletRequest request) {
    String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
    if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
      throw new BadCredentialsException("Invalid API Key");
    }

    return new AuthKeyAuthentication(
        apiKey,
        new Person(1, "friendly_stats", "friendly-stats@potrt.com", "Friendly", "Stats", null));
  }
}
