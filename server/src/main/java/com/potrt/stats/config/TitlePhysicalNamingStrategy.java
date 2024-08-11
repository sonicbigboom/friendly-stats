/* Copywrite (c) 2024 */
package com.potrt.stats.config;

import org.apache.commons.text.WordUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class TitlePhysicalNamingStrategy implements PhysicalNamingStrategy {

  @Override
  public Identifier toPhysicalCatalogName(
      final Identifier identifier, final JdbcEnvironment jdbcEnv) {
    return capitalize(identifier);
  }

  @Override
  public Identifier toPhysicalColumnName(
      final Identifier identifier, final JdbcEnvironment jdbcEnv) {
    return capitalize(identifier);
  }

  @Override
  public Identifier toPhysicalSchemaName(
      final Identifier identifier, final JdbcEnvironment jdbcEnv) {
    return capitalize(identifier);
  }

  @Override
  public Identifier toPhysicalSequenceName(
      final Identifier identifier, final JdbcEnvironment jdbcEnv) {
    return capitalize(identifier);
  }

  @Override
  public Identifier toPhysicalTableName(
      final Identifier identifier, final JdbcEnvironment jdbcEnv) {
    return capitalize(identifier);
  }

  private Identifier capitalize(final Identifier identifier) {
    if (identifier == null) {
      return null;
    }

    return Identifier.toIdentifier(WordUtils.capitalize(identifier.getText()));
  }
}
