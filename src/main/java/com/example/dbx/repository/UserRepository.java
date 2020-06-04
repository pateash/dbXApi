package com.example.dbx.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.dbx.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    List<User> findByIsEnabled(Boolean isEnabled);

    @Modifying
    @Query("update User u set u.isEnabled = :isEnabled where u.id = :id")
    int setIsEnabled(@Param("id") Long id, @Param("isEnabled") Integer isEnabled);
}