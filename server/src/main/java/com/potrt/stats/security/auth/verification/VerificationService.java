/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.verification;

import com.potrt.stats.entities.Person;
import com.potrt.stats.exceptions.BadExternalCommunicationException;
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
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * A {@link VerificationService} is a service that sends verification emails and verifies accounts.
 */
@Service
public class VerificationService {

  private static final int EXPIRATION_TIME_IN_MINUTES = 60 * 24;

  private EmailService emailService;
  private VerificationRepository verificationRepository;
  private PersonService personService;

  /** Autowires a {@link VerificationService} */
  @Autowired
  public VerificationService(
      EmailService emailService,
      VerificationRepository verificationRepository,
      PersonService personService) {
    this.emailService = emailService;
    this.verificationRepository = verificationRepository;
    this.personService = personService;
  }

  /**
   * Sends a verification email.
   *
   * @param person The {@link Person} to verify.
   * @param verificationUrl The url (when combined with the token) that the {@link Person} will
   *     verify with. If empty, will direct to endpoint.
   * @throws BadExternalCommunicationException Failure to send the verification email.
   */
  public void sendVerificationEmail(Person person, String verificationUrl)
      throws BadExternalCommunicationException {
    String token = createVerificationToken(person);

    String recipientAddress = person.getEmail();
    String subject = "Email Verification";

    String verificationBaseUrl;

    if (verificationUrl != null) {
      verificationBaseUrl = verificationUrl;
    } else {
      String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
      verificationBaseUrl = baseUrl + "/auth/verify?token=";
    }

    String confirmationUrl = verificationBaseUrl + token;
    String message =
        "Hello "
            + person.getUsername()
            + "!\n\nPlease verifying your account with the following link:\n\n"
            + confirmationUrl;

    try {
      emailService.sendMessage(recipientAddress, subject, message);
    } catch (MessagingException | UnsupportedEncodingException | MailException e) {
      throw new BadExternalCommunicationException(e);
    }
  }

  /**
   * Verifies an account verification token.
   *
   * @param token The token.
   * @throws VerificationDoesNotExistException Thrown if the token does not exists.
   * @throws VerificationExpiredException Thrown if the token is expired.
   */
  @Transactional
  public void verify(String token)
      throws VerificationDoesNotExistException, VerificationExpiredException {
    Optional<Verification> optionalVerification = verificationRepository.findById(token);
    if (optionalVerification.isEmpty()) {
      throw new VerificationDoesNotExistException();
    }
    Verification verification = optionalVerification.get();
    if (verification.getExpirationDate().getTime() < (new Date()).getTime()) {
      throw new VerificationExpiredException();
    }
    personService.enable(verification.getPersonID());
  }

  /**
   * Creates and saves {@link Verification} token for a {@link Person}.
   *
   * @param person The {@link Person}.
   */
  private String createVerificationToken(Person person) {
    String token = UUID.randomUUID().toString();
    Verification verification = new Verification(token, person.getId(), calculateExpirationTime());
    verificationRepository.save(verification);
    return token;
  }

  /**
   * Calculates the expiration time of new verification token.
   *
   * @return The expiration {@link Timestamp} of a new verification token.
   */
  private Timestamp calculateExpirationTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Timestamp(cal.getTime().getTime()));
    cal.add(Calendar.MINUTE, EXPIRATION_TIME_IN_MINUTES);
    return new Timestamp(cal.getTimeInMillis());
  }
}
