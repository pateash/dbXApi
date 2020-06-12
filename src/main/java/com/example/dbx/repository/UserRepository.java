package com.example.dbx.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.dbx.model.User;
import com.example.dbx.model.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Page<User> findByIsEnabledAndRole(Boolean isEnabled, UserRole role, Pageable page);

    Page<User> findAll(Pageable page);

    Page<User> findAllByRole(UserRole role, Pageable page);

    @Modifying
    @Query("update User u set u.isEnabled = :isEnabled where u.id = :id")
    void setIsEnabled(@Param("id") Long id, @Param("isEnabled") Boolean isEnabled);
}