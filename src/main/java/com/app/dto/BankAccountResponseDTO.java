package com.app.dto;

import com.app.entities.AccountType;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankAccountResponseDTO {
	
	private String accountId;
	
	private AccountType accType;
	
	private double balance;
	
	private String username;

}
