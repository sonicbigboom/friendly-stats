/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth;

import com.potrt.stats.entities.Person;
import com.potrt.stats.exceptions.BadExternalCommunicationException;
import com.potrt.stats.security.auth.exceptions.EmailAlreadyExistsException;
import com.potrt.stats.security.auth.exceptions.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * An {@link AuthService} provides registeration and login authentication for a {@link AuthType}.
 */
@Transactional
public interface AuthService {

  /**
   * Registers a {@link Person}.
   *
   * @param registerDto A {@link RegisterDto} with all of the info for a new person.
   * @return The newly registered {@link Person}.
   * @throws EmailAlreadyExistsException Thrown when the email already exists.
   * @throws UsernameAlreadyExistsException Thrown when the username already exists.
   */
  @Transactional
  public Person register(RegisterDto registerDto)
      throws EmailAlreadyExistsException,
          UsernameAlreadyExistsException,
          BadExternalCommunicationException;

  /**
   * Attempts to login a {@link Person}.
   *
   * @param loginDto A {@link LoginDto} with a person's credentials.
   * @return The logged in {@link Authentication}.
   * @throws AuthenticationException Thrown if the authentication fails.
   */
  @Transactional
  public Authentication login(@Valid LoginDto loginDto)
      throws BadCredentialsException, DisabledException, BadExternalCommunicationException;

  /**
   * Checks that a {@link Person} is not disabled and not deleteed.
   *
   * @param person The {@link Person} to check.
   * @throws DisabledException Thrown if the account is disabled/deleted.
   */
  static void checkAccountStatus(Person person) throws DisabledException {
    if (Boolean.TRUE.equals(person.getIsDisabled())) {
      throw new DisabledException("Account is disabled.");
    }

    if (Boolean.TRUE.equals(person.getIsDeleted())) {
      throw new DisabledException("Account is deleted.");
    }
  }

  /**
   * Updates the credential for the {@link Person} using the {@link AuthService}'s {@link AuthType}.
   *
   * @param personID The {@link Person}'s id.
   * @param code The code representing the credentials for the {@link AuthService}'s {@link
   *     AuthType}.
   */
  public void updateCredentials(Integer personID, String code)
      throws BadExternalCommunicationException;
}
