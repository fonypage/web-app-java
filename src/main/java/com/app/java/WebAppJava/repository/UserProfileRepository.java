package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    Optional<UserProfile> findByUsername(String username);

    boolean existsByUsername(String username);

}