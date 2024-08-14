/* Copywrite (c) 2024 */
package com.potrt.stats.security.auth.user;

import org.springframework.data.repository.CrudRepository;

public interface AuthLocalRepository extends CrudRepository<AuthLocal, String> {

  AuthLocal findByEmail(String email);

  AuthLocal findByUsername(String email);
}
