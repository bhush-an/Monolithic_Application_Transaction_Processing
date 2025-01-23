package com.app.entities;

import com.app.utils.GenerateUserId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "user")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class User {
	
	@Id
	@GenerateUserId
	@Column(length = 10)
	private String userId;
	
	@Column(length = 30, nullable = false)
	private String firstName;
	
	@Column(length = 30, nullable = false)
	private String lastName;
	
	@Column(length = 30, nullable = false)
	private String emailId;
	
	@Column(length = 10, nullable = false)
	private String contactNumber;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private Date dob;
	
	@Column(length = 10, nullable = false)
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;

}
