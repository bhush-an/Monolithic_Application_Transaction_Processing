package com.app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "transaction_record")
@Setter
@Getter
@ToString
public class TransactionRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recordId;
	
	private double credit;
	
	private double debit;
	
	private double balance;
	
	@ManyToOne
	@JoinColumn(name = "utr", nullable = false)
	private Transaction utr;

}
