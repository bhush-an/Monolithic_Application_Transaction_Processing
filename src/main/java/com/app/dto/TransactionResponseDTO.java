package com.app.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionResponseDTO {
	
	private String utr;
	
    private String sender;
    
    private String receiver;
    
    private LocalDateTime transactionDateTime;
    
    private double credit;
    
    private double debit;
    
    private double balance;
}
