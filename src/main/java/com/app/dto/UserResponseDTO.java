package com.app.dto;

import com.app.entities.GenderEnum;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDTO {
	
	private String firstName;
	
	private String lastName;
	
	private String emailId;
	
	private String contactNumber;
	
	private Date dob;
	
	private GenderEnum gender;

}
