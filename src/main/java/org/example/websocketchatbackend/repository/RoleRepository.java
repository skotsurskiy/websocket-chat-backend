package org.example.websocketchatbackend.repository;

import org.example.websocketchatbackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findRoleByName(Role.RoleName name);

  boolean existsByName(Role.RoleName name);
}
