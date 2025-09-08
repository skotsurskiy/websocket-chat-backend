package org.example.websocketchatbackend.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.websocketchatbackend.model.Role;
import org.example.websocketchatbackend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitialization implements CommandLineRunner {
  private final RoleRepository roleRepository;

  @Override
  public void run(String... args) throws Exception {
    if (!roleRepository.existsByName(Role.RoleName.ROLE_USER)) {
      Role user = new Role();
      user.setName(Role.RoleName.ROLE_USER);
      Role admin = new Role();
      admin.setName(Role.RoleName.ROLE_ADMIN);

      roleRepository.saveAll(List.of(user, admin));
    }
  }
}
