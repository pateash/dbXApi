package com.example.dbx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionBeanUpdate {
	private ExceptionSeverity severity;
	private ExceptionStatus status;
	private BusinessComponent businessComponent;
	private String technicalDescription;
	private String comment;
}
