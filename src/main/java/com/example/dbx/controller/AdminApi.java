package com.example.dbx.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.UserRepository;
import com.example.dbx.security.services.UserPrinciple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
// @PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasRole('USER')")
public class AdminApi {
	@Autowired
	UserRepository userRepository;

	@Autowired
	OrgUnitRepository orgUnitRepository;

	@GetMapping("/user")
	public List<User> getAllUsers(@RequestParam(required = true, defaultValue = "") String type, Principal principal) {
		UserPrinciple up = UserPrinciple.extractFromPrincipal(principal);

		System.out.println(up.getOrgUnit() + " : " + up.getId());

		switch (type) {
			case "disabled":
				return userRepository.findByIsEnabled(false);
			case "enabled":
				return userRepository.findByIsEnabled(true);
		}

		return userRepository.findAll();
	}

	@GetMapping("/org_unit")
	public List<OrgUnit> getAllOrgUnits() {
		return orgUnitRepository.findAll();
	}

	@PostMapping("/org_unit")
	public ResponseEntity<String> addOrgUnit(@Valid @RequestBody OrgUnit orgUnit) {
		OrgUnit newOrgUnit = new OrgUnit(orgUnit.getName());
		orgUnitRepository.save(newOrgUnit);
		return ResponseEntity.ok().body("Org Unit registered successfully!");
	}

	@DeleteMapping("/org_unit/{id}")
	public ResponseEntity<String> deleteOrgUnit(@PathVariable("id") Long id) {
		orgUnitRepository.deleteById(id);
		return ResponseEntity.ok().body("Org Unit deleted successfully!");
	}

}
