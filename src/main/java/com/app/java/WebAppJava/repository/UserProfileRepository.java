package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}