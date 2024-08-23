/* Copywrite (c) 2024 */
package com.potrt.stats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;

/** A {@link Person} represents a user of the application. */
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String username;

  @Column(nullable = true)
  private String firstName;

  @Column(nullable = true)
  private String lastName;

  @Column(nullable = true)
  private String nickname;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private Boolean isDisabled;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private Boolean isDeleted;

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class MaskedPerson {
    private Integer id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String nickname;

    public MaskedPerson(Person person) {
      this(person, false);
    }

    public MaskedPerson(Person person, boolean includeEmail) {
      this.id = person.id;
      if (includeEmail) {
        this.email = person.email;
      }
      this.username = person.username;
      this.firstName = person.firstName;
      this.lastName = person.lastName;
      this.nickname = person.nickname;
    }
  }
}
