package com.example.dbx.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidQueryException extends RuntimeException {
	
	public InvalidQueryException(String message) {
		super(message);
	}
}
