package com.example.dbx.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

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

@RequiredArgsConstructor
@Service
public class ExceptionService {
	private final ExceptionRepository exceptionRepository;
	private final OldExceptionRepository oldExceptionRepository;
	private final BusinessComponentRepository businessComponentRepository;
	private final ExceptionSummaryRepository exceptionSummaryRepository;

	public static Direction getDirection(String order) {
		return (order != null && order.toLowerCase().contains("des")) ? Direction.DESC : Direction.ASC;
	}

	public static String notExistsMsg(Long id) {
		return String.format("Exception with id -> %s does not exist", id);
	}

	public ExceptionsResult exceptions( // All exceptions API
			String sort, // filter field
			String order, // filter value
			Integer page, // page no.
			Integer pageSize, // page size
			ExceptionFilter filter, // filter
			Long orgUnitId // orgUnitId
	) {
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
			ExceptionSeverity severe = filter.getExceptionSeverity();

			if (filter.getStatus() != null) {
				ExceptionStatus status = filter.getExceptionStatus();
				pageRes = exceptionRepository
						.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndStatusAndOrgUnitId(
								filter.getSource(), filter.getCategory(), severe, status, orgUnitId, pageReq);
			} else {
				pageRes = exceptionRepository
						.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndOrgUnitId(
								filter.getSource(), filter.getCategory(), severe, orgUnitId, pageReq);
			}
		} else if (filter.getStatus() != null) {
			ExceptionStatus status = filter.getExceptionStatus();
			pageRes = exceptionRepository
					.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndStatusAndOrgUnitId(
							filter.getSource(), filter.getCategory(), status, orgUnitId, pageReq);
		} else {
			pageRes = exceptionRepository.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndOrgUnitId(
					filter.getSource(), filter.getCategory(), orgUnitId, pageReq);
		}

		result.setExceptions(pageRes.getContent());
		result.setTotalElements(pageRes.getTotalElements());

		return result;
	}

	public AcceptedExceptionBean getExceptionBean( // get particluar exception
			Long id, // id
			Long orgUnitId // orgUnitId
	) {
		Optional<AcceptedExceptionBean> res = exceptionRepository.findByIdAndOrgUnitId(id, orgUnitId);
		if (!res.isPresent()) {
			throw new ExceptionNotFound(notExistsMsg(id));
		}

		return res.get();
	}

	public AcceptedExceptionBean updateExceptionBean( // update exception
			Long id, // id
			ExceptionBeanUpdate update, // update request
			Long orgUnitId // principal
	) {
		Optional<AcceptedExceptionBean> opt = exceptionRepository.findByIdAndOrgUnitId(id, orgUnitId);
		if (!opt.isPresent()) {
			throw new ExceptionNotFound(notExistsMsg(id));
		}
		BusinessComponent businessComponent = businessComponentRepository
				.findByIdAndOrgUnitIdAndIsEnabled(update.getBusinessComponent().getId(), orgUnitId, true);
		AcceptedExceptionBean res = opt.get();

		Timestamp now = new Timestamp(System.currentTimeMillis());

		OldExceptionBean exceptionBean = new OldExceptionBean(null, res.getId(), 0, res.getSeverity(),
				res.getBusinessComponent(), res.getTechnicalDescription(), res.getStatus(), now, res.getComment());

		oldExceptionRepository.save(exceptionBean); // Saving the old exception in database

		// Updating the exception
		res.setSeverity(update.getSeverity());
		res.setStatus(update.getStatus());
		res.setBusinessComponent(businessComponent);
		res.setTechnicalDescription(update.getTechnicalDescription());
		res.setComment(update.getComment());
		res.setUpdateTime(now);

		// Uncomment this
		res = exceptionRepository.save(res);

		return res;

	}

	public OldExceptionsResult getExceptionVersions( // get particluar exception
			Long id, // id
			Long orgUnitId, // orgUnitId
			Integer page, // page no.
			Integer pageSize // page size
	) {
		Optional<AcceptedExceptionBean> res = exceptionRepository.findByIdAndOrgUnitId(id, orgUnitId);
		if (!res.isPresent()) {
			throw new ExceptionNotFound(notExistsMsg(id));
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

	public ExceptionSummary getExceptionSummary(Long orgUnitId) throws ParseException {
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
		return new ExceptionSummary(totalExceptions, totalResolvedExceptions, totalUnresolvedExceptions,
				totalLowSeverityExceptions, totalMediumSeverityExceptions, totalHighSeverityExceptions,
				exceptionCategoryCount, dayWiseSeverityWrapper);
	}
}