/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.verification;

import com.potrt.stats.entities.Person;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class VerificationService {

  private static final int EXPIRATION_TIME_IN_MINUTES = 60 * 24;

  private EmailService emailService;
  private VerificationRepository verificationRepository;
  private PersonService personService;

  @Autowired
  public VerificationService(
      EmailService emailService,
      VerificationRepository verificationRepository,
      PersonService personService) {
    this.emailService = emailService;
    this.verificationRepository = verificationRepository;
    this.personService = personService;
  }

  public void sendVerificationEmail(Person person, String verificationUrl)
      throws MessagingException, UnsupportedEncodingException {
    String token = UUID.randomUUID().toString();
    createVerificationToken(person, token);

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

    emailService.sendMessage(recipientAddress, subject, message);
  }

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

  private void createVerificationToken(Person person, String token) {
    Verification verification = new Verification(token, person.getId(), calculateExpirationTime());
    verificationRepository.save(verification);
  }

  private Timestamp calculateExpirationTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Timestamp(cal.getTime().getTime()));
    cal.add(Calendar.MINUTE, EXPIRATION_TIME_IN_MINUTES);
    return new Timestamp(cal.getTimeInMillis());
  }
}
