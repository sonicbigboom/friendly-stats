/* Copywrite (c) 2024 */
package com.potrt.stats.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
  @GetMapping("/auth/login")
  public String login() {
    return "login";
  }

  @ResponseBody
  @GetMapping("/auth/google/grantcode")
  public String grantCode(@RequestParam("code") String code) {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  @ResponseBody
  @GetMapping("/auth/test")
  public String anyoneTest() {
    return "anyone";
  }

  @ResponseBody
  @GetMapping("/test")
  public String authenticatedTest() {
    return "authenticated";
  }
}
