/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.services.EmailService;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

  private EmailService emailService;

  @Autowired
  public LoginController(EmailService emailService) {
    this.emailService = emailService;
  }

  @GetMapping("/auth/login")
  public String login() {
    return "login";
  }

  @GetMapping("/auth/google/grantcode")
  public void grantCode(@RequestParam("code") String code) {
    /* Creates endpoint. */
  }

  @ResponseBody
  @GetMapping("/auth/test/email")
  public String testEmail() {
    try {
      emailService.sendMessage("", "Test Email", "Text");
      return "Sent email!";
    } catch (MessagingException | UnsupportedEncodingException e) {
      return "Error: " + e.getMessage();
    }
  }
}
