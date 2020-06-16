package com.example.dbx.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.model.BusinessComponent;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.UserRole;
import com.example.dbx.repository.BusinessComponentRepository;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.security.services.UserPrinciple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@Data
class BusinessComponentsResult {
    List<BusinessComponent> businessComponents;
    Long totalElements;

    public BusinessComponentsResult(List<BusinessComponent> businessComponents, Long totalElements) {
        this.businessComponents = businessComponents;
        this.totalElements = totalElements;
    }
}

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class BusinessComponentApi {
    @Autowired
    private BusinessComponentRepository businessComponentRepository;

    @Autowired
    private OrgUnitRepository orgUnitRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/businessComponent")
    public BusinessComponentsResult getAllBusinessComponent(@RequestParam(required = true, defaultValue = "0") int page,
            @RequestParam(required = true, defaultValue = "5000") int pageSize, Principal principal) {
        UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
        Pageable pageRequest = PageRequest.of(page, pageSize);
        Page<BusinessComponent> pageResult;

        if (userPrinciple.getUserRole() == UserRole.ROLE_ADMIN) {
            pageResult = businessComponentRepository.findAll(pageRequest);
        } else {
            pageResult = businessComponentRepository.findByOrgUnitId(userPrinciple.getOrgUnit().getId(), pageRequest);
        }

        BusinessComponentsResult result = new BusinessComponentsResult(pageResult.getContent(),
                pageResult.getTotalElements());

        return result;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/businessComponent/{id}")
    public BusinessComponent getBusinessComponent(@PathVariable("id") Long id, Principal principal) {
        Optional<BusinessComponent> component;
        UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);

        if (userPrinciple.getUserRole() == UserRole.ROLE_ADMIN) {
            component = businessComponentRepository.findOneById(id);
        } else {
            component = businessComponentRepository.findOneByIdAndOrgUnitId(id, userPrinciple.getOrgUnit().getId());
        }

        if (!component.isPresent()) {
            throw new InvalidException("Business-Component -> " + id + " does not Exist");
        }

        return component.get();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/businessComponent")
    public BusinessComponent addBusinessComponent(@Valid @RequestBody BusinessComponent businessComponent,
            Principal principal) {
        UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);
        Optional<OrgUnit> orgUnit = orgUnitRepository.findById(userPrinciple.getOrgUnit().getId());
        if (!orgUnit.isPresent()) {
            throw new InvalidException("Org-Unit -> " + businessComponent.getOrgUnitCreateId() + " does not Exist");
        }
        BusinessComponent newBusinessComponent = new BusinessComponent(businessComponent.getName(), orgUnit.get());
        businessComponentRepository.save(newBusinessComponent);
        return newBusinessComponent;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/businessComponent/{id}")
    public BusinessComponent updateBusinessComponent(@PathVariable("id") Long id,
            @Valid @RequestBody BusinessComponent businessComponentUpdate) {
        Optional<BusinessComponent> businessComponent = businessComponentRepository.findById(id);
        if (!businessComponent.isPresent()) {
            throw new InvalidException("Business-Component -> " + id + " does not Exist");
        }
        BusinessComponent newBusinessComponent = businessComponent.get();

        newBusinessComponent.setIsEnabled(businessComponentUpdate.getIsEnabled());
        businessComponentRepository.save(newBusinessComponent);

        return newBusinessComponent;
    }

    @DeleteMapping("/businessComponent/{id}")
    public ResponseEntity<String> deleteBusinessComponent(@PathVariable("id") Long id) {
        businessComponentRepository.deleteById(id);
        return ResponseEntity.ok().body("Business Component deleted successfully!");
    }
}