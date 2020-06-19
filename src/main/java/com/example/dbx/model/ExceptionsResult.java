package com.example.dbx.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionsResult {
	List<AcceptedExceptionBean> exceptions;
	Long totalElements;
}
