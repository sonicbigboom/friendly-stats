/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.basic;

import org.springframework.data.repository.CrudRepository;

public interface AuthBasicRepository extends CrudRepository<AuthBasic, String> {

  AuthBasic findByEmail(String email);

  AuthBasic findByUsername(String email);
}
