/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.verification.reset;

import com.potrt.stats.entities.Person;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.security.auth.exceptions.VerificationDoesNotExistException;
import com.potrt.stats.security.auth.exceptions.VerificationExpiredException;
import com.potrt.stats.services.EmailService;
import com.potrt.stats.services.PersonService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetService {

  private static final int EXPIRATION_TIME_IN_MINUTES = 10;

  private EmailService emailService;
  private ResetRepository resetRepository;
  private PersonService personService;

  @Autowired
  public ResetService(
      EmailService emailService, ResetRepository resetRepository, PersonService personService) {
    this.emailService = emailService;
    this.resetRepository = resetRepository;
    this.personService = personService;
  }

  public void sendResetEmail(Person person)
      throws MessagingException, UnsupportedEncodingException {
    String token = UUID.randomUUID().toString();
    createResetToken(person, token);

    String recipientAddress = person.getEmail();
    String subject = "Reset Credentials";

    String message =
        "Hello "
            + person.getUsername()
            + "!\n\nTo reset your account, use the following code: \""
            + token
            + "\"";

    emailService.sendMessage(recipientAddress, subject, message);
  }

  @Transactional
  public Person checkToken(String email, String token)
      throws PersonDoesNotExistException,
          VerificationDoesNotExistException,
          VerificationExpiredException {

    Person person = personService.getPerson(email);

    Optional<Reset> optionalReset = resetRepository.findById(token);
    if (optionalReset.isEmpty()) {
      throw new VerificationDoesNotExistException();
    }
    Reset reset = optionalReset.get();
    if (reset.getExpirationDate().getTime() < (new Date()).getTime()) {
      throw new VerificationExpiredException();
    }

    if (!person.getId().equals(reset.getPersonID())) {
      throw new VerificationDoesNotExistException();
    }

    return person;
  }

  private void createResetToken(Person person, String token) {
    Reset reset = new Reset(token, person.getId(), calculateExpirationTime());
    resetRepository.save(reset);
  }

  private Timestamp calculateExpirationTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Timestamp(cal.getTime().getTime()));
    cal.add(Calendar.MINUTE, EXPIRATION_TIME_IN_MINUTES);
    return new Timestamp(cal.getTimeInMillis());
  }
}
