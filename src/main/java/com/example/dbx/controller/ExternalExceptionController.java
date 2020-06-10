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
import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.ExternalException;
import com.example.dbx.model.RejectedExceptionBean;
import com.example.dbx.repository.ExceptionRepository;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.RejectedExceptionRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ExternalExceptionController {
    @Autowired
    private ExceptionRepository exceptionRepository;

    @Autowired
    private OrgUnitRepository orgUnitRepository;
    
    @Autowired
    private RejectedExceptionRepository rejectedExceptionRepository;

    @PostMapping("/exception")
    public ResponseEntity<Object> addException(@RequestBody ExternalException externalException) {
        // STEP1: Validate the incoming exception.
        // STEP2: Fill in the empty fields required for the database
        // STEP3: Store the exception in the database
        // STEP4: Return a relevant response
    	
    	//Encoding severity string to numbers:
    	Integer severe = null;
    	switch(externalException.getSeverity()) {
    		case "low":
    			severe = 0;
    			break;
    		case "medium":
    			severe = 1;
    			break;
    		case "high":
    			severe = 2;
    			break;
    	}
    	

        // STEP1: Validating the incoming request:
        if (orgUnitRepository.findByName(externalException.getOrgUnit()) == null) {
        	//Incoming request is invalid
        	
        	long millis = System.currentTimeMillis();
        	
        	RejectedExceptionBean rejectedException = new RejectedExceptionBean(
        			null, //id , will be auto generated, hence setting value as null 
        			new Date(millis), //timeGenerated
        			externalException.getSource(), //source
        			externalException.getCategory(), //category
        			externalException.getDescription(), //description
        			severe, //severity
        			externalException.getBusinessComponent(), //businessComponent
        			externalException.getOrgUnit(), //orgUnit
        			externalException.getTechnicalDescription(), //technical description
        			0, //status
        			null, //updateTime 
        			null //comment
        			);
        	
        	//Adding the rejected exception to 'rejected_exception' database
        	rejectedExceptionRepository.save(rejectedException);
        	
            throw new InvalidException("Org-Unit -> " + externalException.getOrgUnit() + " does not Exist");
        }
        
        //Incoming exception is valid
        long millis = System.currentTimeMillis();
        
        AcceptedExceptionBean acceptedException = new AcceptedExceptionBean(
        		null, //id , will be auto generated, hence setting value as null 
    			new Date(millis), //timeGenerated
    			externalException.getSource(), //source
    			externalException.getCategory(), //category
    			externalException.getDescription(), //description
    			severe, //severity
    			externalException.getBusinessComponent(), //businessComponent
    			externalException.getOrgUnit(), //orgUnit
    			externalException.getTechnicalDescription(), //technical description
    			0, //status
    			null, //updateTime 
    			null //comment
        		);

        // STEP3: Adding the exception to Database
        AcceptedExceptionBean savedException = exceptionRepository.save(acceptedException);

        // STEP4: Sending proper response back. We are sending the URL of the saved
        // exception and an HTTP Response of "CREATED"
        URI exceptionLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedException.getId()).toUri();

        return ResponseEntity.created(exceptionLocation).build();
    }

}

//eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhcHVydiIsImlhdCI6MTU5MTYwNzM2MiwiZXhwIjoxNTkxNjkzNzYyfQ.hHd2lX8E0OJh4B2Mieexr40IzLgASCmwyYdwwiNAssR3HwyOL50vEeeSIwLMNsJySDBaHlHCq57Br5xXOzTJZw