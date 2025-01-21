package com.mysettlement.domain.user.repository;

import com.mysettlement.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}
