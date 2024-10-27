/* Copyright (c) 2024 */
package com.potrt.stats.data.person;

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

/** A {@link Person} represents a person in the application. */
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
  private boolean isPrivate;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private boolean isDisabled;

  @Column(nullable = false)
  @Convert(converter = NumericBooleanConverter.class)
  private boolean isDeleted;

  /** A {@link Person} with private information hidden. */
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
    private boolean isPrivate;

    /**
     * A {@link MaskedPerson} with only public information.
     *
     * @param person The {@link Person} to mask.
     */
    public MaskedPerson(Person person) {
      this(person, false);
    }

    /**
     * A {@link MaskedPerson} with public information and potentially sensitive info.
     *
     * @param person The {@link Person} to mask.
     * @param includeSensitive Whether the {@code email} amd {@code isPrivate} is included.
     */
    public MaskedPerson(Person person, boolean includeSensitive) {
      this.id = person.id;
      this.username = person.username;
      this.firstName = person.firstName;
      this.lastName = person.lastName;
      this.nickname = person.nickname;
      this.isPrivate = person.isPrivate;

      if (includeSensitive) {
        this.email = person.email;
      }
    }
  }
}
