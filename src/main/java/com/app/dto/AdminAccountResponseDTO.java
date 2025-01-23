package com.app.dto;

import com.app.entities.RoleEnum;
import com.app.entities.Status;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountResponseDTO {
	
	private String username;
	
	private Status status;
	
	private RoleEnum role;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
