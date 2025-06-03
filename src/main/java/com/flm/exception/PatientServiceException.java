package com.flm.exception;

public class PatientServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PatientServiceException (String message) {
		super(message);
	}
}
