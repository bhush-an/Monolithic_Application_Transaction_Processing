package com.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
	
	private String message;
	
	private LocalDateTime timeStamp;
	
	private UserResponseDTO userDetails;
	
	private AddressResponseDTO addressDetails;
	
	private CustomerAccountResponseDTO customerDetails;

	private AdminAccountResponseDTO adminDetails;
	
	private BankAccountResponseDTO bankAccountDetails;
	
	private TransactionResponseDTO transactionDetails;
	
	private LoginResponseDTO loginDetails;
	
	private ImageResDTO imageDetails;
	
	private ForgotResponseDTO userForgotDetails;
	

}
