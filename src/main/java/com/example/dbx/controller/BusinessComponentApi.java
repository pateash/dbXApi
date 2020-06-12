package com.example.dbx.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import com.example.dbx.model.BusinessComponent;
import com.example.dbx.repository.BusinessComponentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
public class BusinessComponentApi {
    @Autowired
    BusinessComponentRepository businessComponentRepository;

    @GetMapping("/businessComponent")
    public BusinessComponentsResult getAllBusinessComponent(@RequestParam(required = true, defaultValue = "0") int page,
            @RequestParam(required = true, defaultValue = "5") int pageSize, Principal principal) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        Page<BusinessComponent> pageResult;

        pageResult = businessComponentRepository.findAll(pageRequest);

        BusinessComponentsResult result = new BusinessComponentsResult(pageResult.getContent(),
                pageResult.getTotalElements());

        return result;
    }

    @PostMapping("/businessComponent")
    public BusinessComponent addBusinessComponent(@Valid @RequestBody BusinessComponent businessComponent) {
        BusinessComponent newBusinessComponent = new BusinessComponent(businessComponent.getName());
        businessComponentRepository.save(newBusinessComponent);
        return newBusinessComponent;
    }

    @DeleteMapping("/businessComponent/{id}")
    public ResponseEntity<String> deleteBusinessComponent(@PathVariable("id") Long id) {
        businessComponentRepository.deleteById(id);
        return ResponseEntity.ok().body("Business Component deleted successfully!");
    }
}