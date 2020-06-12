package com.example.dbx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

import com.example.dbx.exception.ExceptionNotFound;
import com.example.dbx.exception.InvalidException;
// import com.example.dbx.exception.InvalidQueryException;
import com.example.dbx.model.RejectedExceptionBean;
import com.example.dbx.model.ExceptionFilter;
import com.example.dbx.model.ExceptionSeverity;
import com.example.dbx.model.ExceptionStatus;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.BusinessComponent;
import com.example.dbx.repository.BusinessComponentRepository;
import com.example.dbx.repository.ExceptionRepository;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.RejectedExceptionRepository;

/**
 * RejectedExceptionsResult
 */
@Data
class RejectedExceptionsResult {
	List<RejectedExceptionBean> rejectedExceptions;
	Long totalElements;

}

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class RejectedExceptionController {

	@Autowired
	private ExceptionRepository exceptionRepository;

	@Autowired
	private RejectedExceptionRepository rejectedExceptionRepository;

	@Autowired
	private OrgUnitRepository orgUnitRepository;

	@Autowired
	private BusinessComponentRepository businessComponentRepository;

	private Direction getDirection(String order) {
		return (order != null && order.toLowerCase().contains("des")) ? Direction.DESC : Direction.ASC;
	}

	/*
	 * If the optional parameters are not passed, they are converted to null.
	 */
	@GetMapping("/rejectedException")
	public @ResponseBody ResponseEntity<Object> exceptions( // All exceptions API
			@RequestParam(required = false) String filterBy, // filter field
			@RequestParam(required = false) String filterName, // filter value
			@RequestParam(required = false) String sort, // filter field
			@RequestParam(required = false) String order, // filter value
			@RequestParam(defaultValue = "0", required = false) Integer page, // page no.
			@RequestParam(defaultValue = "5", required = false) Integer pageSize, // page size
			@RequestParam(required = false) ExceptionFilter filter // filter
	) {

		System.out.println(sort + " - " + order);
		System.out.println(filter);
		List<Order> orders = new ArrayList<Order>();
		RejectedExceptionsResult result = new RejectedExceptionsResult();

		if (filter.getSeverityOrder() != null) {
			System.out.println("severtiy order" + filter.getSeverityOrder());
			Direction direction = getDirection(filter.getSeverityOrder());
			orders.add(new Order(direction, "severity"));
		}

		if (sort != null) {
			System.out.println(sort + " - " + order);
			Direction direction = getDirection(order);
			orders.add(new Order(direction, sort));
		}

		// Switch case expression need to be a constant, it cannot be null. Hence
		// writing filterBy=null case outside the switch case
		// if (filterBy == null) {
		/*
		 * if (filter == null) { Page<RejectedExceptionBean> res =
		 * rejectedExceptionRepository .findAll(PageRequest.of(page, pageSize,
		 * Sort.by(orders))); result.exceptions = res.getContent(); result.totalElements
		 * = res.getTotalElements(); return new ResponseEntity<Object>(result,
		 * HttpStatus.OK); }
		 */

		System.out.println(orders + " - " + orders.size());

		PageRequest pageReq = PageRequest.of(page, pageSize, Sort.by(orders));
		Page<RejectedExceptionBean> pageRes;

		if (filter.getCategory() == null) {
			filter.setCategory("");
		}

		if (filter.getSource() == null) {
			filter.setSource("");
		}

		if (filter.getSeverity() != null) {
			ExceptionSeverity severe;
			switch (filter.getSeverity()) {
				case "medium":
					severe = ExceptionSeverity.SEVERITY_MEDIUM;
					break;
				case "high":
					severe = ExceptionSeverity.SEVERITY_HIGH;
					break;
				default:
					severe = ExceptionSeverity.SEVERITY_LOW;
					break;
			}

			if (filter.getStatus() != null) {
				ExceptionStatus status;
				switch (filter.getStatus()) {
					case "resolved":
						status = ExceptionStatus.STATUS_RESOLVED;
						break;
					default:
						status = ExceptionStatus.STATUS_UNRESOLVED;
						break;
				}
				pageRes = rejectedExceptionRepository
						.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndStatus(
								filter.getSource(), filter.getCategory(), severe, status, pageReq);
			} else {
				pageRes = rejectedExceptionRepository
						.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverity(filter.getSource(),
								filter.getCategory(), severe, pageReq);
			}
		} else if (filter.getStatus() != null) {
			ExceptionStatus status;
			switch (filter.getStatus()) {
				case "resolved":
					status = ExceptionStatus.STATUS_RESOLVED;
					break;
				default:
					status = ExceptionStatus.STATUS_UNRESOLVED;
					break;
			}
			pageRes = rejectedExceptionRepository
					.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndStatus(filter.getSource(),
							filter.getCategory(), status, pageReq);
		} else {
			pageRes = rejectedExceptionRepository.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCase(
					filter.getSource(), filter.getCategory(), pageReq);
		}

		// switch (filterBy) {
		// case "category":
		// pageRes = rejectedExceptionRepository.findByCategoryContaining(filterName,
		// pageReq);
		// break;

		// case "source":
		// pageRes = rejectedExceptionRepository
		// .findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCase(filterName,
		// "", pageReq);
		// break;

		// case "severity":
		// ExceptionSeverity severe;
		// switch (filterName.toLowerCase()) {
		// case "medium":
		// severe = ExceptionSeverity.SEVERITY_MEDIUM;
		// break;
		// case "high":
		// severe = ExceptionSeverity.SEVERITY_HIGH;
		// break;
		// default:
		// severe = ExceptionSeverity.SEVERITY_LOW;
		// break;
		// }

		// pageRes = rejectedExceptionRepository.findBySeverity(severe, pageReq);
		// break;

		// default:
		// pageRes = rejectedExceptionRepository.findAll(PageRequest.of(page,
		// pageSize));
		// }

		result.rejectedExceptions = pageRes.getContent();
		result.totalElements = pageRes.getTotalElements();

		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	@GetMapping("/rejectedException/{id}")
	public RejectedExceptionBean getExceptionBean(@PathVariable Long id) {
		Optional<RejectedExceptionBean> res = rejectedExceptionRepository.findById(id);
		if (!res.isPresent()) {
			throw new ExceptionNotFound("Exception with id -> " + id + " does not exist");
		}

		return res.get();
	}

	@DeleteMapping("/rejectedException/{id}")
	public RejectedExceptionBean updateExceptionBean(@PathVariable Long id) {
		Optional<RejectedExceptionBean> res = rejectedExceptionRepository.findById(id);
		if (!res.isPresent()) {
			throw new ExceptionNotFound("Exception with id -> " + id + " does not exist");
		}

		RejectedExceptionBean externalException = res.get();

		externalException.setBusinessComponent(externalException.getBusinessComponent().replace(",-", ""));
		externalException.setOrgUnit(externalException.getOrgUnit().replace(",-", ""));

		OrgUnit orgUnit = orgUnitRepository.findByName(externalException.getOrgUnit());
		BusinessComponent businessComponent = businessComponentRepository
				.findByName(externalException.getBusinessComponent());
		if (orgUnit == null || businessComponent == null) {
			if (orgUnit == null && businessComponent != null) {
				throw new InvalidException("Org-Unit -> " + externalException.getOrgUnit() + " does not Exist");
			} else if (businessComponent == null && orgUnit != null) {
				throw new InvalidException(
						"Business-Component -> " + externalException.getBusinessComponent() + " does not Exist");
			} else {
				throw new InvalidException("Business-Component -> " + externalException.getBusinessComponent()
						+ " & Org-Unit -> " + externalException.getOrgUnit() + " does not Exist");
			}
		}

		AcceptedExceptionBean acceptedException = new AcceptedExceptionBean( // Accepted exception
				externalException.getSource(), // source
				externalException.getCategory(), // category
				externalException.getDescription(), // description
				externalException.getSeverity(), // severity
				businessComponent, // businessComponent
				orgUnit, // orgUnit
				externalException.getTechnicalDescription(), // technical description
				null // comment
		);

		// STEP3: Adding the exception to Database
		/* AcceptedExceptionBean savedException = */exceptionRepository.save(acceptedException);

		rejectedExceptionRepository.deleteById(id);

		return externalException;
	}
}