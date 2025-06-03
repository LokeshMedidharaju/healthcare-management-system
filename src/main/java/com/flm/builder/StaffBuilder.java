package com.flm.builder;

import com.flm.dto.AddressDTO;
import com.flm.dto.RegisterStaffDTO;
import com.flm.dto.StaffDetailsDTO;
import com.flm.model.Address;
import com.flm.model.Staff;
import com.flm.model.User;

public class StaffBuilder {

	public static Staff buildStaffFromDTO(RegisterStaffDTO registerStaffDTO) {
		return Staff.builder()
				.firstName(registerStaffDTO.getFirstName())
				.lastName(registerStaffDTO.getLastName())
				.phoneNumber(registerStaffDTO.getPhoneNumber())
				.dateOfJoining(registerStaffDTO.getDateOfJoining())
				.experienceInYears(registerStaffDTO.getExperienceInYears())
				.isEmployeeActive(true)
				.canLogin(registerStaffDTO.isCanLogin())
				.address(Address.builder()
						.street(registerStaffDTO.getAddressDto().getStreet())
						.landmark(registerStaffDTO.getAddressDto().getLandmark())
						.city(registerStaffDTO.getAddressDto().getCity())
						.postalCode(registerStaffDTO.getAddressDto().getPostalCode())
						.state(registerStaffDTO.getAddressDto().getState())
						.country(registerStaffDTO.getAddressDto().getCountry())
						.build())
				.user(User.builder()
						.role(registerStaffDTO.getRole().toUpperCase())
						.email(registerStaffDTO.getEmail())
						.build())
				.build();				
				
	}
	
	public static StaffDetailsDTO buildStaffDetailsDTOFromStaff(Staff staff) {
		return StaffDetailsDTO.builder()
				.staffId(staff.getStaffId())
				.firstName(staff.getFirstName())
				.lastName(staff.getLastName())
				.phoneNumber(staff.getPhoneNumber())
				.experienceInYears(staff.getExperienceInYears())
				.isActive(staff.isEmployeeActive())
				.canLogin(staff.isCanLogin())
				.dateOfJoining(staff.getDateOfJoining())
				.addressDto(AddressDTO.builder()
						.street(staff.getAddress().getStreet())
						.landmark(staff.getAddress().getLandmark())
						.city(staff.getAddress().getCity())
						.postalCode(staff.getAddress().getPostalCode())
						.state(staff.getAddress().getState())
						.country(staff.getAddress().getCountry())
						.build())
				.role(staff.getUser().getRole())
				.email(staff.getUser().getEmail())
				.build();
	}
}
