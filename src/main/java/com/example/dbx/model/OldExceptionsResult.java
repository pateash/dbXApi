package com.example.dbx.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OldExceptionsResult {
	List<OldExceptionBean> oldExceptions;
	Long totalElements;
}