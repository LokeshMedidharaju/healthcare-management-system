package com.flm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flm.dto.RegisterStaffDTO;
import com.flm.dto.StaffDetailsDTO;

@Service
public interface StaffService {

	public StaffDetailsDTO saveStaff(RegisterStaffDTO registerStaffDTO);

	public StaffDetailsDTO getStaffById(String staffId);

	public StaffDetailsDTO updateStaff(String staffId, RegisterStaffDTO registerStaffDTO);

	public List<StaffDetailsDTO> getAllStaff();
	
	public StaffDetailsDTO findByNameContainingIgnoringCase(String staffName);
}
