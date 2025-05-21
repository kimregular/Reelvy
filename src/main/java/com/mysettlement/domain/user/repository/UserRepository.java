package com.mysettlement.domain.user.repository;

import com.mysettlement.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String email);

	Optional<User> findBySocialId(String openId);
}
