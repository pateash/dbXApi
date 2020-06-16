package com.example.dbx.model;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

public interface ExceptionCategoryModel {
	String getCategory();
	Long getCount();
}
