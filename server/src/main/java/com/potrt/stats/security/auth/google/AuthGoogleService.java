/* Copyright (c) 2024 */
package com.potrt.stats.security.auth.google;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.potrt.stats.data.person.Person;
import com.potrt.stats.data.person.PersonService;
import com.potrt.stats.exceptions.BadExternalCommunicationException;
import com.potrt.stats.exceptions.ImpossibleRuntimeException;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.LoginDto;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import jakarta.validation.Valid;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/** The {@link AuthService} for google authentication. */
@Service
public class AuthGoogleService implements AuthService {

  private AuthGoogleRepository authGoogleRepository;
  private PersonService personService;

  /** Autowires an {@link AuthGoogleService}. */
  @Autowired
  public AuthGoogleService(AuthGoogleRepository authGoogleRepository, PersonService personService) {
    this.authGoogleRepository = authGoogleRepository;
    this.personService = personService;
  }

  @Override
  public Person register(RegisterDto registerDto)
      throws BadCredentialsException,
          BadExternalCommunicationException,
          EmailAlreadyExistsException,
          UsernameAlreadyExistsException {
    String idToken = registerDto.getCode();
    String googleId = getGoogleId(idToken);

    Person person = personService.register(registerDto);

    AuthGoogle authGoogle = new AuthGoogle(googleId, person.getId());
    authGoogleRepository.save(authGoogle);
    return person;
  }

  @Override
  public Authentication login(@Valid LoginDto loginDto) throws BadCredentialsException {
    try {
      String idToken = loginDto.getCode();
      String googleId = getGoogleId(idToken);
      Person person = getPerson(googleId);

      AuthService.checkAccountStatus(person);

      return new AuthGoogleAuthentication(idToken, person);
    } catch (Exception e) {
      throw new BadCredentialsException("Invalid Google Key");
    }
  }

  /**
   * Gets a google id from an id token.
   *
   * @param idToken The id token.
   * @return The google id.
   * @throws BadExternalCommunicationException Thrown if communication with google failed.
   * @throws BadCredentialsException Thrown if google id token is invalid.
   */
  private String getGoogleId(String idToken)
      throws BadExternalCommunicationException, BadCredentialsException {
    String url = "https://oauth2.googleapis.com/tokeninfo";
    String urlTemplate =
        UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("id_token", idToken)
            .encode()
            .toUriString();
    HttpEntity<String> requestEntity = new HttpEntity<>(new HttpHeaders());
    ResponseEntity<String> response;
    try {
      response =
          new RestTemplate().exchange(urlTemplate, HttpMethod.GET, requestEntity, String.class);
    } catch (RestClientException e) {
      throw new BadExternalCommunicationException(e);
    }
    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new BadExternalCommunicationException();
    }

    String info = response.getBody();
    JsonObject jsonObject;
    try {
      jsonObject = JsonParser.parseString(info).getAsJsonObject();
    } catch (JsonParseException | IllegalStateException e) {
      throw new BadExternalCommunicationException(e);
    }

    String aud = jsonObject.get("aud").getAsString();
    if (!aud.equals(System.getenv("FRIENDLY_STATS_APP_GOOGLE_CLIENT_ID"))
        && !aud.equals(System.getenv("FRIENDLY_STATS_API_GOOGLE_CLIENT_ID"))) {
      throw new BadCredentialsException("Invalid client authenticator.");
    }

    String iss = jsonObject.get("iss").getAsString();
    if (!iss.equals("accounts.google.com") && !iss.equals("https://accounts.google.com")) {
      throw new BadCredentialsException("Wrong issuer.");
    }

    Date expirationDate = new Date(Long.parseLong(jsonObject.get("exp").getAsString()) * 1000);
    Date currentDate = new Date();
    if (!expirationDate.after(currentDate)) {
      throw new BadCredentialsException("Credential expired.");
    }

    return jsonObject.get("sub").getAsString();
  }

  /**
   * Gets a {@link Person} from a google id.
   *
   * @param googleId The google id.
   * @return The {@link Person} linked to the google id.
   * @throws BadCredentialsException Thrown if there is no {@link Person} linked to this google id.
   */
  private Person getPerson(String googleId) throws BadCredentialsException {
    Optional<AuthGoogle> optionalId = authGoogleRepository.findById(googleId);
    if (optionalId.isEmpty()) {
      throw new BadCredentialsException("Google token did not have an id.");
    }

    Integer id = optionalId.get().getPersonId();
    if (id == null) {
      throw new BadCredentialsException("No user with this google account.");
    }

    try {
      return personService.getPersonWithoutAuthCheck(id);
    } catch (PersonDoesNotExistException e) {
      throw new ImpossibleRuntimeException(e);
    }
  }

  @Override
  public void updateCredentials(Integer personId, String code)
      throws BadCredentialsException, BadExternalCommunicationException {
    String idToken = code;
    String googleId = getGoogleId(idToken);
    AuthGoogle authGoogle = new AuthGoogle(googleId, personId);
    authGoogleRepository.save(authGoogle);
  }
}
