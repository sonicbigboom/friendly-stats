/* Copywrite (c) 2024 */
package com.potrt.stats;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Example {

  JdbcTemplate jdbcTemplate;

  @Autowired
  public Example(DataSource source) {
    jdbcTemplate = new JdbcTemplate(source);
  }

  @GetMapping("/")
  public String test() {
    RowMapper<String> mapper =
        (resultSet, rowNum) -> {
          return resultSet.getString("FirstName");
        };

    List<String> names = jdbcTemplate.query("SELECT * FROM PERSON;", mapper);

    return names.get(0);
  }
}
