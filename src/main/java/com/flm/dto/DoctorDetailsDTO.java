package com.flm.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DoctorDetailsDTO {

	private String doctorId;
	
	private String firstName;

	private String lastName;

	private String phoneNumber;

	private String email;
	
	private AddressDTO addressDto;

	private LocalDate dateOfJoining;

	private double experienceInYears;

	private boolean isActive;

	private String specialization;
	
	private boolean canLogin;
}
