package com.flm.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flm.builder.AppointmentBuilder;
import com.flm.dao.AppointmentRepository;
import com.flm.dao.DoctorRepository;
import com.flm.dao.DoctorScheduleRepository;
import com.flm.dao.PatientRepository;
import com.flm.dto.AppointmentDTO;
import com.flm.dto.RegisterAppointmentDTO;
import com.flm.dto.RescheduleAppointmentDTO;
import com.flm.exception.AppointmentAlreadyExistsException;
import com.flm.exception.AppointmentNotFoundException;
import com.flm.exception.DoctorNotFoundException;
import com.flm.exception.DoctorUnavailableException;
import com.flm.exception.InvalidDateException;
import com.flm.exception.InvalidTimeException;
import com.flm.exception.PatientNotFoundException;
import com.flm.model.Appointment;
import com.flm.model.Doctor;
import com.flm.model.Patient;
import com.flm.service.AppointmentService;
import com.flm.util.Constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {

	private final DoctorRepository doctorRepository;
	private final DoctorScheduleRepository doctorScheduleRepository;
	private final AppointmentRepository appointmentRepository;
	private final PatientRepository patientRepository;
	private final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

	
	
	@Override
	public AppointmentDTO bookAppointmment(RegisterAppointmentDTO registerAppointmentDTO) {

		logger.info("Booking appointment: {}", registerAppointmentDTO);

		validateDate(registerAppointmentDTO.getAppointmentDate());
		validateTime(registerAppointmentDTO.getStartTime(), registerAppointmentDTO.getEndTime());

		Doctor doctor = doctorRepository.findById(registerAppointmentDTO.getDoctorId())
				.orElseThrow(() -> new DoctorNotFoundException(
						"Doctor " + Constants.NOT_FOUND_WITH_ID + registerAppointmentDTO.getDoctorId()));

		if (!doctor.isEmployeeActive()) {
			throw new DoctorUnavailableException("Doctor is no longer active and cannot take appointments.");
		}

		boolean isDateUnAvailable = doctorScheduleRepository
				.existsByDoctor_StaffIdAndUnavailableDate(doctor.getStaffId(), registerAppointmentDTO.getAppointmentDate());//if already unavailable dates and user appointment dates match(means exists) , true is returned

		if (isDateUnAvailable) {
			throw new DoctorUnavailableException("Doctor is not available on : " + registerAppointmentDTO.getAppointmentDate());
		}

		Patient patient = patientRepository.findById(registerAppointmentDTO.getPatientId())
				.orElseThrow(() -> new PatientNotFoundException(
						"Patient " + Constants.NOT_FOUND_WITH_ID + registerAppointmentDTO.getPatientId()));

		int count = appointmentRepository.countConflictingAppointments(doctor.getStaffId(), patient.getPatientId(),
				registerAppointmentDTO.getAppointmentDate(), registerAppointmentDTO.getStartTime(), registerAppointmentDTO.getEndTime());

		if (count > 0) {
			throw new AppointmentAlreadyExistsException(Constants.APPOINTMENT_EXISTS);
		}

		Appointment appointment = AppointmentBuilder.buildAppointmentFromDTO(registerAppointmentDTO, doctor, patient,
				"SCHEDULED");

		Appointment savedAppointemnt = appointmentRepository.save(appointment);

		AppointmentDTO dto = AppointmentBuilder.buildDTOFromAppointment(savedAppointemnt);

		logger.info("Successfully booked appointment for patient {} with doctor {} on {} from {} to {}",
				registerAppointmentDTO.getPatientId(), registerAppointmentDTO.getDoctorId(), registerAppointmentDTO.getAppointmentDate(),
				registerAppointmentDTO.getStartTime(), registerAppointmentDTO.getEndTime());
		return dto;
	}

	@Override
	public AppointmentDTO rescheduleAppointment(String appointmentId,
			RescheduleAppointmentDTO rescheduleAppointmentDTO) {
		logger.info("Rescheduling appointment ID: {} to {} from {} to {}", appointmentId,
				rescheduleAppointmentDTO.getNewDate(), rescheduleAppointmentDTO.getStartTime(),
				rescheduleAppointmentDTO.getEndTime());

		validateDate(rescheduleAppointmentDTO.getNewDate());

		validateTime(rescheduleAppointmentDTO.getStartTime(), rescheduleAppointmentDTO.getEndTime());

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

		Doctor doctor = appointment.getDoctor();
		Patient patient = appointment.getPatient();

		if (!doctor.isEmployeeActive()) {
			throw new DoctorUnavailableException("Doctor is no longer active and cannot take appointments.");
		}

		boolean isDateUnAvailable = doctorScheduleRepository
				.existsByDoctor_StaffIdAndUnavailableDate(doctor.getStaffId(), rescheduleAppointmentDTO.getNewDate());

		if (isDateUnAvailable) {
			throw new DoctorUnavailableException(
					"Doctor is not available on : " + rescheduleAppointmentDTO.getNewDate());
		}

		int count = appointmentRepository.countConflictingAppointments(doctor.getStaffId(), patient.getPatientId(),
				rescheduleAppointmentDTO.getNewDate(), rescheduleAppointmentDTO.getStartTime(),
				rescheduleAppointmentDTO.getEndTime());

		if (count > 0) {
			throw new AppointmentAlreadyExistsException(Constants.APPOINTMENT_EXISTS);
		}

		appointment.setAppointmentDate(rescheduleAppointmentDTO.getNewDate());
		appointment.setStartTime(rescheduleAppointmentDTO.getStartTime());
		appointment.setEndTime(rescheduleAppointmentDTO.getEndTime());
		appointment.setStatus("SCHEDULED");

		Appointment updatedAppointment = appointmentRepository.save(appointment);

		logger.info("Successfully rescheduled appointment ID: {} to {} from {} to {}", appointmentId,
				rescheduleAppointmentDTO.getNewDate(), rescheduleAppointmentDTO.getStartTime(),
				rescheduleAppointmentDTO.getEndTime());
		return AppointmentBuilder.buildDTOFromAppointment(updatedAppointment);
	}

	@Override
	public AppointmentDTO getAppointmentDetails(String appointmentId) {
		logger.info("Fetching details for appointment ID: {}", appointmentId);

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

		return AppointmentBuilder.buildDTOFromAppointment(appointment);
	}

	@Override
	public List<AppointmentDTO> getAllAppointments() {
		logger.info("Fetching all appointments");

		List<Appointment> appointments = appointmentRepository.findAll();

		return appointments.stream()
				.map(AppointmentBuilder::buildDTOFromAppointment)
				.collect(Collectors.toList());
	}

	private void validateDate(LocalDate appointmentDate) {
		LocalDate today = LocalDate.now();
		if (appointmentDate.isBefore(today)) {
			throw new InvalidDateException("Date must be today or later.");
		}

	}
	
	private void validateTime(LocalTime startTime, LocalTime endTime) {
		LocalTime startLimit = LocalTime.of(10, 0);
		LocalTime endLimit = LocalTime.of(17, 0);
		if (startTime.isAfter(endLimit)) {
			throw new InvalidTimeException("Appointment booking should be in between " + startLimit + "&" + endLimit);
		} else if (startTime.equals(endLimit)) {
			throw new InvalidTimeException("Appointment booking should be in between " + startLimit + "&" + endLimit);
		} else if (startTime.isBefore(startLimit) || startTime.isAfter(endLimit)) {
			throw new InvalidTimeException("Appointment booking should be in between " + startLimit + "&" + endLimit);
		}

	}

}
