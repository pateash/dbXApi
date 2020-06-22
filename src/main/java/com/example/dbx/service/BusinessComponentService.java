package com.example.dbx.service;

import java.util.Optional;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.model.BusinessComponent;
import com.example.dbx.model.BusinessComponentsResult;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.UserRole;
import com.example.dbx.repository.BusinessComponentRepository;
import com.example.dbx.repository.OrgUnitRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BusinessComponentService {
    private final BusinessComponentRepository businessComponentRepository;
    private final OrgUnitRepository orgUnitRepository;

    public static String notExistsMsg(String id) {
        return String.format("Business-Component -> %s does not Exist", id);
    }

    public static String notExistsMsg(Long id) {
        return notExistsMsg(id.toString());
    }

    public BusinessComponentsResult getAllBusinessComponent(int page, int pageSize, UserRole userRole, Long orgUnitId) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        Page<BusinessComponent> pageResult;

        if (userRole == UserRole.ROLE_ADMIN) {
            pageResult = businessComponentRepository.findAll(pageRequest);
        } else {
            pageResult = businessComponentRepository.findByOrgUnitId(orgUnitId, pageRequest);
        }

        return new BusinessComponentsResult(pageResult.getContent(), pageResult.getTotalElements());
    }

    public BusinessComponent getBusinessComponent(Long id, UserRole userRole, Long orgUnitId) {
        Optional<BusinessComponent> component;

        if (userRole == UserRole.ROLE_ADMIN) {
            component = businessComponentRepository.findById(id);
        } else {
            component = businessComponentRepository.findOneByIdAndOrgUnitId(id, orgUnitId);
        }

        if (!component.isPresent()) {
            throw new InvalidException(BusinessComponentService.notExistsMsg(id));
        }

        return component.get();
    }

    public BusinessComponent addBusinessComponent(BusinessComponent businessComponent, Long orgUnitId) {
        Optional<OrgUnit> orgUnit = orgUnitRepository.findById(orgUnitId);
        if (!orgUnit.isPresent()) {
            throw new InvalidException(OrgUnitService.notExistsMsg(orgUnitId));
        }

        return businessComponentRepository
                .save(new BusinessComponent(null, businessComponent.getName(), orgUnit.get(), false, 0l));
    }

    public BusinessComponent updateBusinessComponent(Long id, BusinessComponent businessComponentUpdate) {
        Optional<BusinessComponent> businessComponent = businessComponentRepository.findById(id);
        if (!businessComponent.isPresent()) {
            throw new InvalidException(BusinessComponentService.notExistsMsg(id));
        }

        BusinessComponent newBusinessComponent = businessComponent.get();
        newBusinessComponent.setIsEnabled(businessComponentUpdate.getIsEnabled());
        return businessComponentRepository.save(newBusinessComponent);
    }

    public String deleteBusinessComponent(Long id) {
        businessComponentRepository.deleteById(id);
        return "Business Component deleted successfully!";
    }
}