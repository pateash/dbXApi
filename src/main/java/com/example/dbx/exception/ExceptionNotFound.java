package com.example.dbx.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//To handle the exceptions of type-> Exception not found
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExceptionNotFound extends RuntimeException {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -2131816111272359329L;

	public ExceptionNotFound(String message) {
		super(message);
	}
}
