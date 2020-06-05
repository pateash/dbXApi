package com.example.dbx.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidException extends RuntimeException {
	
	public InvalidException(String message) {
		super(message);
	}
}