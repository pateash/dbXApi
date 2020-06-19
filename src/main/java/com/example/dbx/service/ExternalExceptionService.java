package com.example.dbx.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

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

@RequiredArgsConstructor
@Service
public class ExternalExceptionService {
	private final ExceptionRepository exceptionRepository;
	private final OrgUnitRepository orgUnitRepository;
	private final BusinessComponentRepository businessComponentRepository;
	private final RejectedExceptionRepository rejectedExceptionRepository;

	public AcceptedExceptionBean addException(ExternalException externalException) {
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
		return exceptionRepository.save(acceptedException);
	}

}