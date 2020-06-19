package com.example.dbx.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.OrgUnitsResult;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class OrgUnitApi {
	@Autowired
	UserRepository userRepository;

	@Autowired
	OrgUnitRepository orgUnitRepository;

	@GetMapping("/orgUnit")
	public OrgUnitsResult getAllOrgUnits(@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "5") int pageSize, Principal principal) {
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<OrgUnit> pageResult;

		pageResult = orgUnitRepository.findAll(pageRequest);
		List<OrgUnit> units = new ArrayList<>(pageResult.getContent());

		units.removeIf(o -> o.getName().equals("-"));

		return new OrgUnitsResult(units, pageResult.getTotalElements());
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
