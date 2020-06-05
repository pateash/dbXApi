package com.example.dbx.controller;

import java.net.URI;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.exception.InvalidQueryException;
import com.example.dbx.model.ExceptionBean;
import com.example.dbx.repository.ExceptionRepository;
import com.example.dbx.repository.OrgUnitRepository;


@RestController
@CrossOrigin
public class ExceptionController {
	
	
	@Autowired
	private ExceptionRepository exceptionRepository;
	
	@Autowired
	private OrgUnitRepository orgUnitRepository;
	
	@PostMapping("/exception")
	public ResponseEntity<Object> addException(@RequestBody ExceptionBean exceptionBean) {
		//STEP1: Validate the incoming exception.
		//STEP2: Fill in the empty fields required for the database
		//STEP3: Store the exception in the database
		//STEP4: Return a relevant response
		
		//STEP1: Validating the incoming request:
		if(orgUnitRepository.findByName(exceptionBean.getOrgUnit()) == null) {
			throw new InvalidException("Org Unit" + exceptionBean.getOrgUnit() + " is invalid");
		}
		
		//STEP2: Setting all the empty fields
		long millis=System.currentTimeMillis();
		exceptionBean.setTimeGenerated(new Date(millis));
		
		exceptionBean.setStatus("unresolved");
		
		exceptionBean.setUpdateTime(null);
		
		exceptionBean.setComment(null);
		
		//STEP3: Adding the exception to Database
		ExceptionBean savedException = exceptionRepository.save(exceptionBean);
		
		//STEP4: Sending proper response back. We are sending the URL of the saved exception and an HTTP Response of "CREATED"
		URI exceptionLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedException.getId()).toUri();
		
		return ResponseEntity.created(exceptionLocation).build();
	}
	
	
	@GetMapping("/exception/source/{sourceName}/{pageNumber}/{pageSize}")
	public List<ExceptionBean> getExceptionsBySource(@PathVariable String sourceName, @PathVariable int pageNumber , @PathVariable int pageSize){
		List<ExceptionBean> expList = exceptionRepository.findAllBySource(sourceName, PageRequest.of(pageNumber , pageSize , Sort.by("id").descending()));
		
		if(expList.size() == 0) {
			throw new InvalidQueryException("Source: " + sourceName + " is invalid");
		}
		
		return expList;
	}
	
	@GetMapping("/exception/category/{categoryName}/{pageNumber}/{pageSize}")
	public List<ExceptionBean> getExceptionsByCategory(@PathVariable String categoryName, @PathVariable int pageNumber , @PathVariable int pageSize){
		List<ExceptionBean> expList = exceptionRepository.findAllByCategory(categoryName, PageRequest.of(pageNumber , pageSize , Sort.by("id").descending()));
		
		if(expList.size() == 0) {
			throw new InvalidQueryException("Category: " + categoryName + " is invalid");
		}
		
		return expList;
	}
	
	//This returns all the exceptions without any filter and without any pagenation
	@GetMapping("/exception")
	public List<ExceptionBean> getAllExceptions(){
		return exceptionRepository.findAll();
	}
	
	
	//This returns all the exceptions without any filter but with pagenation
	@GetMapping("/exception/{pageNumber}/{pageSize}")
	public List<ExceptionBean> getAllExceptionsPagenated(@PathVariable int pageNumber , @PathVariable int pageSize){
		
		List<ExceptionBean> exp =  exceptionRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
		
		return exp;
	}
}