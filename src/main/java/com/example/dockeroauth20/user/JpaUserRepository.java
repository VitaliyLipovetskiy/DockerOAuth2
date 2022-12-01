package com.example.dockeroauth20.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByVerificationCode(String code);
}
