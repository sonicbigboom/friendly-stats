/* Copyright (c) 2024 */
package com.potrt.stats.exceptions;

import com.potrt.stats.data.game.Game;
import com.potrt.stats.data.person.Person;
import com.potrt.stats.data.player.GamePlayer;

/**
 * An {@link Exception} when trying to perform an action on a {@link Person} for a {@link Game} that
 * the {@link Person} is not a {@link GamePlayer} of.
 *
 * @fs.httpStatus 400 Bad Request
 */
public class PersonIsNotPlayerException extends Exception {
  public PersonIsNotPlayerException() {
    super();
  }

  public PersonIsNotPlayerException(Throwable cause) {
    super(cause);
  }

  public PersonIsNotPlayerException(String msg) {
    super(msg);
  }

  public PersonIsNotPlayerException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
