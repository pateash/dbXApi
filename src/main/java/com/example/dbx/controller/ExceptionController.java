package com.example.dbx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

import com.example.dbx.exception.InvalidQueryException;
import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.repository.ExceptionRepository;
// import com.example.dbx.repository.OrgUnitRepository;

/**
 * ExceptionsResult
 */
@Data
class ExceptionsResult {
	List<AcceptedExceptionBean> exceptions;
	Long totalElements;

}

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
public class ExceptionController {

	@Autowired
	private ExceptionRepository exceptionRepository;
	
	/* 
		If the optional parameters are not passed, they are converted to null.
	*/
	@GetMapping("/exception")
	public @ResponseBody ResponseEntity<Object> testMapping(
			@RequestParam(value="filterBy" , required=false) String filterBy,
			@RequestParam(value="filterName" , required=false) String filterName,
			@RequestParam(value="page" , required=false) Integer page,
			@RequestParam(value="size" , required=false) Integer size
			) {
		
		ExceptionsResult result = new ExceptionsResult();
		
		
		//Switch case expression need to be a constant, it cannot be null. Hence writing filterBy=null case outside the switch case
		if(filterBy==null) {
			result.exceptions = exceptionRepository.findAll();
			result.totalElements = exceptionRepository.count();
			return new ResponseEntity<Object>(result , HttpStatus.FOUND);
		}
		
		switch(filterBy) {
		
			case "category":
				result.exceptions = exceptionRepository.findAllByCategory(filterName, PageRequest.of(page, size, Sort.by("id").descending()));
				result.totalElements = exceptionRepository.count();
				return new ResponseEntity<Object>(result , HttpStatus.FOUND);
				
			case "source":
				result.exceptions = exceptionRepository.findAllBySource(filterName, PageRequest.of(page, size, Sort.by("id").descending()));
				result.totalElements = exceptionRepository.count();
				return new ResponseEntity<Object>(result , HttpStatus.FOUND);
				
			case "severity":
				
				Integer severe = null;
				
				switch(filterName) {
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
				
				result.exceptions = exceptionRepository.findAllBySeverity(severe.intValue(), PageRequest.of(page, size, Sort.by("id").descending()));
				result.totalElements = exceptionRepository.count();
				return new ResponseEntity<Object>(result , HttpStatus.FOUND);
				
			default:
				
				result.exceptions = null;
				result.totalElements = Long(0);
				return new ResponseEntity<Object>(result , HttpStatus.BAD_REQUEST);
		}
		
	}

	//Convert integer to long.
	private Long Long(int i) {
		// TODO Auto-generated method stub
		return null;
	}
}