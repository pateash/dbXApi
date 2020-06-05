package com.example.dbx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dbx.exception.InvalidQueryException;
import com.example.dbx.model.ExceptionBean;
import com.example.dbx.repository.ExceptionRepository;
// import com.example.dbx.repository.OrgUnitRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
public class ExceptionController {

	@Autowired
	private ExceptionRepository exceptionRepository;

	/* @Autowired
	private OrgUnitRepository orgUnitRepository; */

	@GetMapping("/exception/source/{sourceName}/{pageNumber}/{pageSize}")
	public List<ExceptionBean> getExceptionsBySource(@PathVariable String sourceName, @PathVariable int pageNumber,
			@PathVariable int pageSize) {
		List<ExceptionBean> expList = exceptionRepository.findAllBySource(sourceName,
				PageRequest.of(pageNumber, pageSize, Sort.by("id").descending()));

		if (expList.size() == 0) {
			throw new InvalidQueryException("Source: " + sourceName + " is invalid");
		}

		return expList;
	}

	@GetMapping("/exception/category/{categoryName}/{pageNumber}/{pageSize}")
	public List<ExceptionBean> getExceptionsByCategory(@PathVariable String categoryName, @PathVariable int pageNumber,
			@PathVariable int pageSize) {
		List<ExceptionBean> expList = exceptionRepository.findAllByCategory(categoryName,
				PageRequest.of(pageNumber, pageSize, Sort.by("id").descending()));

		if (expList.size() == 0) {
			throw new InvalidQueryException("Category: " + categoryName + " is invalid");
		}

		return expList;
	}

	// This returns all the exceptions without any filter and without any pagenation
	@GetMapping("/exception")
	public List<ExceptionBean> getAllExceptions() {
		return exceptionRepository.findAll();
	}

	// This returns all the exceptions without any filter but with pagenation
	@GetMapping("/exception/{pageNumber}/{pageSize}")
	public List<ExceptionBean> getAllExceptionsPagenated(@PathVariable int pageNumber, @PathVariable int pageSize) {

		List<ExceptionBean> exp = exceptionRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();

		return exp;
	}
}