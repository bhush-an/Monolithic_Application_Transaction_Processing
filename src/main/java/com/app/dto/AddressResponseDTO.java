package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
	
	private String building;
	
	private String pincode;
	
	private String city;
	
	private String state;
	
	private String country;

}
