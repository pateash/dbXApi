package com.example.dbx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

import com.example.dbx.exception.ExceptionNotFound;
import com.example.dbx.exception.InvalidException;
import com.example.dbx.model.RejectedExceptionBean;
import com.example.dbx.model.RejectedExceptionsResult;
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

@RequiredArgsConstructor
@Service
public class RejectedExceptionsService {
    private final ExceptionRepository exceptionRepository;
    private final RejectedExceptionRepository rejectedExceptionRepository;
    private final OrgUnitRepository orgUnitRepository;
    private final BusinessComponentRepository businessComponentRepository;

    private Direction getDirection(String order) {
        return (order != null && order.toLowerCase().contains("des")) ? Direction.DESC : Direction.ASC;
    }

    public RejectedExceptionsResult exceptions(String sort, String order, Integer page, Integer pageSize,
            ExceptionFilter filter) {

        List<Order> orders = new ArrayList<>();
        RejectedExceptionsResult result = new RejectedExceptionsResult();

        if (filter.getSeverityOrder() != null) {
            Direction direction = getDirection(filter.getSeverityOrder());
            orders.add(new Order(direction, "severity"));
        }

        if (sort != null) {
            Direction direction = getDirection(order);
            orders.add(new Order(direction, sort));
        }

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
                ExceptionStatus status = filter.getStatus().equals("resolved") ? ExceptionStatus.STATUS_RESOLVED
                        : ExceptionStatus.STATUS_UNRESOLVED;
                pageRes = rejectedExceptionRepository
                        .findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverityAndStatus(
                                filter.getSource(), filter.getCategory(), severe, status, pageReq);
            } else {
                pageRes = rejectedExceptionRepository
                        .findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndSeverity(filter.getSource(),
                                filter.getCategory(), severe, pageReq);
            }
        } else if (filter.getStatus() != null) {
            ExceptionStatus status = filter.getStatus().equals("resolved") ? ExceptionStatus.STATUS_RESOLVED
                    : ExceptionStatus.STATUS_UNRESOLVED;
            pageRes = rejectedExceptionRepository
                    .findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndStatus(filter.getSource(),
                            filter.getCategory(), status, pageReq);
        } else {
            pageRes = rejectedExceptionRepository.findBySourceContainingIgnoreCaseAndCategoryContainingIgnoreCase(
                    filter.getSource(), filter.getCategory(), pageReq);
        }

        result.setRejectedExceptions(pageRes.getContent());
        result.setTotalElements(pageRes.getTotalElements());

        return result;
    }

    public RejectedExceptionBean getExceptionBean(Long id) {
        Optional<RejectedExceptionBean> res = rejectedExceptionRepository.findById(id);
        if (!res.isPresent()) {
            throw new ExceptionNotFound("Exception with id -> " + id + " does not exist");
        }

        return res.get();
    }

    public RejectedExceptionBean updateExceptionBean(Long id) {
        Optional<RejectedExceptionBean> res = rejectedExceptionRepository.findById(id);
        if (!res.isPresent()) {
            throw new ExceptionNotFound("Exception with id -> " + id + " does not exist");
        }

        RejectedExceptionBean externalException = res.get();

        externalException.setBusinessComponent(externalException.getBusinessComponent().replace(",-", ""));
        externalException.setOrgUnit(externalException.getOrgUnit().replace(",-", ""));

        OrgUnit orgUnit = orgUnitRepository.findByName(externalException.getOrgUnit());
        BusinessComponent businessComponent = businessComponentRepository.findByNameAndOrgUnitIdAndIsEnabled(
                externalException.getBusinessComponent(),
                (orgUnit != null && orgUnit.getId() != null) ? orgUnit.getId() : -1, true);
        if (orgUnit == null || businessComponent == null) {
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
