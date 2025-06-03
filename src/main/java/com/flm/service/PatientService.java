package com.flm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flm.dto.PatientDetailsDTO;
import com.flm.dto.RegisterPatientDTO;

@Service
public interface PatientService {

	public PatientDetailsDTO registerPatient(RegisterPatientDTO registerPatientDTO);

	public PatientDetailsDTO updatePatient(String patientId, RegisterPatientDTO patientDto);

	public PatientDetailsDTO getPatientDetails(String patientId);

	public List<PatientDetailsDTO> getAllPatientDetails();

	public List<PatientDetailsDTO> getPatientsByName(String name);
	
	public List<PatientDetailsDTO> getPatientByPhoneNumber(String phoneNumber);
}
