package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionRequestDTO {
	
	@NotBlank(message = "Please provide username of receiver.")
	private String receiver;
	
	@NotNull(message = "Please provide amount to be transferred.")
	private double amount;
}
