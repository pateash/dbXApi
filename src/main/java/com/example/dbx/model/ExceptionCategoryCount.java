package com.example.dbx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//List of objects of this class is required in ExceptionSummary class
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionCategoryCount {
	private String category;
	private Long categoryCount;
}
