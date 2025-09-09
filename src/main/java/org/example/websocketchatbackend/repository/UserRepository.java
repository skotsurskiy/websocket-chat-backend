package org.example.websocketchatbackend.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.websocketchatbackend.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByUsername(String username);

  @EntityGraph(attributePaths = {"roles"})
  Optional<User> findByUsername(String username);

  @Query("""
          SELECT u FROM User u
          WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))
            AND u.username NOT IN :usersFromChats
          ORDER BY
            CASE
              WHEN LOWER(u.username) = LOWER(:username) THEN 1
              WHEN LOWER(u.username) LIKE LOWER(CONCAT(:username, '%')) THEN 2
              ELSE 3
            END,
            u.username
      """)
  List<User> findByUsernameContainingIgnoreCase(
      @Param("username") String username,
      @Param("usersFromChats") Set<String> usersFromChats
  );
}
