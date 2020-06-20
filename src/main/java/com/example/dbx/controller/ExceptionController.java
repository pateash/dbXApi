package com.example.dbx.controller;

import java.security.Principal;
import java.text.ParseException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.ExceptionBeanUpdate;
import com.example.dbx.model.ExceptionFilter;
import com.example.dbx.model.ExceptionSummary;
import com.example.dbx.model.ExceptionsResult;
import com.example.dbx.model.OldExceptionsResult;
import com.example.dbx.security.services.UserPrinciple;
import com.example.dbx.service.ExceptionService;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
public class ExceptionController {
	private final ExceptionService exceptionService;

	@GetMapping("/exception")
	public ExceptionsResult exceptions( // All exceptions API
			@RequestParam(required = false) String sort, // filter field
			@RequestParam(required = false) String order, // filter value
			@RequestParam(defaultValue = "0", required = false) Integer page, // page no.
			@RequestParam(defaultValue = "5", required = false) Integer pageSize, // page size
			@RequestParam(required = false) ExceptionFilter filter, // filter
			Principal principal // principal
	) {
		UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
		Long orgUnitId = userPrinciple.getOrgUnit().getId();

		return exceptionService.exceptions(sort, order, page, pageSize, filter, orgUnitId);
	}

	@GetMapping("/exception/{id}")
	public AcceptedExceptionBean getExceptionBean( // get particluar exception
			@PathVariable Long id, // id
			Principal principal // principal
	) {
		UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
		Long orgUnitId = userPrinciple.getOrgUnit().getId();

		return exceptionService.getExceptionBean(id, orgUnitId);
	}

	@PatchMapping("/exception/{id}")
	public AcceptedExceptionBean updateExceptionBean( // update exception
			@PathVariable Long id, // id
			@RequestBody ExceptionBeanUpdate update, // update request
			Principal principal // principal
	) {
		UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
		Long orgUnitId = userPrinciple.getOrgUnit().getId();

		return exceptionService.updateExceptionBean(id, update, orgUnitId);
	}

	@GetMapping("/oldException/{id}")
	public OldExceptionsResult getExceptionVersions( // get particluar exception
			@PathVariable Long id, // id
			Principal principal, // principal
			@RequestParam(defaultValue = "0", required = false) Integer page, // page no.
			@RequestParam(defaultValue = "1000", required = false) Integer pageSize // page size
	) {
		UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
		Long orgUnitId = userPrinciple.getOrgUnit().getId();

		return exceptionService.getExceptionVersions(id, orgUnitId, page, pageSize);
	}

	@GetMapping("/exception/summary")
	public ExceptionSummary getExceptionSummary(Principal principal) throws ParseException {
		UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
		Long orgUnitId = userPrinciple.getOrgUnit().getId();

		return exceptionService.getExceptionSummary(orgUnitId);
	}
}