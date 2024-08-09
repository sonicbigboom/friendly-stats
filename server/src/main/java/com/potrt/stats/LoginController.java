package com.potrt.stats;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.google.gson.JsonParser;

@RestController
public class LoginController {
  @RequestMapping("/login")
  public String login() {
    return "<a href=\"https://accounts.google.com/o/oauth2/v2/auth?"
            + "redirect_uri=http://localhost:8080/grantcode"
            + "&response_type=code&client_id=" + System.getenv("FRIENDLY_STATS_GOOGLE_CLIENT_ID")
            + "&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email" 
              + "+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile" 
              + "+openid"
            + "&access_type=offline\">"
            + "Sign in with Google"
            + "</a>";
  }  

  @GetMapping("/grantcode")
  public String grantCode(@RequestParam("code") String code) {
    return getGoogleProfileDetails(getGoogleOauthAccessToken(code));
  }

  private String getGoogleOauthAccessToken(String code) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", code);
    params.add("redirect_uri", "http://localhost:8080/grantcode");
    params.add("client_id", System.getenv("FRIENDLY_STATS_GOOGLE_CLIENT_ID"));
    params.add("client_secret", System.getenv("FRIENDLY_STATS_GOOGLE_CLIENT_SECRET"));
    params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
    params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
    params.add("scope", "openid");
    params.add("grant_type", "authorization_code");

    String url = "https://oauth2.googleapis.com/token";
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);
    String response = new RestTemplate().postForObject(url, requestEntity, String.class);

    return JsonParser.parseString(response).getAsJsonObject().get("access_token").getAsString();
  }

  private String getGoogleProfileDetails(String accessToken) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBearerAuth(accessToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

    String url = "https://www.googleapis.com/oauth2/v2/userinfo";
    ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
    return response.getBody();
  }
}
