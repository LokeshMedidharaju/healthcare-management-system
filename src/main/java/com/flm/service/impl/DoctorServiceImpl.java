package com.flm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flm.builder.DoctorBuilder;
import com.flm.dao.DoctorRepository;
import com.flm.dto.DoctorDetailsDTO;
import com.flm.dto.RegisterDoctorDTO;
import com.flm.exception.DoctorNotFoundException;
import com.flm.exception.DoctorServiceException;
import com.flm.model.Doctor;
import com.flm.service.DoctorService;
import com.flm.util.Constants;

@Service
public class DoctorServiceImpl implements DoctorService{

	DoctorRepository doctorRepository;
    
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
		this.doctorRepository = doctorRepository;
	}

	private final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Override
    public DoctorDetailsDTO saveDoctor(RegisterDoctorDTO doctorDto) {
        try {
            Doctor doctor = DoctorBuilder.buildDoctorFromDoctorDTO(doctorDto);
            Doctor savedDoctor = doctorRepository.save(doctor);
            logger.info("Doctor " + Constants.CREATED, savedDoctor.getStaffId());
            return DoctorBuilder.buildDoctorDetailsDTOFromDoctor(savedDoctor);
        } catch (Exception e) {
            logger.error(Constants.ERROR+ " saving doctor: {}", e.getMessage());
            throw new DoctorServiceException(Constants.ERROR+ " saving doctor" + e.getMessage());
        }
    }

    @Override
    public DoctorDetailsDTO updateDoctor(String doctorId, RegisterDoctorDTO doctorDto) {
        try {
            Doctor existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor " + Constants.NOT_FOUND_WITH_ID + doctorId));
            
            logger.info(Constants.RETRIEVED, "Doctor" ,  doctorId);

            Doctor updatedDoctor = DoctorBuilder.buildDoctorFromDoctorDTO(doctorDto);

            // Preserve essential details from the existing doctor
            updatedDoctor.getUser().setUserId(existingDoctor.getUser().getUserId());
            updatedDoctor.getUser().setPassword(existingDoctor.getUser().getPassword());
            updatedDoctor.setStaffId(existingDoctor.getStaffId());
            updatedDoctor.getAddress().setAddressId(existingDoctor.getAddress().getAddressId());

            doctorRepository.save(updatedDoctor);
            logger.info("Doctor " + Constants.UPDATED + "{}", doctorId);
 
            return DoctorBuilder.buildDoctorDetailsDTOFromDoctor(updatedDoctor);

        } catch (Exception e) { // Catching unexpected errors
            logger.error("{} updating doctor with ID: {} - Exception - {}", Constants.ERROR, doctorId, e);
            throw new DoctorServiceException(Constants.ERROR + " updating doctor with ID: " + doctorId);
        }
    }


    @Override
    public DoctorDetailsDTO getDoctorDetails(String doctorId) {
        try {
            Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor" + Constants.NOT_FOUND_WITH_ID+ doctorId));

            logger.info(Constants.RETRIEVED, "Doctor" ,  doctorId);
            return DoctorBuilder.buildDoctorDetailsDTOFromDoctor(doctor);
            
        } catch (Exception e) { // Catching unexpected exceptions
            logger.error("{} fetching Doctor details with id: {} - Exception - {}", Constants.ERROR, doctorId, e);
            throw new DoctorServiceException(Constants.ERROR + " Unexpected error while fetching Doctor details with id: " + doctorId);
        }
    }


    @Override
    public List<DoctorDetailsDTO> getAllDoctorsDeatils() {
        try {
            List<Doctor> doctors = doctorRepository.findAll();
            if (doctors.isEmpty()) {
                logger.info("No doctors found");
            }
            return doctors.stream()
                    .map(DoctorBuilder::buildDoctorDetailsDTOFromDoctor)
                    .collect(Collectors.toList());
        } catch (Exception e) {
        	logger.error(Constants.ERROR+ " Fetching All doctors deatails: {}", e.getMessage());
            throw new DoctorServiceException(Constants.ERROR+ " Fetching All doctors deatails: " + e.getMessage());
        }
    }
    
    @Override
    public List<DoctorDetailsDTO> getDoctorsByName(String name) {
        try {
            List<Doctor> doctors = doctorRepository.findByFirstNameContainingIgnoreCase(name);
            if (doctors.isEmpty()) {
                logger.warn("No doctors found with name containing: {}", name);
                throw new DoctorNotFoundException("No doctors found with name: " + name);
            }

            logger.info("Retrieved {} doctors with name containing: {}", doctors.size(), name);
            return doctors.stream()
                          .map(DoctorBuilder::buildDoctorDetailsDTOFromDoctor)
                          .collect(Collectors.toList());

        } catch (Exception e) {  // Handle other exceptions
            logger.error("{} retrieving doctors with name: {} - exception - {}", Constants.ERROR, name, e);
            throw new DoctorServiceException(Constants.ERROR + " retrieving doctors with name: " + name);
        }
    }
}
