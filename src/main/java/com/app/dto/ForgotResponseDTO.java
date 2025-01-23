package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class ForgotResponseDTO {

	private String message;
	
	private String emailId;
	
	private String username;
}
