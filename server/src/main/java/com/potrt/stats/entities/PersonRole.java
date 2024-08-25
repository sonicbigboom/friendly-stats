/* Copywrite (c) 2024 */
package com.potrt.stats.entities;

import java.util.HashMap;
import java.util.Map;

/** A {@link PersonRole} represents the permissions that a {@link Person} has in a {@link Club}. */
public enum PersonRole {
  NOT_A_MEMBER(0, null),
  PERSON(1, "Player"),
  GAME_ADMIN(2, "Game Admin"),
  CASH_ADMIN(3, "Cash Admin"),
  CO_OWNER(3, "Co-Owner"),
  OWNER(4, "Owner");

  private static final Map<String, PersonRole> roleMap = new HashMap<>();

  static {
    roleMap.put(NOT_A_MEMBER.identifier, NOT_A_MEMBER);
    roleMap.put(PERSON.identifier, PERSON);
    roleMap.put(GAME_ADMIN.identifier, GAME_ADMIN);
    roleMap.put(CASH_ADMIN.identifier, CASH_ADMIN);
    roleMap.put(CO_OWNER.identifier, CO_OWNER);
    roleMap.put(OWNER.identifier, OWNER);
  }

  private int permissionLevel;
  private String identifier;

  /**
   * Creates a {@link PersonRole} with it's permission level and identifier.
   *
   * @param permissionLevel The permission level. Any permission with a higher permission level has
   *     this permission implicitly.
   * @param identifier The role identifier.
   */
  PersonRole(int permissionLevel, String identifier) {
    this.permissionLevel = permissionLevel;
    this.identifier = identifier;
  }

  /**
   * Get the {@link PersonRole} from an identifier.
   *
   * @param identifier The role identifer.
   * @return The corresponnding {@link PersonRole}.
   */
  public static PersonRole getRole(String identifier) {
    return roleMap.get(identifier);
  }

  /**
   * Gets the identifier.
   *
   * @return The identifier.
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Determines whether the given identifier has greater or equal permissions.
   *
   * @param identifier The identifier.
   * @return Whether the identifier has at least this {@link PersonRole}'s permissions.
   */
  public boolean permits(String identifier) {
    PersonRole role = getRole(identifier);
    return permits(role);
  }

  /**
   * Determines whether the given {@link PersonRole} has greater or equal permissions.
   *
   * @param identifier The {@link PersonRole}.
   * @return Whether the {@link PersonRole} has at least this {@link PersonRole}'s permissions.
   */
  public boolean permits(PersonRole role) {
    return role.permissionLevel >= this.permissionLevel;
  }
}
