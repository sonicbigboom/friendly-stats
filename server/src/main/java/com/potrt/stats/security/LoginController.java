/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
  @RequestMapping("/auth/login")
  public String login() {
    return "<a href=\"https://accounts.google.com/o/oauth2/v2/auth?"
        + "redirect_uri=http://localhost:8080/auth/google/grantcode"
        + "&response_type=code&client_id="
        + System.getenv("FRIENDLY_STATS_GOOGLE_CLIENT_ID")
        + "&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email"
        + "+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile"
        + "+openid"
        + "&access_type=offline\">"
        + "Sign in with Google"
        + "</a>";
  }

  @GetMapping("/auth/google/grantcode")
  public String grantCode(@RequestParam("code") String code) {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
