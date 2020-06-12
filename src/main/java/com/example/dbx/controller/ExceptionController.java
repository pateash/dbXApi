package com.example.dbx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.security.Principal;
import java.sql.Timestamp;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

import com.example.dbx.exception.ExceptionNotFound;
// import com.example.dbx.exception.InvalidQueryException;
import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.ExceptionBeanUpdate;
import com.example.dbx.model.ExceptionFilter;
import com.example.dbx.model.ExceptionSeverity;
import com.example.dbx.model.ExceptionStatus;
import com.example.dbx.repository.ExceptionRepository;
// import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.security.services.UserPrinciple;

/**
 * ExceptionsResult
 */
@Data
class ExceptionsResult {
	List<AcceptedExceptionBean> exceptions;
	Long totalElements;

}

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
public class ExceptionController {

	@Autowired
	private ExceptionRepository exceptionRepository;

	private Direction getDirection(String order) {
		return (order != null && order.toLowerCase().contains("des")) ? Direction.DESC : Direction.ASC;
	}

	/*
	 * If the optional parameters are not passed, they are converted to null.
	 */
	@GetMapping("/exception")
	public @ResponseBody ResponseEntity<Object> exceptions( // All exceptions API
			@RequestParam(required = false) String filterBy, // filter field
			@RequestParam(required = false) String filterName, // filter value
			@RequestParam(required = false) String sort, // filter field
			@RequestParam(required = false) String order, // filter value
			@RequestParam(defaultValue = "0", required = false) Integer page, // page no.
			@RequestParam(defaultValue = "5", required = false) Integer pageSize, // page size
			@RequestParam(required = false) ExceptionFilter filter, // filter
			Principal principal // principal
	) {
		UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
		Long id = userPrinciple.getOrgUnit().getId();

		System.out.println("Org Unit - " + userPrinciple.getOrgUnit());
		System.out.println(sort + " - " + order);
		System.out.println(filter);
		List<Order> orders = new ArrayList<Order>();
		ExceptionsResult result = new ExceptionsResult();

		if (filter.getSeverityOrder() != null) {
			Direction direction = getDirection(filter.getSeverityOrder());
			orders.add(new Order(direction, "severity"));
		}

		if (sort != null) {
			Direction direction = getDirection(order);
			orders.add(new Order(direction, sort));
		}

		PageRequest pageReq = PageRequest.of(page, pageSize, Sort.by(orders));
		Page<AcceptedExceptionBean> pageRes;

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
				pageRes = exceptionRepository
						.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndStatusAndOrgUnitId(
								filter.getSource(), filter.getCategory(), severe, status, id, pageReq);
			} else {
				pageRes = exceptionRepository
						.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndOrgUnitId(
								filter.getSource(), filter.getCategory(), severe, id, pageReq);
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
			pageRes = exceptionRepository
					.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndStatusAndOrgUnitId(
							filter.getSource(), filter.getCategory(), status, id, pageReq);
		} else {
			pageRes = exceptionRepository.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndOrgUnitId(
					filter.getSource(), filter.getCategory(), id, pageReq);
		}

		result.exceptions = pageRes.getContent();
		result.totalElements = pageRes.getTotalElements();

		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	@GetMapping("/exception/{id}")
	public AcceptedExceptionBean getExceptionBean( // get particluar exception
			@PathVariable Long id, // id
			Principal principal // principal
	) {
		UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
		Long orgUnitId = userPrinciple.getOrgUnit().getId();

		System.out.println("Org Unit - " + userPrinciple.getOrgUnit());
		Optional<AcceptedExceptionBean> res = exceptionRepository.findByIdAndOrgUnitId(id, orgUnitId);
		if (!res.isPresent()) {
			throw new ExceptionNotFound("Exception with id -> " + id + " does not exist");
		}

		return res.get();
	}

	@PatchMapping("/exception/{id}")
	public AcceptedExceptionBean updateExceptionBean( // update exception
			@PathVariable Long id, // id
			@RequestBody ExceptionBeanUpdate update, // update request
			Principal principal // principal
	) {
		UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
		Long orgUnitId = userPrinciple.getOrgUnit().getId();

		System.out.println("Org Unit - " + userPrinciple.getOrgUnit());
		Optional<AcceptedExceptionBean> res = exceptionRepository.findByIdAndOrgUnitId(id, orgUnitId);
		if (!res.isPresent()) {
			throw new ExceptionNotFound("Exception with id -> " + id + " does not exist");
		}

		AcceptedExceptionBean exceptionBean = res.get();
		long millis = System.currentTimeMillis();

		exceptionBean.setComment(update.getComment());
		exceptionBean.setStatus(update.getStatus());
		exceptionBean.setUpdateTime(new Timestamp(millis));

		exceptionRepository.save(exceptionBean);

		return exceptionBean;
	}
}