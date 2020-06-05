package com.example.dbx.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidException extends RuntimeException {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 4507910610368331640L;

	public InvalidException(String message) {
		super(message);
	}
}