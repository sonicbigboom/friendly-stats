/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import com.potrt.stats.security.auth.basic.AuthBasicService;
import com.potrt.stats.security.auth.google.AuthGoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/** An {@link AuthType} represents a valid authentication type. */
public enum AuthType {
  BASIC("basic", AuthBasicService.class),
  GOOGLE("google", AuthGoogleService.class);

  private String name;
  private Class<? extends AuthService> authServiceClass;
  private ApplicationContext applicationContext;

  /**
   * Prepares a valid authentication type with its name and authentication service.
   *
   * @param name The authentication type name.
   * @param authServiceClass The {@link AuthService} for the authentication type.
   */
  AuthType(String name, Class<? extends AuthService> authServiceClass) {
    this.name = name;
    this.authServiceClass = authServiceClass;
  }

  /**
   * Checks whether there is an {@link AuthType} for the given name
   *
   * @param name The name of the authentication type.
   * @return Whether there is a {@link AuthType}.
   */
  public static boolean authExists(String name) {
    for (AuthType authType : AuthType.values()) {
      if (authType.name.equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the authentication service based on the name.
   *
   * @param name The name of the {@link AuthType};
   * @return The {@link AuthService} for the name of the {@link AuthType}.
   */
  @Autowired
  public static AuthService getAuthService(String name) {
    for (AuthType authType : AuthType.values()) {
      if (authType.name.equalsIgnoreCase(name)) {
        return authType.getAuthService();
      }
    }
    return null;
  }

  /**
   * Gets the {@link AuthService}.
   *
   * @return The {@link AuthService}.
   */
  public AuthService getAuthService() {
    return applicationContext.getBean(authServiceClass);
  }

  /** Autowired injection of an {@link ApplicationContext}. */
  @Autowired
  public void injectApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
