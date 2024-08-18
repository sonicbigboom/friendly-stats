/* Copywrite (c) 2024 */
package com.potrt.stats.api.auth.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthLoginApi {

  ApplicationContext applicationContext;

  @Autowired
  public AuthLoginApi(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @GetMapping("/auth/login")
  public String login() {
    return "login";
  }
}
