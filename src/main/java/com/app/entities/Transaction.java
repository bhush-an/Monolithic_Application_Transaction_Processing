package com.app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Setter
@Getter
@ToString
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(length = 40)
	private String utr;
	
	@Column(length = 30, nullable = false)
	private String sender;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver", nullable = false)
	private Credential receiver;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime transactionDateTime;

}
