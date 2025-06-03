package com.flm.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(StaffNotFoundException.class)
	public ResponseEntity<String> handleStaffNotFoundException(StaffNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(StaffServiceException.class)
	public ResponseEntity<String> handleStaffServiceException(StaffServiceException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DoctorNotFoundException.class)
	public ResponseEntity<String> handleDoctorNotFoundException(DoctorNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DoctorServiceException.class)
	public ResponseEntity<String> handleDoctorServiceException(DoctorServiceException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InvalidDateException.class)
	public InvalidDateException handleInvalidDateException(InvalidDateException e) {
		return new InvalidDateException(e.getMessage());
	}

	@ExceptionHandler(InvalidTimeException.class)
	public InvalidTimeException handleInvalidTimeException(InvalidTimeException e) {
		return new InvalidTimeException(e.getMessage());
	}

	@ExceptionHandler(DoctorUnavailableException.class)
	public DoctorUnavailableException handleDoctorUnavailableException(DoctorUnavailableException e) {
		return new DoctorUnavailableException(e.getMessage());
	}

	@ExceptionHandler(AppointmentAlreadyExistsException.class)
	public AppointmentAlreadyExistsException handleAppointmentAlreadyExistsException(
			AppointmentAlreadyExistsException e) {
		return new AppointmentAlreadyExistsException(e.getMessage());
	}

	@ExceptionHandler(DoctorStatusUpdateFailedException.class)
	public DoctorStatusUpdateFailedException handleDoctorStatusUpdateFailedException(
			DoctorStatusUpdateFailedException e) {
		return new DoctorStatusUpdateFailedException(e.getMessage());
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public InvalidCredentialsException handleInvalidCredentialsException(InvalidCredentialsException e) {
		return new InvalidCredentialsException(e.getMessage());
	}

	@ExceptionHandler(PasswordDoesNotExistsException.class)
	public PasswordDoesNotExistsException handlePasswordDoesNotExistsException(PasswordDoesNotExistsException e) {
		return new PasswordDoesNotExistsException(e.getMessage());
	}
}
