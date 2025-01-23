package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@ToString
public class AddressRequestDTO {
	
	private Long addrId;
	
	@NotBlank(message = "Please provide building address.")
	private String building;
	
	@NotNull(message = "Please provide Pincode.")
	@Length(min = 6, max = 6, message = "Please provide valid pincode of length 6.")
	private String pincode;

}
