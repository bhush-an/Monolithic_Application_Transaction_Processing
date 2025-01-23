package com.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ForgotRequestDTO {
	
	@NotBlank(message = "Please provide unique username.")
	private String username;
	
	@NotBlank(message = "Please provide Email Address.")
	@Email(message = "Please provide Email in correct format.")
	private String emailId;

}
