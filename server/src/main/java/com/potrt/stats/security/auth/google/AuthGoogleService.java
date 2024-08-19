/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.google;

import com.google.gson.JsonParser;
import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.AuthService;
import com.potrt.stats.security.auth.LoginDto;
import com.potrt.stats.security.auth.RegisterDto;
import com.potrt.stats.services.PersonService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

  @Override
  public Person registerPerson(RegisterDto registerDto) {
    String idToken = registerDto.getCode();
    String googleID = getGoogleID(idToken);

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
      String idToken = loginDto.getCode();
      String googleID = getGoogleID(idToken);
      Person person = getPerson(googleID);
      return new AuthGoogleAuthentication(idToken, person);
    } catch (Exception e) {
      throw new BadCredentialsException("Invalid Google Key");
    }
  }

  private String getGoogleID(String idToken) {
    String url = "https://oauth2.googleapis.com/tokeninfo";
    String urlTemplate =
        UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("id_token", idToken)
            .encode()
            .toUriString();
    HttpEntity<String> requestEntity = new HttpEntity<>(new HttpHeaders());
    ResponseEntity<String> response =
        new RestTemplate().exchange(urlTemplate, HttpMethod.GET, requestEntity, String.class);
    String info = response.getBody();
    return JsonParser.parseString(info).getAsJsonObject().get("sub").getAsString();
  }

  private Person getPerson(String googleID) {
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
}
