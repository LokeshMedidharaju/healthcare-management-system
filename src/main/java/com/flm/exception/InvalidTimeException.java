package com.flm.exception;

public class InvalidTimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidTimeException(String message) {
		super(message);
	}
}
