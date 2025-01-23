package com.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@AllArgsConstructor
@ToString
public class ResetPasswordRequestDTO {
	
	@NotBlank(message = "Please enter valid OTP.")
	private String otp;
	
	@NotBlank(message = "Please provide correlated Email ID.")
	private String emailId;
	
	@NotBlank(message = "Please provide new password.")
	@Pattern(regexp="((?=.*\\d)(?=.*[a-z])(?=.*[#@$*]).{5,20})", 
	message = "Password does not match with the pattern.")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@NotBlank(message = "Please confirm new password.")
	private String confirmPassword;

}
