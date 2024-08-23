/* Copywrite (c) 2024 */
package com.potrt.stats.entities;

import java.util.HashMap;
import java.util.Map;

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

  PersonRole(int value, String string) {
    this.permissionLevel = value;
    this.identifier = string;
  }

  public static PersonRole getRole(String identifier) {
    return roleMap.get(identifier);
  }

  public boolean permits(String identifier) {
    PersonRole role = getRole(identifier);
    return permits(role);
  }

  public boolean permits(PersonRole role) {
    return role.permissionLevel >= this.permissionLevel;
  }
}
