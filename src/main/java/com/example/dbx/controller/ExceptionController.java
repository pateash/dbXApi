package com.example.dbx.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.math.BigInteger;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.example.dbx.exception.ExceptionNotFound;
import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.BusinessComponent;
import com.example.dbx.model.DayWiseSeverityCount;
import com.example.dbx.model.DayWiseSeverityCountWrapper;
import com.example.dbx.model.ExceptionBeanUpdate;
import com.example.dbx.model.ExceptionCategoryCount;
import com.example.dbx.model.ExceptionFilter;
import com.example.dbx.model.ExceptionSeverity;
import com.example.dbx.model.ExceptionStatus;
import com.example.dbx.model.ExceptionSummary;
import com.example.dbx.model.ExceptionsResult;
import com.example.dbx.model.OldExceptionBean;
import com.example.dbx.model.OldExceptionsResult;
import com.example.dbx.repository.BusinessComponentRepository;
import com.example.dbx.repository.ExceptionRepository;
import com.example.dbx.repository.ExceptionSummaryRepository;
import com.example.dbx.repository.OldExceptionRepository;
import com.example.dbx.security.services.UserPrinciple;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
public class ExceptionController {

	@Autowired
	private ExceptionRepository exceptionRepository;

	@Autowired
	private OldExceptionRepository oldExceptionRepository;

	@Autowired
	private BusinessComponentRepository businessComponentRepository;

	@Autowired
	private ExceptionSummaryRepository exceptionSummaryRepository;

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

		List<Order> orders = new ArrayList<>();
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
			ExceptionSeverity severe = ExceptionSeverity.SEVERITY_LOW;
			if (filter.getSeverity().equals("medium")) {
				severe = ExceptionSeverity.SEVERITY_MEDIUM;
			} else if (filter.getSeverity().equals("high")) {
				severe = ExceptionSeverity.SEVERITY_HIGH;
			}

