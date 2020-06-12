package com.example.dbx.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserFilter;
import com.example.dbx.model.UserRole;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@Data
class OrgUnitsResult {
	List<OrgUnit> orgUnits;
	Long totalElements;

	public OrgUnitsResult(List<OrgUnit> orgUnits, Long totalElements) {
		this.orgUnits = orgUnits;
		this.totalElements = totalElements;
	}
}

@Data
class UsersResult {
	List<User> users;
	Long totalElements;

	public UsersResult(List<User> users, Long totalElements) {
		this.users = users;
		this.totalElements = totalElements;
	}
}

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasRole('USER')")
public class AdminApi {
	@Autowired
	UserRepository userRepository;

	@Autowired
	OrgUnitRepository orgUnitRepository;

	@GetMapping("/user")
	public UsersResult getAllUsers(@RequestParam(defaultValue = "") String type,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "5") int pageSize,
			@RequestParam(required = false) UserFilter filter, Principal principal) {
		// System.out.println("Filter : " + filter != null ? filter.getIsEnabled() :
		// "NULL");

		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<User> pageResult;

		if (filter == null || (filter != null && filter.getIsEnabled() == null)) {
			System.out.println("FILTER NULL");
			pageResult = userRepository.findAllByRole(UserRole.ROLE_USER, pageRequest);
		} else {
			pageResult = userRepository.findByIsEnabledAndRole(filter.getIsEnabled(), UserRole.ROLE_USER, pageRequest);
		}

		UsersResult result = new UsersResult(pageResult.getContent(), pageResult.getTotalElements());

		return result;
	}

	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);

		return user.get();
	}

	@PatchMapping("/user/{id}")
	public User updateUser(@PathVariable Long id, @RequestBody User user) {
		System.out.println("ID: " + id + " & username: " + user.getUsername() + " & isEnabled: " + user.getIsEnabled());
		Optional<User> res = userRepository.findById(id);
		if (!res.isPresent()) {
			return new User();
		}

		User updatedUser = res.get();
		if (updatedUser.getRole() != UserRole.ROLE_ADMIN) {
			updatedUser.setIsEnabled(user.getIsEnabled());
			// userRepository.setIsEnabled(id, user.getIsEnabled());
			userRepository.save(updatedUser);
		}

		return updatedUser;
	}

	@GetMapping("/orgUnit")
	public OrgUnitsResult getAllOrgUnits(@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "5") int pageSize, Principal principal) {
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<OrgUnit> pageResult;

		pageResult = orgUnitRepository.findAll(pageRequest);
		List<OrgUnit> units = new ArrayList<>(pageResult.getContent());

		units.removeIf(o -> {
			return (o.getName().equals("-"));
		});

		OrgUnitsResult result = new OrgUnitsResult(units, pageResult.getTotalElements());

		return result;
	}

	@PostMapping("/orgUnit")
	public OrgUnit addOrgUnit(@Valid @RequestBody OrgUnit orgUnit) {
		OrgUnit newOrgUnit = new OrgUnit(orgUnit.getName());
		orgUnitRepository.save(newOrgUnit);
		return newOrgUnit;
	}

	@DeleteMapping("/orgUnit/{id}")
	public ResponseEntity<String> deleteOrgUnit(@PathVariable("id") Long id) {
		orgUnitRepository.deleteById(id);
		return ResponseEntity.ok().body("Org Unit deleted successfully!");
	}
}
