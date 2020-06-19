package com.example.dbx.controller;

import java.security.Principal;
import java.util.Optional;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.model.User;
import com.example.dbx.model.UserFilter;
import com.example.dbx.model.UserRole;
import com.example.dbx.model.UsersResult;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class UserApi {
	@Autowired
	public UserRepository userRepository;

	@Autowired
	OrgUnitRepository orgUnitRepository;

	@GetMapping("/user")
	public UsersResult getAllUsers(@RequestParam(defaultValue = "") String type,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "5") int pageSize,
			@RequestParam(required = false) UserFilter filter, Principal principal) {

		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<User> pageResult;

		if (filter == null || (filter.getIsEnabled() == null)) {
			pageResult = userRepository.findAllByRole(UserRole.ROLE_USER, pageRequest);
		} else {
			pageResult = userRepository.findByIsEnabledAndRole(filter.getIsEnabled(), UserRole.ROLE_USER, pageRequest);
		}

		return new UsersResult(pageResult.getContent(), pageResult.getTotalElements());
	}

	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new InvalidException("User -> " + id + " does not Exist");
		}

		return user.get();
	}
}
