package com.app.entities;

import com.app.utils.GenerateAccountId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "account")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Account {
	
	@Id
	@Column(length = 10)
	private String accountId;
	
	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountType accType;
	
	@Column(nullable = false)
	private double balance;
	
	@OneToOne
	@JoinColumn(name = "username")
	private Credential username;
	
	@PrePersist
	public void setID() {
		if (this.accountId == null) {
			this.accountId = GenerateAccountId.generateID();
		}
	}
	

}
