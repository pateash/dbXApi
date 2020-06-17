package com.example.dbx.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

//Wrapper class where day wise severity count is maintained
@NoArgsConstructor
@Data
public class DayWiseSeverityCountWrapper {
	String day;
	
	List<DayWiseSeverityCount> dayWiseSeverityCount;

	public DayWiseSeverityCountWrapper(String day, List<DayWiseSeverityCount> dayWiseSeverityCount) {
		super();
		this.day = day;
		this.dayWiseSeverityCount = dayWiseSeverityCount;
	}
	
	
}
