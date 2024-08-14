/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import com.potrt.stats.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

  private SecurityService securityService;

  @Autowired
  public LoginController(SecurityService securityService) {
    this.securityService = securityService;
  }

  @GetMapping("/auth/login")
  public String login() {
    return "login";
  }

  @ResponseBody
  @GetMapping("/auth/google/grantcode")
  public Person grantCode(@RequestParam("code") String code) {
    return securityService.getPerson();
  }

  @ResponseBody
  @GetMapping("/auth/test")
  public Person anyoneTest() {
    return securityService.getPerson();
  }

  @ResponseBody
  @GetMapping("/")
  public Person authenticatedTest() {
    return securityService.getPerson();
  }
}
