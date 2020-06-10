package com.example.dbx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.dbx.exception.ExceptionNotFound;
import com.example.dbx.exception.InvalidQueryException;
import com.example.dbx.model.AcceptedExceptionBean;
import com.example.dbx.model.UpdateCommentRequest;
import com.example.dbx.repository.ExceptionRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
public class UpdateAPIs {
	
	@Autowired
	ExceptionRepository exceptionRepository;
	
	@PostMapping("/exception")
	public ResponseEntity<Object> updateComment(
		@RequestParam(value="update") String update ,
		@RequestParam(value="value" , required=false) String value,
		@RequestParam(value="id") Integer id,
		@RequestBody UpdateCommentRequest updateCommentRequest
			){
		
		switch(update) {
		case "comment":
			AcceptedExceptionBean commentUpdate  = exceptionRepository.getOne(id);
			if(commentUpdate == null) {
				throw new ExceptionNotFound("Exception with id -> "+  id + " does not exist");
			}
			
			commentUpdate.setComment(value);
			
			exceptionRepository.save(commentUpdate);
			
			return new ResponseEntity("Updated" , HttpStatus.OK);
			
		case "status":
			AcceptedExceptionBean statusUpdate  = exceptionRepository.getOne(id);
			if(statusUpdate == null) {
				throw new ExceptionNotFound("Exception with id -> "+  id + " does not exist");
			}
			
			statusUpdate.setStatus(1);
			
			exceptionRepository.save(statusUpdate);
			
			return new ResponseEntity("Updated" , HttpStatus.OK);
		
			
		default:
			
			throw new InvalidQueryException("Invalid Query");
		} 
		
	}
}
