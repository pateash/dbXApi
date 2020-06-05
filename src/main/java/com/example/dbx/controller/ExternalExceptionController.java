package com.example.dbx.controller;

import java.net.URI;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.model.ExceptionBean;
import com.example.dbx.repository.ExceptionRepository;
import com.example.dbx.repository.OrgUnitRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ExternalExceptionController {
    @Autowired
    private ExceptionRepository exceptionRepository;

    @Autowired
    private OrgUnitRepository orgUnitRepository;

    @PostMapping("/exception")
    public ResponseEntity<Object> addException(@RequestBody ExceptionBean exceptionBean) {
        // STEP1: Validate the incoming exception.
        // STEP2: Fill in the empty fields required for the database
        // STEP3: Store the exception in the database
        // STEP4: Return a relevant response

        // STEP1: Validating the incoming request:
        if (orgUnitRepository.findByName(exceptionBean.getOrgUnit()) == null) {
            throw new InvalidException("Org Unit" + exceptionBean.getOrgUnit() + " is invalid");
        }

        // STEP2: Setting all the empty fields
        long millis = System.currentTimeMillis();
        exceptionBean.setTimeGenerated(new Date(millis));

        exceptionBean.setStatus("unresolved");

        exceptionBean.setUpdateTime(null);

        exceptionBean.setComment(null);

        // STEP3: Adding the exception to Database
        ExceptionBean savedException = exceptionRepository.save(exceptionBean);

        // STEP4: Sending proper response back. We are sending the URL of the saved
        // exception and an HTTP Response of "CREATED"
        URI exceptionLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedException.getId()).toUri();

        return ResponseEntity.created(exceptionLocation).build();
    }

}