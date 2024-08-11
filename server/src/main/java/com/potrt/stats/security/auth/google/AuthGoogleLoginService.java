/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.google;

import com.google.gson.JsonParser;
import com.potrt.stats.entities.Person;
import com.potrt.stats.repositories.PersonRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/** The service for google authentication. */
@Service
public class AuthGoogleLoginService {

  private AuthGoogleRepository googleRepository;
  private PersonRepository personRepository;

  @Autowired
  public AuthGoogleLoginService(
      AuthGoogleRepository googleRepository, PersonRepository personRepository) {
    this.googleRepository = googleRepository;
    this.personRepository = personRepository;
  }

  /**
   * Gets the access token from a code provided by Google's oAuth.
   *
   * @param code The Google oAuth code.
   * @return The access token.
   */
  public String getAccessToken(String code) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", code);
    params.add("redirect_uri", "http://localhost:8080/auth/google/grantcode");
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

  /**
   * Gets the {@link Person} from a Google access token.
   *
   * @param accessToken The Google access token.
   * @return The {@link Person}.
   */
  public Person getPerson(String accessToken) {
    String info = getProfileInfo(accessToken);
    String googleID = JsonParser.parseString(info).getAsJsonObject().get("id").getAsString();
    Optional<AuthGoogle> optionalID = googleRepository.findById(googleID);
    if (optionalID.isEmpty()) {
      return null;
    }

    Integer id = optionalID.get().getPersonID();
    if (id == null) {
      return null;
    }

    return personRepository.findById(id).orElseGet(() -> null);
  }

  /**
   * Gets the profile info for the given Google access token.
   *
   * @param accessToken The Google access token.
   * @return The JSON profile info.
   */
  private String getProfileInfo(String accessToken) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBearerAuth(accessToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

    String url = "https://www.googleapis.com/oauth2/v2/userinfo";
    ResponseEntity<String> response =
        new RestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
    return response.getBody();
  }
}
