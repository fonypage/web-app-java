package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.EmailLoginCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailLoginCodeRepository extends JpaRepository<EmailLoginCode, Long> {

    @Query("select c from EmailLoginCode c " +
            "where c.email = :email and c.usedAt is null and c.expiresAt > :now " +
            "order by c.id desc")
    List<EmailLoginCode> findActive(@Param("email") String email, @Param("now") LocalDateTime now);
}