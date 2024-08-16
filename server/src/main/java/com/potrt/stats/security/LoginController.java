/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.entities.Person;
import com.potrt.stats.security.auth.local.AuthLocalPasswordDto;
import com.potrt.stats.security.auth.local.AuthLocalService;
import com.potrt.stats.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

  private SecurityService securityService;
  private AuthLocalService authLocalService;
  private EmailService emailService;

  @Autowired
  public LoginController(
      SecurityService securityService,
      AuthLocalService authLocalService,
      EmailService emailService) {
    this.securityService = securityService;
    this.authLocalService = authLocalService;
    this.emailService = emailService;
  }

  @GetMapping("/auth/login")
  public String login() {
    return "login";
  }

  @GetMapping("/auth/register")
  public String register(Model model) {
    model.addAttribute("person", new PersonDto());
    model.addAttribute("local", new AuthLocalPasswordDto());
    return "register";
  }

  @PostMapping("/auth/register")
  public ModelAndView registerUserAccount(
      ModelAndView mav,
      @ModelAttribute("person") @Valid PersonDto personDto,
      @ModelAttribute("local") @Valid AuthLocalPasswordDto authLocalPasswordDto,
      HttpServletRequest request,
      Errors errors) {

    try {
      Person person = authLocalService.registerNewPerson(personDto, authLocalPasswordDto);
      return new ModelAndView("registerSuccess", "person", person);
    } catch (Exception exception) {
      mav.addObject("message", "An account for that username/email already exists.");
      return mav;
    }
  }

  @ResponseBody
  @GetMapping("/auth/google/grantcode")
  public Person grantCode(@RequestParam("code") String code) {
    return securityService.getPerson();
  }

  @ResponseBody
  @GetMapping("/auth/test/person")
  public Person testPerson() {
    return securityService.getPerson();
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

  @ResponseBody
  @GetMapping("/")
  public Person authenticatedTest() {
    return securityService.getPerson();
  }
}
