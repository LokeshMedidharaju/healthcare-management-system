package com.flm.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RegisterStaffDTO {
	
	private String firstName;

	private String lastName;

	private String phoneNumber;

	private AddressDTO addressDto;

	private LocalDate dateOfJoining;

	private double experienceInYears;

	private boolean canLogin;

	private String role;
	
	private String email;
}
