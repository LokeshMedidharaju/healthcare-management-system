package com.flm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flm.builder.PatientBuilder;
import com.flm.dao.PatientRepository;
import com.flm.dto.PatientDetailsDTO;
import com.flm.dto.RegisterPatientDTO;
import com.flm.exception.PatientNotFoundException;
import com.flm.exception.PatientServiceException;
import com.flm.model.Patient;
import com.flm.service.PatientService;
import com.flm.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService{

	private final PatientRepository patientRepository;
	
	public PatientServiceImpl (PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}
	
	private static final Logger logger  = LoggerFactory.getLogger(PatientServiceImpl.class);
	
	@Override
	public PatientDetailsDTO registerPatient(RegisterPatientDTO registerPatientDTO) {
		try {
			
			Patient patient = PatientBuilder.buildPatientFromDTO(registerPatientDTO);
			
			Patient savedPatient = patientRepository.save(patient);
			logger.info(Constants.CREATED, patient.getPatientId());
			
			return PatientBuilder.buildPatientDtoFromPatient(savedPatient);
		} catch (Exception e) {
			logger.error("{} registering patient details. Exception - {}", Constants.ERROR, e.getMessage());
			throw new PatientServiceException(Constants.ERROR + "registering patient details.");
		}
		
	}
	
	 @Override
	    public PatientDetailsDTO getPatientDetails(String patientId) {
	        try {
	            Patient patient = patientRepository.findById(patientId)
	                .orElseThrow(() -> new PatientNotFoundException("Patient " + Constants.NOT_FOUND_WITH_ID + patientId));

	            logger.info("Patient {} retrieved successfully", patientId);
	            return PatientBuilder.buildPatientDtoFromPatient(patient);
	            
	        } catch (Exception e) {
	            logger.error("{} fetching patient details with ID: {} - Exception - {}", Constants.ERROR, patientId, e);
	            throw new PatientServiceException(Constants.ERROR + " fetching patient details with ID: " + patientId);
	        }
	    }

	@Override
    public PatientDetailsDTO updatePatient(String patientId, RegisterPatientDTO patientDto) {
        try {
            Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient " + Constants.NOT_FOUND_WITH_ID + patientId));

            logger.info(Constants.RETRIEVED, "Patient", patientId);

            Patient updatedPatient = PatientBuilder.buildPatientFromDTO(patientDto);
            
            updatedPatient.setPatientId(existingPatient.getPatientId());
            updatedPatient.getAddress().setAddressId(existingPatient.getAddress().getAddressId());

            patientRepository.save(updatedPatient);
            logger.info(Constants.UPDATED, "patient", patientId);

            return PatientBuilder.buildPatientDtoFromPatient(updatedPatient);

        } catch (Exception e) {
            logger.error("{} updating patient with ID: {} - Exception - {}", Constants.ERROR, patientId, e.getMessage());
            throw new PatientServiceException(Constants.ERROR + " updating patient with ID: " + patientId);
        }
    }

    @Override
    public List<PatientDetailsDTO> getAllPatientDetails() {
        try {
            List<Patient> patientList = patientRepository.findAll();
            if (patientList.isEmpty()) {
                logger.info("No patients found");
            }
            return patientList.stream()
                    .map(PatientBuilder::buildPatientDtoFromPatient)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("{} Fetching all patient details: {}", Constants.ERROR, e.getMessage());
            throw new PatientServiceException(Constants.ERROR + " Fetching all patient details: " + e.getMessage());
        }
    }
    
    @Override
    public List<PatientDetailsDTO> getPatientsByName(String name) {
        try {
            List<Patient> patientList = patientRepository.findByFirstNameContainingIgnoreCase(name);
            if (patientList == null) {
                logger.warn("No patients found with name containing: {}", name);
                throw new PatientNotFoundException("No patients found with name: " + name);
            }

            logger.info("Retrieved {} patients with name containing: {}", patientList.size(), name);
            return patientList.stream()
                          .map(PatientBuilder::buildPatientDtoFromPatient)
                          .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("{} retrieving patients with name: {} - exception - {}", Constants.ERROR, name, e);
            throw new PatientServiceException(Constants.ERROR + " retrieving patients with name: " + name);
        }
    }

	@Override
	public List<PatientDetailsDTO> getPatientByPhoneNumber(String phoneNumber) {
		
		try {
			List<Patient> patient = patientRepository.findByPhoneNumber(phoneNumber);
			if(patient == null) {
			
			logger.error("Patient details not found with phone number : " + phoneNumber);
			throw new PatientNotFoundException("No patient details found with phone number : " + phoneNumber);
			
			}
	        return patient.stream()
	        		.map(PatientBuilder::buildPatientDtoFromPatient)
	        		.collect(Collectors.toList());

        } catch (Exception e) {
        	logger.error("{} fetching patient details with Phone Number: {} - Exception - {}", Constants.ERROR, phoneNumber, e);
            throw new PatientServiceException(Constants.ERROR + " fetching patient details with Phone number: " + phoneNumber);
        }
    } 
	
}

