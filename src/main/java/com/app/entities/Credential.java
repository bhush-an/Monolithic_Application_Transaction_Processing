package com.app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "credential")
@Setter
@Getter
@NoArgsConstructor
@ToString(exclude = "userId")
public class Credential {
	
	@Id
	@Column(length = 30, unique = true)
	private String username;
	
	@Column(length = 150, nullable = false)
	private String password;
	
	@Transient
	@Column(length = 150, nullable = false)
	private String confirmPassword;
	
	@Column(length = 200, nullable = false)
	@Enumerated(EnumType.STRING)
	private SecurityQuestion question;
	
	@Column(length = 100, nullable = false)
	private String answer;
	
	@Column(length = 100)
	private String photo;
	
	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private RoleEnum role;
	
	@CreationTimestamp
	@Column(updatable = false, name = "created_at")
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userId;

}
