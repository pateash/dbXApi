package com.example.dbx.model;

import lombok.Data;
import lombok.NoArgsConstructor;

//Day wise severity count is stored as List of objects of this class.
@NoArgsConstructor
@Data
public class DayWiseSeverityCount {
	int severity;
	
	Long count;

	public DayWiseSeverityCount(int severity, Long count) {
		super();
		this.severity = severity;
		this.count = count;
	}
}
