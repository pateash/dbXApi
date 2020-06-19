package com.example.dbx.controller;

import java.security.Principal;

import javax.validation.Valid;

import com.example.dbx.model.BusinessComponent;
import com.example.dbx.model.BusinessComponentsResult;
import com.example.dbx.security.services.UserPrinciple;
import com.example.dbx.service.BusinessComponentService;

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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class BusinessComponentController {
    private final BusinessComponentService businessComponentService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/businessComponent")
    public BusinessComponentsResult getAllBusinessComponent(@RequestParam(required = true, defaultValue = "0") int page,
            @RequestParam(required = true, defaultValue = "5000") int pageSize, Principal principal) {
        UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);

        return businessComponentService.getAllBusinessComponent(page, pageSize, userPrinciple.getUserRole(),
                userPrinciple.getOrgUnit().getId());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/businessComponent/{id}")
    public BusinessComponent getBusinessComponent(@PathVariable("id") Long id, Principal principal) {
        UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);

        return businessComponentService.getBusinessComponent(id, userPrinciple.getUserRole(),
                userPrinciple.getOrgUnit().getId());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/businessComponent")
    public BusinessComponent addBusinessComponent(@Valid @RequestBody BusinessComponent businessComponent,
            Principal principal) {
        UserPrinciple userPrinciple = UserPrinciple.extractFromPrincipal(principal);

        return businessComponentService.addBusinessComponent(businessComponent, userPrinciple.getOrgUnit().getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/businessComponent/{id}")
    public BusinessComponent updateBusinessComponent(@PathVariable("id") Long id,
            @Valid @RequestBody BusinessComponent businessComponentUpdate) {
        return businessComponentService.updateBusinessComponent(id, businessComponentUpdate);
    }

    @DeleteMapping("/businessComponent/{id}")
    public String deleteBusinessComponent(@PathVariable("id") Long id) {
        return businessComponentService.deleteBusinessComponent(id);
    }
}