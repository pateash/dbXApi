package com.example.dbx.model;

import lombok.Data;

//List of objects of this class is required in ExceptionSummary class
@Data
public class ExceptionCategoryCount {
	private String category;
	
	private Long categoryCount;
	
	public ExceptionCategoryCount(String category, Long categoryCount) {
		super();
		this.category = category;
		this.categoryCount = categoryCount;
	}
	public ExceptionCategoryCount() {
		super();
	}
	
	
}
