package com.flm.builder;

import com.flm.dto.AppointmentDTO;
import com.flm.dto.RegisterAppointmentDTO;
import com.flm.model.Appointment;
import com.flm.model.Doctor;
import com.flm.model.Patient;

public class AppointmentBuilder {

    public static Appointment buildAppointmentFromDTO(RegisterAppointmentDTO dto, Doctor doctor,Patient patient, String status) {
        if (dto == null || doctor == null) {
            return null;
        }

       return Appointment.builder()
        	.doctor(doctor)
        	.patient(patient)
        	.appointmentDate(dto.getAppointmentDate())
        	.startTime(dto.getStartTime())
        	.endTime(dto.getEndTime())
        	.status(status)
        	.notes(dto.getNotes())
        	.build();
       
    }

    public static AppointmentDTO buildDTOFromAppointment(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        return AppointmentDTO.builder()
        		.appointmentId(appointment.getAppointmentId())
        		.doctorId(appointment.getDoctor().getStaffId())
        		.patientId(appointment.getPatient().getPatientId())
        		.appointmentDate(appointment.getAppointmentDate())
        		.startTime(appointment.getStartTime())
        		.endTime(appointment.getEndTime())
        		.notes(appointment.getNotes())
        		.build();
    }
}