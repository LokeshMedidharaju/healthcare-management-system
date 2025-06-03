package com.flm.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flm.model.DoctorSchedule;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    public boolean existsByDoctor_StaffIdAndUnavailableDate(String doctorId, LocalDate date);

    public void deleteByDoctor_StaffIdAndUnavailableDateIn(String doctorId, List<LocalDate> dates);

	public List<LocalDate> findUnavailableDatesByDoctor_StaffId(String doctorId);
   
}