package com.flm.service;

import java.util.List;

import com.flm.dto.AppointmentDTO;
import com.flm.dto.RegisterAppointmentDTO;
import com.flm.dto.RescheduleAppointmentDTO;

public interface AppointmentService {

	public AppointmentDTO bookAppointmment(RegisterAppointmentDTO registerAppointmentDTO);

	public AppointmentDTO rescheduleAppointment(String appointmentId,
			RescheduleAppointmentDTO rescheduleAppointmentDTO);

	public AppointmentDTO getAppointmentDetails(String appointmentId);

	public List<AppointmentDTO> getAllAppointments();
}
