package com.flm.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Long addressId;
	
	@OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
	private Staff staff;

	@Column(name = "street", nullable = false)
	private String street;
	
	@Column(name = "landmark")
	private String landmark;

	@Column(name = "city", nullable = false)
	private String city;

	@Column(name = "postal_code", nullable = false)
	private String postalCode;

	@Column(name = "state", nullable = false)
	private String state;

	@Column(name = "country", nullable = false)
	private String country;

	public Address(String street, String landmark, String city, String postalCode, String state, String country) {
		super();
		this.street = street;
		this.landmark = landmark;
		this.city = city;
		this.postalCode = postalCode;
		this.state = state;
		this.country = country;
	}
}
