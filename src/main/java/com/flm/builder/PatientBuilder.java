package com.flm.builder;

import com.flm.dto.AddressDTO;
import com.flm.dto.PatientDetailsDTO;
import com.flm.dto.RegisterPatientDTO;
import com.flm.model.Address;
import com.flm.model.Patient;

public class PatientBuilder {

	public static Patient buildPatientFromDTO (RegisterPatientDTO registerPatientDTO) {
		return Patient.builder()
				.firstName(registerPatientDTO.getFirstName())
				.lastName(registerPatientDTO.getLastName())
				.age(registerPatientDTO.getAge())
				.phoneNumber(registerPatientDTO.getPhoneNumber())
				.email(registerPatientDTO.getEmail())
				.gender(registerPatientDTO.getGender())
				.address(Address.builder()
						.street(registerPatientDTO.getAddressDTO().getStreet())
						.landmark(registerPatientDTO.getAddressDTO().getLandmark())
						.city(registerPatientDTO.getAddressDTO().getCity())
						.postalCode(registerPatientDTO.getAddressDTO().getPostalCode())
						.state(registerPatientDTO.getAddressDTO().getState())
						.country(registerPatientDTO.getAddressDTO().getCountry())
						.build())
				.build();
	}
	
	public static PatientDetailsDTO buildPatientDtoFromPatient (Patient patient) {
		return PatientDetailsDTO.builder()
				.patient_id(patient.getPatientId())
				.firstName(patient.getFirstName())
				.lastName(patient.getLastName())
				.age(patient.getAge())
				.phoneNumber(patient.getPhoneNumber())
				.email(patient.getEmail())
				.gender(patient.getGender())
				.addressDTO(AddressDTO.builder()
						.street(patient.getAddress().getStreet())
						.landmark(patient.getAddress().getLandmark())
						.city(patient.getAddress().getCity())
						.postalCode(patient.getAddress().getPostalCode())
						.state(patient.getAddress().getState())
						.country(patient.getAddress().getCountry())
						.build())
				.build();
	}
}
