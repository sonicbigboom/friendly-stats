/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.jwt;

import com.potrt.stats.entities.Person;
import com.potrt.stats.exceptions.UnauthenticatedException;
import com.potrt.stats.security.PersonPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {

  private String jwtSecret = System.getenv("FRIENDLY_STATS_SIGNATURE");
  private long jwtExpirationDate = 3600000;

  // generate JWT token
  public String generateToken(Authentication authentication) throws UnauthenticatedException {

    Person person;
    Object principal = authentication.getPrincipal();
    if (Person.class.isAssignableFrom(principal.getClass())) {
      person = (Person) principal;
    } else if (PersonPrincipal.class.isAssignableFrom(principal.getClass())) {
      PersonPrincipal personPrincipal = (PersonPrincipal) principal;
      person = personPrincipal.getPerson();
    } else {
      throw new UnauthenticatedException();
    }
    Integer id = person.getId();

    Date currentDate = new Date();

    Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

    return Jwts.builder()
        .subject(id.toString())
        .issuedAt(new Date())
        .expiration(expireDate)
        .signWith(key())
        .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  // get username from JWT token
  public Integer getId(String token) {

    return Integer.parseInt(
        Jwts.parser()
            .verifyWith((SecretKey) key())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject());
  }

  // validate JWT token
  public boolean validateToken(String token) {
    Jwts.parser().verifyWith((SecretKey) key()).build().parse(token);
    return true;
  }
}
