package com.flm.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flm.dto.RegisterStaffDTO;
import com.flm.dto.StaffDetailsDTO;
import com.flm.service.StaffService;

@RestController
@RequestMapping("/staff")
public class StaffController {

	private final StaffService staffService;

	public StaffController(StaffService staffService) {
		this.staffService = staffService;
	}

	@PostMapping()
	public ResponseEntity<StaffDetailsDTO> registerStaff (@RequestBody RegisterStaffDTO registerStaffDto) {
		StaffDetailsDTO staffDetailsDTO = staffService.saveStaff(registerStaffDto);
		return new ResponseEntity<StaffDetailsDTO>(staffDetailsDTO, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<StaffDetailsDTO> getStaffById(@PathVariable String id) {
		StaffDetailsDTO staffDetailsDTO = staffService.getStaffById(id);
		return new ResponseEntity<StaffDetailsDTO>(staffDetailsDTO, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<StaffDetailsDTO> updateStaff (@PathVariable String id, @RequestBody RegisterStaffDTO registerStaffDTO) {
		StaffDetailsDTO staffDetailsDTO = staffService.updateStaff(id, registerStaffDTO);
		return new ResponseEntity<StaffDetailsDTO> (staffDetailsDTO, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity <List<StaffDetailsDTO>> getAllStaff () {
		List<StaffDetailsDTO> staffDetailsDTOs = staffService.getAllStaff();
		return new ResponseEntity <List<StaffDetailsDTO>> (staffDetailsDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/byName")
	public ResponseEntity<StaffDetailsDTO> getStaffByName (String staffName) {
		StaffDetailsDTO staffDetailsDTO = staffService.findByNameContainingIgnoringCase(staffName);
		return new ResponseEntity<StaffDetailsDTO> (staffDetailsDTO, HttpStatus.OK);
	}

}
