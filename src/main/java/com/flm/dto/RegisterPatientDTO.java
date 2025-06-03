package com.flm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterPatientDTO {

	
	private String firstName;
	
	private String lastName;
	
	private String gender;
	
	private String age;
	
	private String phoneNumber;
	
	private String email;
	
	private AddressDTO addressDTO;
}
