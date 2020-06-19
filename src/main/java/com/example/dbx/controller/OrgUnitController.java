package com.example.dbx.controller;

import javax.validation.Valid;

import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.OrgUnitsResult;
import com.example.dbx.service.OrgUnitService;

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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class OrgUnitController {
	private final OrgUnitService orgUnitService;

	@GetMapping("/orgUnit")
	public OrgUnitsResult getAllOrgUnits(@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "5") int pageSize) {
		return orgUnitService.getAllOrgUnits(page, pageSize);
	}

	@PostMapping("/orgUnit")
	public OrgUnit addOrgUnit(@Valid @RequestBody OrgUnit orgUnit) {
		return orgUnitService.addOrgUnit(orgUnit);
	}

	@DeleteMapping("/orgUnit/{id}")
	public String deleteOrgUnit(@PathVariable("id") Long id) {
		return orgUnitService.deleteOrgUnit(id);
	}
}
