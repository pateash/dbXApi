package com.example.dbx.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidQueryException extends RuntimeException {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -8964320068247804636L;

	public InvalidQueryException(String message) {
		super(message);
	}
}
