package com.example.dbx.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.ExternalException;
import com.example.dbx.service.ExternalExceptionService;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ExternalExceptionController {
	private final ExternalExceptionService externalExceptionService;

	@PostMapping("/exception")
	public ResponseEntity<Object> addException(@RequestBody ExternalException externalException) {
		AcceptedExceptionBean savedException = externalExceptionService.addException(externalException);

		// Sending proper response back. We are sending the URL of the saved
		// exception and an HTTP Response of "CREATED"
		URI exceptionLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedException.getId()).toUri();

		return ResponseEntity.created(exceptionLocation).build();
	}

}