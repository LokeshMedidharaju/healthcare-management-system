package com.flm.dao;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flm.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, String>{

	@Query("SELECT COUNT(a) FROM Appointment a " +
		       "WHERE a.appointmentDate = :date " +
		       "AND NOT (a.endTime <= :startTime OR a.startTime >= :endTime) " +//NOT means there should be no overlap.if there is overlap, count is increased
		       "AND (a.doctor.id = :doctorId OR a.patient.id = :patientId) " +//doctor and patient should have conflicting appointments
		       "AND a.status <> 'CANCELLED'")
	public int countConflictingAppointments(
		    @Param("doctorId") String doctorId, 
		    @Param("patientId") String patientId,
		    @Param("date") LocalDate date, 
		    @Param("startTime") LocalTime startTime, 
		    @Param("endTime") LocalTime endTime
		);
}
