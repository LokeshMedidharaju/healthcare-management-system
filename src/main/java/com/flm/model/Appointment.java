package com.flm.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
@Builder
public class Appointment {

	@Id
	@Column(name = "appointment_id")
	private String appointmentId;
	
	@ManyToOne
	@JoinColumn(name = "doctor_id", nullable = false)
	private Doctor doctor;
	
	@ManyToOne //Many Appointments, one Patient
	@JoinColumn(name = "patient_id", nullable = false)
	private Patient patient;
	
	@Column(name = "appointment_date", nullable = false)
	private LocalDate appointmentDate;
	
	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;
	
	@Column(name = "end_date", nullable = false)
	private LocalTime endTime;
	
	@Column(name = "status", nullable = false)
	private String status;	//	scheduled or cancelled or completed
	
	@Column(name = "notes", nullable = false)
	private String notes;
	
	@PrePersist
    private void generateAppointmentId() {
        if (this.appointmentId == null && this.doctor != null) {
        	
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHMMSS"));

            String doctorSuffix = doctor.getStaffId().length() > 4
                    ? doctor.getStaffId().substring(doctor.getStaffId().length() - 4)
                    : doctor.getStaffId();

            this.appointmentId = timestamp + doctorSuffix;
        }
    }

}
