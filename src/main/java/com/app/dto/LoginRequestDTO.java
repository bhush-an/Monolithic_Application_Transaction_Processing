package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginRequestDTO {

	@NotBlank(message = "Please enter username.")
	private String username;
	
	@NotBlank(message = "Please enter password.")
	private String password;
}
