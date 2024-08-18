/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import com.potrt.stats.security.auth.google.AuthGoogleService;
import com.potrt.stats.security.auth.local.AuthLocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public enum AuthType {
  LOCAL("local", AuthLocalService.class),
  GOOGLE("google", AuthGoogleService.class);

  private String name;
  private Class<? extends AuthService> authServiceClass;

  AuthType(String name, Class<? extends AuthService> authServiceClass) {
    this.name = name;
    this.authServiceClass = authServiceClass;
  }

  public static boolean authExists(String auth) {
    for (AuthType authType : AuthType.values()) {
      if (authType.name.equalsIgnoreCase(auth)) {
        return true;
      }
    }
    return false;
  }

  @Autowired
  public static AuthService getAuthService(ApplicationContext context, String auth) {
    for (AuthType authType : AuthType.values()) {
      if (authType.name.equalsIgnoreCase(auth)) {
        return authType.getAuthService(context);
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }

  @Autowired
  public AuthService getAuthService(ApplicationContext context) {
    return context.getBean(authServiceClass);
  }
}
