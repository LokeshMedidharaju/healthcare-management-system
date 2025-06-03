package com.flm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flm.model.Staff;

public interface StaffRepository extends JpaRepository<Staff, String> {

	public Staff findByFirstNameContainingIgnoringCase(String staffName);

}
