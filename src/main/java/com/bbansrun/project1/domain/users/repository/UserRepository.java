package com.bbansrun.project1.domain.users.repository;

import com.bbansrun.project1.domain.users.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserUuid(UUID userUuid);
}
