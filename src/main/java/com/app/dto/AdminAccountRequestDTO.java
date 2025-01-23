package com.app.dto;

import com.app.entities.SecurityQuestion;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AdminAccountRequestDTO {
	
	@NotBlank(message = "Please provide unique username.")
	private String username;
	
	@NotBlank(message = "Please provide password.")
	@Pattern(regexp="((?=.*\\d)(?=.*[a-z])(?=.*[#@$*]).{5,20})", 
	message = "Password does not match with the pattern.")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@NotBlank(message = "Please confirm password.")
	private String confirmPassword;
	
	@NotNull(message = "Please select one security question.")
	private SecurityQuestion question;
	
	@NotBlank(message = "Please provide relevant answer for security question.")
	private String answer;
	
	@AssertTrue(message = "The password fields must match!")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
