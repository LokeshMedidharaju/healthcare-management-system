package com.flm.exception;

public class PasswordDoesNotExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PasswordDoesNotExistsException(String message) {
		super(message);
	}
}
