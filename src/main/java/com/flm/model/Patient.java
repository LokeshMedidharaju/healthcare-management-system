package com.flm.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patients")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

	@Id
	@Column(name = "patient_id")
	private String patientId;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "gender", nullable = false)
	private String gender;

	@Column(name = "age", nullable = false)
	private String age;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "email", unique = true)
	private String email;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL) // One Patient, Many Appointments
	private List<Appointment> appointment;

	public Patient(String firstName, String lastName, String gender, String age, String phoneNumber, String email,
			Address address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
	}

	@PrePersist
	private void generatePatientId() {
		if (this.patientId == null) {
			String yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

			String randomDigits = String.format("%04d", new Random().nextInt(1_000_000));

			this.patientId = yearMonth + randomDigits;
		}
	}

}
