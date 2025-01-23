package com.app.dto;

import com.app.entities.AccountType;
import com.app.entities.RoleEnum;
import com.app.entities.Status;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountResponseDTO {
	
	private String username;
	
	private AccountType accType;
	
	private double balance;
	
	private Status status;
	
	private RoleEnum role;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
