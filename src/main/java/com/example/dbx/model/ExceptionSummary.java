package com.example.dbx.model;

import java.util.List;

import lombok.Data;

//This is the response of GET "/api/exception/summary"
@Data
public class ExceptionSummary {
	private Long totalExceptions;
	
	private Long totalResolvedExceptions;
	
	private Long totalUnresolvedExceptions;
	
	private Long totalLowSeverityExceptions;
	
	private Long totalMediumSeverityExceptions;
	
	private Long totalHighSeverityException;
	
	private List<ExceptionCategoryCount> exceptionCategoryCount;
	
	private List<DayWiseSeverityCountWrapper> dayWiseSeverityCountWrapper;

	public ExceptionSummary() {
		super();
	}

	public ExceptionSummary(Long totalExceptions, Long totalResolvedExceptions, Long totalUnresolvedExceptions,
			Long totalLowSeverityExceptions, Long totalMediumSeverityExceptions, Long totalHighSeverityException,
			List<ExceptionCategoryCount> exceptionCategoryCount,
			List<DayWiseSeverityCountWrapper> dayWiseSeverityCountWrapper) {
		super();
		this.totalExceptions = totalExceptions;
		this.totalResolvedExceptions = totalResolvedExceptions;
		this.totalUnresolvedExceptions = totalUnresolvedExceptions;
		this.totalLowSeverityExceptions = totalLowSeverityExceptions;
		this.totalMediumSeverityExceptions = totalMediumSeverityExceptions;
		this.totalHighSeverityException = totalHighSeverityException;
		this.exceptionCategoryCount = exceptionCategoryCount;
		this.dayWiseSeverityCountWrapper = dayWiseSeverityCountWrapper;
	}
	
}
