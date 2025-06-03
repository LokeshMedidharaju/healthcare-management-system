package com.flm.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flm.dto.AppointmentDTO;
import com.flm.dto.RegisterAppointmentDTO;
import com.flm.dto.RescheduleAppointmentDTO;
import com.flm.service.AppointmentService;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

	private final AppointmentService appointmentService;

	public AppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@PostMapping("/booking")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
	public ResponseEntity<AppointmentDTO> bookAppointment(@RequestBody RegisterAppointmentDTO registerAppointmentDTO) {
		AppointmentDTO appDto = appointmentService.bookAppointmment(registerAppointmentDTO);
		return new ResponseEntity<AppointmentDTO>(appDto, HttpStatus.CREATED);
	}

	@PutMapping("/reschedule/{appointmentId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
	public ResponseEntity<AppointmentDTO> rescheduleAppointment(@PathVariable String appointmentId,
			@RequestBody RescheduleAppointmentDTO rescheduleAppointmentDTO) {

		AppointmentDTO updatedAppointment = appointmentService.rescheduleAppointment(appointmentId,
				rescheduleAppointmentDTO);
		return ResponseEntity.ok(updatedAppointment);
	}

	@GetMapping("/{appointmentId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
	public ResponseEntity<AppointmentDTO> getAppointmentDetails(@PathVariable String appointmentId) {
		AppointmentDTO appointment = appointmentService.getAppointmentDetails(appointmentId);
		return ResponseEntity.ok(appointment);
	}

	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
	public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
		List<AppointmentDTO> appointments = appointmentService.getAllAppointments();
		return ResponseEntity.ok(appointments);
	}

}
