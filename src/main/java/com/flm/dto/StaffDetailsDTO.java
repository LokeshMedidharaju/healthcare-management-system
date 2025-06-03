package com.flm.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class StaffDetailsDTO {

	private String staffId;
	
	private String firstName;

	private String lastName;

	private String phoneNumber;

	private AddressDTO addressDto;

	private LocalDate dateOfJoining;

	private double experienceInYears;

	private boolean canLogin;

	private String role;
	
	private String email;
	
	private boolean isActive;
}
