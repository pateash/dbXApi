package com.example.dbx.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.ExceptionSeverity;
import com.example.dbx.model.ExternalException;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.BusinessComponent;
import com.example.dbx.model.RejectedExceptionBean;
import com.example.dbx.repository.BusinessComponentRepository;
import com.example.dbx.repository.ExceptionRepository;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.RejectedExceptionRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ExternalExceptionController {
	@Autowired
	private ExceptionRepository exceptionRepository;

	@Autowired
	private OrgUnitRepository orgUnitRepository;

	@Autowired
	private BusinessComponentRepository businessComponentRepository;

	@Autowired
	private RejectedExceptionRepository rejectedExceptionRepository;

	@PostMapping("/exception")
	public ResponseEntity<Object> addException(@RequestBody ExternalException externalException) {
		// STEP1: Validate the incoming exception.
		// STEP2: Fill in the empty fields required for the database
		// STEP3: Store the exception in the database
		// STEP4: Return a relevant response

		// Encoding severity:
		ExceptionSeverity severity = null;
		switch (externalException.getSeverity().toLowerCase()) {
			case "low":
				severity = ExceptionSeverity.SEVERITY_LOW;
				break;
			case "medium":
				severity = ExceptionSeverity.SEVERITY_MEDIUM;
				break;
			case "high":
			default:
				severity = ExceptionSeverity.SEVERITY_HIGH;
				break;
		}

		// STEP1: Validating the incoming request:
		OrgUnit orgUnit = orgUnitRepository.findByName(externalException.getOrgUnit());
		BusinessComponent businessComponent = businessComponentRepository.findByNameAndOrgUnitIdAndIsEnabled(
				externalException.getBusinessComponent(),
				(orgUnit != null && orgUnit.getId() != null) ? orgUnit.getId() : -1, true);
		if (orgUnit == null || businessComponent == null) {
			// Incoming request is invalid
			RejectedExceptionBean rejectedException = new RejectedExceptionBean( // RejectedException
					externalException.getSource(), // source
					externalException.getCategory(), // category
					externalException.getDescription(), // description
					severity, // severity
					externalException.getBusinessComponent() + (businessComponent == null ? ",-" : ""), // businessComponent
					externalException.getOrgUnit() + (orgUnit == null ? ",-" : ""), // orgUnit
					externalException.getTechnicalDescription(), // technical description
					null // comment
			);

			// Adding the rejected exception to 'rejected_exception' database
			rejectedExceptionRepository.save(rejectedException);

			if (orgUnit == null && businessComponent != null) {
				throw new InvalidException("Org-Unit -> " + externalException.getOrgUnit() + " does not Exist");
			} else if (orgUnit != null) {
				throw new InvalidException(
						"Business-Component -> " + externalException.getBusinessComponent() + " does not Exist");
			} else {
				throw new InvalidException("Business-Component -> " + externalException.getBusinessComponent()
						+ " & Org-Unit -> " + externalException.getOrgUnit() + " does not Exist");
			}

		}

		// STEP2: Fill in the empty fields required for the database
		AcceptedExceptionBean acceptedException = new AcceptedExceptionBean( // Accepted exception
				externalException.getSource(), // source
				externalException.getCategory(), // category
				externalException.getDescription(), // description
				severity, // severity
				businessComponent, // businessComponent
				orgUnit, // orgUnit
				externalException.getTechnicalDescription(), // technical description
				null // comment
		);

		// STEP3: Adding the exception to Database
		AcceptedExceptionBean savedException = exceptionRepository.save(acceptedException);

		// STEP4: Sending proper response back. We are sending the URL of the saved
		// exception and an HTTP Response of "CREATED"
		URI exceptionLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedException.getId()).toUri();

		return ResponseEntity.created(exceptionLocation).build();
	}

}