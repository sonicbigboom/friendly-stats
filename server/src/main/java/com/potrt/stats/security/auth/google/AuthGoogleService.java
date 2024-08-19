/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.google;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.LoginDto;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.services.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/** The service for google authentication. */
@Service
public class AuthGoogleService implements AuthService {

  private AuthGoogleRepository authGoogleRepository;
  private PersonService personService;

  @Autowired
  public AuthGoogleService(AuthGoogleRepository authGoogleRepository, PersonService personService) {
    this.authGoogleRepository = authGoogleRepository;
    this.personService = personService;
  }

  public Authentication getAuthentication(HttpServletRequest request) {
    try {
      String code = request.getParameter("code");
      String accessToken = getAccessToken(code);
      Person person = getPerson(accessToken);
      return new AuthGoogleAuthentication(accessToken, person);
    } catch (Exception e) {
      throw new BadCredentialsException("Invalid Google Key");
    }
  }

  /**
   * Gets the access token from a code provided by Google's oAuth.
   *
   * @param code The Google oAuth code.
   * @return The access token.
   */
  private String getAccessToken(String code) {
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

    try {
      String response = new RestTemplate().postForObject(url, requestEntity, String.class);
      JsonElement jsonElement = JsonParser.parseString(response);
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      JsonElement accessToken = jsonObject.get("access_token");
      if (accessToken == null) {
        throw new BadCredentialsException("Google oAuth did not send back id.");
      }
      return accessToken.getAsString();
    } catch (RestClientException e) {
      throw new BadCredentialsException("Code was not a valid Google oAuth code.", e);
    } catch (JsonSyntaxException e) {
      throw new BadCredentialsException(
          "Google oAuth did not send back JSON for the given code.", e);
    }
  }

  /**
   * Gets the {@link Person} from a Google access token.
   *
   * @param accessToken The Google access token.
   * @return The {@link Person}.
   */
  public Person getPerson(String accessToken) {
    String googleID = getGoogleID(accessToken);
    Optional<AuthGoogle> optionalID = authGoogleRepository.findById(googleID);
    if (optionalID.isEmpty()) {
      throw new BadCredentialsException("Google token did not have an id.");
    }

    Integer id = optionalID.get().getPersonID();
    if (id == null) {
      throw new BadCredentialsException("No user with this google account.");
    }

    return personService.getPerson(id);
  }

  public String getGoogleID(String accessToken) {
    String info = getProfileInfo(accessToken);
    return JsonParser.parseString(info).getAsJsonObject().get("id").getAsString();
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

  @Override
  public Person registerPerson(RegisterDto registerDto) {
    String accessToken = getAccessToken(registerDto.getCode());
    String googleID = getGoogleID(accessToken);

    Person person = registerDto.getPerson();
    person = personService.register(person);

    AuthGoogle authGoogle = new AuthGoogle(googleID, person.getId());
    authGoogleRepository.save(authGoogle);
    return person;
  }

  @Override
  public Authentication login(
      @Valid LoginDto loginDto, AuthenticationManager authenticationManager) {
    try {
      String accessToken = getAccessToken(loginDto.getCode());
      Person person = getPerson(accessToken);
      return new AuthGoogleAuthentication(accessToken, person);
    } catch (Exception e) {
      throw new BadCredentialsException("Invalid Google Key");
    }
  }
}
