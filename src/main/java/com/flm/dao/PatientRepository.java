package com.flm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flm.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, String> {

	public List<Patient> findByFirstNameContainingIgnoreCase(String name);

	public List<Patient> findByPhoneNumber(String phoneNumber);

}
