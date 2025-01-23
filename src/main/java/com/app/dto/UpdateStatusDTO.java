package com.app.dto;

import com.app.entities.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateStatusDTO {

	@NotBlank(message = "Please mention username.")
	private String username;
	
	@NotNull(message = "Please mention status : ACTIVE / INACTIVE / BLOCKED")
	private Status status;
}
