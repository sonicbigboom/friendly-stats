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

@Service
public class EmailService {

  private JavaMailSender emailSender;

  @Autowired
  public EmailService(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  public void sendMessage(String to, String subject, String text)
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
