package com.app.dto;

import com.app.entities.GenderEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Setter
@Getter
@ToString
public class UserRequestDTO {
	
	private String userId;
	
	@NotBlank(message = "Please provide First Name.")
	private String firstName;
	
	@NotBlank(message = "Please provide Last Name.")
	private String lastName;
	
	@NotBlank(message = "Please provide Email ID.")
	@Email(message = "Please provide Email ID in correct format.")
	private String emailId;
	
	@NotBlank(message = "Please provide Contact Number.")
	@Length(min = 10, max = 10, message = "Length of Contact Number is not 10.")
	private String contactNumber;
	
	@NotNull(message = "Please provide Date of Birth in YYYY-MM-DD.")
	private Date dob;
	
	@NotNull(message = "Please provide Gender.")
	private GenderEnum gender;

}
