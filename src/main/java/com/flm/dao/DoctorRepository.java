package com.flm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.flm.model.Doctor;

@Component
public interface DoctorRepository extends JpaRepository<Doctor, String>{

	List<Doctor> findByFirstNameContainingIgnoreCase(String name);

}
