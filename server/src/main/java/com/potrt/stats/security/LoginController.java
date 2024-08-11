/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.google.AuthGoogleLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  AuthGoogleLoginService authGoogleLoginService;

  @Autowired
  public LoginController(AuthGoogleLoginService authGoogleLoginService) {
    this.authGoogleLoginService = authGoogleLoginService;
  }

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
  public Person grantCode(@RequestParam("code") String code) {
    String out = code;
    String accessToken = authGoogleLoginService.getAccessToken(out);
    return authGoogleLoginService.getPerson(accessToken);
  }
}
