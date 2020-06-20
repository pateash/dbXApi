package com.example.dbx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Every incoming exception will be mapped to an object of this class
public class ExternalException {
	private String source; 
	private String category;
	private String description;
	private String severity;
	private String businessComponent;
	private String orgUnit;
	private String technicalDescription;
}
