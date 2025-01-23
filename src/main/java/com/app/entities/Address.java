package com.app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "address")
@Setter
@Getter
@ToString
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addrId;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(length = 100, nullable = false)
	private String building;
	
	@Column(length = 6, nullable = false)
	private String pincode;
	
	@Column(length = 30, nullable = false)
	private String city;
	
	@Column(length = 30, nullable = false)
	private String state;
	
	@Column(length = 30, nullable = false)
	private String country;

}
