package com.app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTP {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long otpId;
	
	@Column(length = 30)
	private String emailId;
	
	@Column(length = 30)
	private String username;
	
	@Column(length = 6)
	private String otp;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	private LocalDateTime expiresAt;
}
