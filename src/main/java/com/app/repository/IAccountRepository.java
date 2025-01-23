package com.app.repository;

import com.app.entities.Account;
import com.app.entities.AccountType;
import com.app.entities.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, String> {

	Optional<Account> findByUsername(Credential username);
	
	Optional<Account> findByAccountId(String accountId);
	
	List<Account> findByAccType(AccountType accountType);

}
