/* Copywrite (c) 2024 */
package com.potrt.stats.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * The {@link EmailService} is a service that provides the ability to send emails as the
 * application.
 */
@Service
public class EmailService {

  private JavaMailSender emailSender;

  /** Autowires a {@link EmailService}. */
  @Autowired
  public EmailService(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  /**
   * Sends an email as the application.
   *
   * @param to The email address of the recipient.
   * @param subject The subject line of the email.
   * @param text The text of the email.
   * @throws MessagingException Thrown if the email content creation fails.
   * @throws UnsupportedEncodingException Thrown if the email creation for the sender fails.
   * @throws MailException Thrown if the email failed to send.
   */
  public void sendEmail(String to, String subject, String text)
      throws MessagingException, UnsupportedEncodingException, MailException {
    MimeMessage message = emailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setFrom(
        new InternetAddress(System.getenv("FRIENDLY_STATS_EMAIL_ADDRESS"), "Friendly Stats"));
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(text);

    emailSender.send(message);
  }
}
