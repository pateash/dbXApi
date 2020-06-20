package com.example.dbx.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//This is the response of GET "/api/exception/summary"
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionSummary {
	private Long totalExceptions;
	private Long totalResolvedExceptions;
	private Long totalUnresolvedExceptions;
	private Long totalLowSeverityExceptions;
	private Long totalMediumSeverityExceptions;
	private Long totalHighSeverityExceptions;
	private List<ExceptionCategoryCount> exceptionCategoryCount;
	private List<DayWiseSeverityCountWrapper> dayWiseSeverityCountWrapper;	
}
