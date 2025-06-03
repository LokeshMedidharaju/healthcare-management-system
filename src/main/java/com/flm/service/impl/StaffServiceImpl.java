package com.flm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.flm.builder.StaffBuilder;
import com.flm.dao.StaffRepository;
import com.flm.dto.RegisterStaffDTO;
import com.flm.dto.StaffDetailsDTO;
import com.flm.exception.StaffNotFoundException;
import com.flm.exception.StaffServiceException;
import com.flm.model.Staff;
import com.flm.service.StaffService;
import com.flm.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

	private final StaffRepository staffRepository;
	
	public StaffServiceImpl(StaffRepository staffRepository) {
		this.staffRepository = staffRepository;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);

	@Override
	public StaffDetailsDTO saveStaff(RegisterStaffDTO registerStaffDTO) {	//	pk is automatically created and we need to only give details to save method
		try {
			Staff staff =  StaffBuilder.buildStaffFromDTO(registerStaffDTO);
			Staff savedStaff = staffRepository.save(staff);
			logger.info("Staff "+Constants.CREATED, savedStaff.getStaffId());
			return StaffBuilder.buildStaffDetailsDTOFromStaff(savedStaff);
		}catch (Exception e) {
			logger.error("{} saving staff : {}", Constants.ERROR, e.getMessage());
			throw new StaffServiceException(Constants.ERROR + " saving staff : "+e.getMessage());
		}
	}

	@Override
	public StaffDetailsDTO getStaffById(String staffId) {
	    try {
	        Staff staff = staffRepository.findById(staffId)
	            .orElseThrow(() -> new StaffNotFoundException("Staff " + Constants.NOT_FOUND_WITH_ID + staffId));

	        logger.info(Constants.RETRIEVED, "Staff", staffId);
	        return StaffBuilder.buildStaffDetailsDTOFromStaff(staff);

	    } catch (DataAccessException e) {
	        logger.error("{} fetching staff details with ID: {} - Exception: {}", Constants.ERROR, staffId, e.getMessage());
	        throw new StaffServiceException(Constants.ERROR + " fetching staff details with ID: " + staffId);
	    }
	}

	@Override
	public StaffDetailsDTO updateStaff(String staffId, RegisterStaffDTO registerStaffDTO) {	//	if send or give staffId, we can update details of given staffId.
	    try {
	    	Staff existingStaff = staffRepository.findById(staffId)
	    	        .orElseThrow(() -> new StaffNotFoundException(staffId));

	    	    logger.info(Constants.RETRIEVED, "staff", staffId);

	    	    Staff updatedStaff = StaffBuilder.buildStaffFromDTO(registerStaffDTO);

	    	    // Preserve essential details BEFORE saving
	    	    updatedStaff.getUser().setUserId(existingStaff.getUser().getUserId());
	    	    updatedStaff.getUser().setPassword(existingStaff.getUser().getPassword());
	    	    updatedStaff.setStaffId(existingStaff.getStaffId());
	    	    updatedStaff.getAddress().setAddressId(existingStaff.getAddress().getAddressId());

	    	    // Save after ensuring IDs are correctly set
	    	    staffRepository.save(updatedStaff);
	    	    logger.info(Constants.UPDATED, "staff", staffId);

	    	    return StaffBuilder.buildStaffDetailsDTOFromStaff(updatedStaff);
	    	    
	    } catch (DataAccessException e) {
	    	logger.error("{} updating staff details with staffId {} - Exception {}",Constants.ERROR, staffId, e.getMessage());
	    	throw new StaffServiceException(Constants.ERROR + " fetching staff details with staffId : " + staffId);
	    }
		
	}

	@Override
	public List<StaffDetailsDTO> getAllStaff() {
		
		try {
			List<Staff> staffs = staffRepository.findAll();
			
			if (staffs == null) {
				logger.error(Constants.ERROR + " fetching staff details.");
			}
			
			return staffs.stream()
			.map(StaffBuilder :: buildStaffDetailsDTOFromStaff)
			.collect(Collectors.toList());
			
		} catch (Exception e) {
			logger.error("{} fetching staff details. Exception - {}", Constants.ERROR, e.getMessage());
			throw new StaffServiceException(Constants.ERROR + " fetching staff details.");
		}
	}

	@Override
	public StaffDetailsDTO findByNameContainingIgnoringCase(String staffName) {
		try {
			Staff staff = staffRepository.findByFirstNameContainingIgnoringCase(staffName);
			
			if (staff == null) {
				logger.error(Constants.ERROR + " fetching staff details with given name {}." + staffName);
			}
			
			return StaffBuilder.buildStaffDetailsDTOFromStaff(staff);
		} catch (Exception e) {
			logger.error(Constants.NOT_FOUND_WITH_NAME + staffName );
			throw new StaffServiceException(Constants.NOT_FOUND_WITH_NAME + staffName );
		}
		
	}

}
