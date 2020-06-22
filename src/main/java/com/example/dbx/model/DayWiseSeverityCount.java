package com.example.dbx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Day wise severity count is stored as List of objects of this class.
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DayWiseSeverityCount {
	int severity;
	Long count;
}
