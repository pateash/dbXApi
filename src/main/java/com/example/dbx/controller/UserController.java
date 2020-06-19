package com.example.dbx.controller;

import com.example.dbx.model.User;
import com.example.dbx.model.UserFilter;
import com.example.dbx.model.UserRole;
import com.example.dbx.model.UsersResult;
import com.example.dbx.service.UserService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
	private final UserService userService;

	@GetMapping("/user")
	public UsersResult getAllUsers( // get all users
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "5") int pageSize,
			@RequestParam(required = false) UserFilter filter) {
		return userService.getAllUsers(page, pageSize, UserRole.ROLE_USER, filter);
	}

	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}
}
