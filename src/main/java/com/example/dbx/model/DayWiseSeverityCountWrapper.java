package com.example.dbx.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Wrapper class where day wise severity count is maintained
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DayWiseSeverityCountWrapper {
	String day;
	List<DayWiseSeverityCount> dayWiseSeverityCount;
}
