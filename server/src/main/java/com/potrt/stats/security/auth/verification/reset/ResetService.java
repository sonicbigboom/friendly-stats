/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.verification.reset;

import com.potrt.stats.entities.Person;
import com.potrt.stats.exceptions.BadExternalCommunicationException;
import com.potrt.stats.exceptions.PersonDoesNotExistException;
import com.potrt.stats.security.auth.exceptions.TokenDoesNotExistException;
import com.potrt.stats.security.auth.exceptions.TokenExpiredException;
import com.potrt.stats.security.auth.verification.VerificationService;
import com.potrt.stats.services.EmailService;
import com.potrt.stats.services.PersonService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

/**
 * A {@link ResetService} is a service that sends reset emails and reset {@link Person} credentials.
 */
@Service
public class ResetService {

  private static final int EXPIRATION_TIME_IN_MILLISECONDS = 10 * 60 * 1000;

  private EmailService emailService;
  private ResetRepository resetRepository;
  private PersonService personService;

  /** Autowires a {@link ResetService}. */
  @Autowired
  public ResetService(
      EmailService emailService, ResetRepository resetRepository, PersonService personService) {
    this.emailService = emailService;
    this.resetRepository = resetRepository;
    this.personService = personService;
  }

  /**
   * Sends a reset email.
   *
   * @param person The {@link Person} who is resetting their credentials.
   * @throws BadExternalCommunicationException Thrown when failing to send the reset email.
   */
  public void sendResetEmail(Person person) throws BadExternalCommunicationException {
    String token = createResetToken(person);

    String recipientAddress = person.getEmail();
    String subject = "Reset Credentials";

    String message =
        "Hello "
            + person.getUsername()
            + "!\n\nTo reset your account, use the following code: \""
            + token
            + "\"";

    try {
      emailService.sendMessage(recipientAddress, subject, message);
    } catch (MessagingException | UnsupportedEncodingException | MailException e) {
      throw new BadExternalCommunicationException(e);
    }
  }

  /**
   * Verifies an account reset token.
   *
   * @param token The token.
   * @throws TokenDoesNotExistException Thrown if the token does not exists.
   * @throws TokenExpiredException Thrown if the token is expired.
   */
  @Transactional
  public Person checkToken(String email, String token)
      throws PersonDoesNotExistException, TokenDoesNotExistException, TokenExpiredException {

    Person person = personService.getPerson(email);

    Optional<Reset> optionalReset = resetRepository.findById(token);
    if (optionalReset.isEmpty()) {
      throw new TokenDoesNotExistException();
    }
    Reset reset = optionalReset.get();
    if (reset.getExpirationDate().getTime() < (new Date()).getTime()) {
      throw new TokenExpiredException();
    }

    if (!person.getId().equals(reset.getPersonID())) {
      throw new TokenDoesNotExistException();
    }

    return person;
  }

  /**
   * Creates and saves {@link Reset} token for a {@link Person}.
   *
   * @param person The {@link Person}.
   */
  private String createResetToken(Person person) {
    String token = UUID.randomUUID().toString();
    Reset reset =
        new Reset(
            token,
            person.getId(),
            VerificationService.calculateExpirationTime(EXPIRATION_TIME_IN_MILLISECONDS));
    resetRepository.save(reset);
    return token;
  }
}
