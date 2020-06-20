package com.example.dbx.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.example.dbx.model.RejectedExceptionBean;
import com.example.dbx.model.RejectedExceptionsResult;
import com.example.dbx.model.ExceptionFilter;
import com.example.dbx.service.RejectedExceptionsService;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class RejectedExceptionController {
	private final RejectedExceptionsService rejectedExceptionsService;

	@GetMapping("/rejectedException")
	public RejectedExceptionsResult exceptions( // All exceptions API
			@RequestParam(required = false) String sort, // filter field
			@RequestParam(required = false) String order, // filter value
			@RequestParam(defaultValue = "0", required = false) Integer page, // page no.
			@RequestParam(defaultValue = "5", required = false) Integer pageSize, // page size
			@RequestParam(required = false) ExceptionFilter filter // filter
	) {
		return rejectedExceptionsService.exceptions(sort, order, page, pageSize, filter);
	}

	@GetMapping("/rejectedException/{id}")
	public RejectedExceptionBean getExceptionBean(@PathVariable Long id) {
		return rejectedExceptionsService.getExceptionBean(id);
	}

	@DeleteMapping("/rejectedException/{id}")
	public RejectedExceptionBean updateExceptionBean(@PathVariable Long id) {
		return rejectedExceptionsService.updateExceptionBean(id);
	}
}