			if (filter.getStatus() != null) {
				ExceptionStatus status = filter.getStatus().equals("resolved") ? ExceptionStatus.STATUS_RESOLVED
						: ExceptionStatus.STATUS_UNRESOLVED;
				pageRes = exceptionRepository
						.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndStatusAndOrgUnitId(
								filter.getSource(), filter.getCategory(), severe, status, id, pageReq);
			} else {
				pageRes = exceptionRepository
						.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndOrgUnitId(
								filter.getSource(), filter.getCategory(), severe, id, pageReq);
			}
		} else if (filter.getStatus() != null) {
			ExceptionStatus status = filter.getStatus().equals("resolved") ? ExceptionStatus.STATUS_RESOLVED
			: ExceptionStatus.STATUS_UNRESOLVED;
			pageRes = exceptionRepository
					.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndStatusAndOrgUnitId(
							filter.getSource(), filter.getCategory(), status, id, pageReq);
		} else {
			pageRes = exceptionRepository.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndOrgUnitId(
					filter.getSource(), filter.getCategory(), id, pageReq);
		}

		result.setExceptions(pageRes.getContent());
		result.setTotalElements(pageRes.getTotalElements());

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/exception/{id}")
	public AcceptedExceptionBean getExceptionBean( // get particluar exception
			@PathVariable Long id, // id
			Principal principal // principal
	) {
		UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
		Long orgUnitId = userPrinciple.getOrgUnit().getId();

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

		Optional<AcceptedExceptionBean> opt = exceptionRepository.findByIdAndOrgUnitId(id, orgUnitId);
		if (!opt.isPresent()) {
			throw new ExceptionNotFound("Exception with id -> " + id + " does not exist");
		}
		BusinessComponent businessComponent = businessComponentRepository
				.findByIdAndOrgUnitIdAndIsEnabled(update.getBusinessComponent().getId(), orgUnitId, true);
		AcceptedExceptionBean res = opt.get();

		long millis = System.currentTimeMillis();

		OldExceptionBean exceptionBean = new OldExceptionBean(res.getId(), res.getSeverity(),
				res.getBusinessComponent(), res.getTechnicalDescription(), res.getStatus(), res.getComment());

		oldExceptionRepository.save(exceptionBean); // Saving the old exception in database

		// Updating the exception
		res.setSeverity(update.getSeverity());
		res.setStatus(update.getStatus());
		res.setBusinessComponent(businessComponent);
		res.setTechnicalDescription(update.getTechnicalDescription());
		res.setComment(update.getComment());
		res.setUpdateTime(new Timestamp(millis));

		// Uncomment this
		res = exceptionRepository.save(res);

		return res;

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

		Optional<AcceptedExceptionBean> res = exceptionRepository.findByIdAndOrgUnitId(id, orgUnitId);
		if (!res.isPresent()) {
			throw new ExceptionNotFound("Exception with id -> " + id + " does not exist");
		}

		PageRequest pageReq = PageRequest.of(page, pageSize, Sort.by(Direction.DESC, "id"));
		Page<OldExceptionBean> pageRes = oldExceptionRepository.findByExceptionId(res.get().getId(), pageReq);
		List<OldExceptionBean> exceptionBeans = pageRes.getContent();
		int idx = exceptionBeans.size();

		for (OldExceptionBean oldExceptionBean : exceptionBeans) {
			oldExceptionBean.setVersion(idx--);
		}

		return new OldExceptionsResult(exceptionBeans, pageRes.getTotalElements());
	}

	// Change the return type to ExceptionSummary
	@GetMapping("/exception/summary")
	public ExceptionSummary getExceptionSummary(Principal principal) throws ParseException {
		// Extracting the org unit ID from UserPrincipal
		UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
		Long orgUnitId = userPrinciple.getOrgUnit().getId();

		// Validate the orgUnitID

		List<ExceptionCategoryCount> exceptionCategoryCount = new ArrayList<>();

		List<Object[]> res = exceptionSummaryRepository.findExceptionCountByCategory(orgUnitId);

		// Converting the BigInteger value to Long so that it can be stored as per the
		// data type of the variable -> categoryCount
		for (Object[] obj : res) {
			BigInteger x = (BigInteger) obj[1];
			exceptionCategoryCount.add(new ExceptionCategoryCount((String) obj[0], x.longValue()));
		}

		// Querying the database
		Long totalExceptions = exceptionSummaryRepository.findTotalExceptions(orgUnitId);
		Long totalUnresolvedExceptions = exceptionSummaryRepository.findTotalUnresolvedExceptions(orgUnitId);
		Long totalResolvedExceptions = exceptionSummaryRepository.findTotalResolvedExceptions(orgUnitId);
		Long totalLowSeverityExceptions = exceptionSummaryRepository.findTotalLowSeverityExceptions(orgUnitId);
		Long totalMediumSeverityExceptions = exceptionSummaryRepository.findTotalMediumSeverityExceptions(orgUnitId);
		Long totalHighSeverityExceptions = exceptionSummaryRepository.findTotalHighSeverityExceptions(orgUnitId);

		/**************************************************************************************************************/
		// Code for past 7 days severity count day wise
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		List<DayWiseSeverityCountWrapper> dayWiseSeverityWrapper = new ArrayList<>();

		String after = null;
		String before = null;

		for (int i = 1; i <= 7; i++) {
			after = dateFormat.format(cal.getTime());
			cal.add(Calendar.DATE, -1);
			before = dateFormat.format(cal.getTime());

			List<Object[]> response1 = exceptionSummaryRepository.findExceptionCountByDate(dateFormat.parse(before),
					dateFormat.parse(after), orgUnitId);
			List<DayWiseSeverityCount> dayWiseCount = new ArrayList<>();

			for (Object[] obj : response1) {
				BigInteger x = (BigInteger) obj[1];
				dayWiseCount.add(new DayWiseSeverityCount((Integer) obj[0], x.longValue()));
			}

			dayWiseSeverityWrapper.add(new DayWiseSeverityCountWrapper(before, dayWiseCount));
		}
		/**********************************************************************************************************/
		// Creating the response object using constructor
		return new ExceptionSummary(totalExceptions, totalResolvedExceptions,
				totalUnresolvedExceptions, totalLowSeverityExceptions, totalMediumSeverityExceptions,
				totalHighSeverityExceptions, exceptionCategoryCount, dayWiseSeverityWrapper);
	}
}