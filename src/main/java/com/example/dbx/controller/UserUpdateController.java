package com.example.dbx.controller;

import com.example.dbx.model.User;
import com.example.dbx.service.UserService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class UserUpdateController {
    private final UserService userService;

    @PatchMapping("/user/{id}")
    public User updateUserData(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
}