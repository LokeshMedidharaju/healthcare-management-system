package com.flm.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flm.dto.PatientDetailsDTO;
import com.flm.dto.RegisterPatientDTO;
import com.flm.service.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientController {

	private final PatientService patientService;

	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
	public ResponseEntity<PatientDetailsDTO> registerPatient(@RequestBody RegisterPatientDTO registerPatientDTO) {
		PatientDetailsDTO patientDetailsDTO = patientService.registerPatient(registerPatientDTO);
		return new ResponseEntity<PatientDetailsDTO>(patientDetailsDTO, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
	public ResponseEntity<PatientDetailsDTO> updatePatient(@PathVariable String id,
			@RequestBody RegisterPatientDTO registerPatientDTO) {
		PatientDetailsDTO updatedPatient = patientService.updatePatient(id, registerPatientDTO);
		return new ResponseEntity<PatientDetailsDTO>(updatedPatient, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
	public ResponseEntity<PatientDetailsDTO> getPatientById(@PathVariable String id) {
		PatientDetailsDTO patientDetails = patientService.getPatientDetails(id);
		return new ResponseEntity<PatientDetailsDTO>(patientDetails, HttpStatus.OK);
	}

	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
	public ResponseEntity<List<PatientDetailsDTO>> getAllPatients() {
		List<PatientDetailsDTO> patients = patientService.getAllPatientDetails();
		return new ResponseEntity<List<PatientDetailsDTO>>(patients, HttpStatus.OK);
	}

	@GetMapping("/searchByName")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
	public ResponseEntity<List<PatientDetailsDTO>> searchPatientsByName(@RequestParam String name) {
		List<PatientDetailsDTO> patients = patientService.getPatientsByName(name);
		return new ResponseEntity<List<PatientDetailsDTO>>(patients, HttpStatus.OK);
	}

	@GetMapping("/searchByPhone")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
	public ResponseEntity<List<PatientDetailsDTO>> searchPatientsByPhone(@RequestParam String phoneNumber) {
		List<PatientDetailsDTO> patients = patientService.getPatientByPhoneNumber(phoneNumber);
		return new ResponseEntity<List<PatientDetailsDTO>>(patients, HttpStatus.OK);
	}

}
