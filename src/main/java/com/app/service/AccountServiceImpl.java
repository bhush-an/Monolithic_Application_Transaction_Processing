package com.app.service;

import com.app.dto.BankAccountResponseDTO;
import com.app.entities.Account;
import com.app.entities.AccountType;
import com.app.entities.Credential;
import com.app.repository.IAccountRepository;
import com.app.repository.ICredentialRepository;
import com.app.utils.JWTUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements IAccountService {
	
	@Autowired
	private IAccountRepository accRepo;
	
	@Autowired
	private ICredentialRepository credRepo;
	
	@Autowired 
	private JWTUtils utils;

	@Autowired
	private ModelMapper mapper;
	
	@Override
	public BankAccountResponseDTO getBankAccountDetails(String token) {
		String usernameFromToken = utils.getUserNameFromJwtToken(token);
		Credential cred = credRepo.findByUsername(usernameFromToken).orElseThrow();
		Account account = accRepo.findByUsername(cred).orElseThrow();
		return mapper.map(account, BankAccountResponseDTO.class);
	}

	@Override
	public List<BankAccountResponseDTO> findAccountByParameter(String account, String username, String accType) {
		List<Account> accountList = new ArrayList<Account>();
		if (!(account == null)) {
			Account acc = accRepo.findByAccountId(account).orElseThrow(() -> new UsernameNotFoundException("Invalid Account ID!"));
			accountList.add(acc);
		} else if (!(username == null)) {
			Credential cred = credRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid Username!"));
			Account acc = accRepo.findByUsername(cred).orElseThrow();
			accountList.add(acc);
		} else if (!(accType == null)) {
			try {
				accountList = accRepo.findByAccType(AccountType.valueOf(accType.toUpperCase()));				
			} catch (RuntimeException e) {
				throw new RuntimeException("Account Type should be either SAVINGS / CURRENT");
			}
		} else {
			accountList = accRepo.findAll();
		}
		List<BankAccountResponseDTO> list = new ArrayList<BankAccountResponseDTO>();
		for(int i=0; i<accountList.size(); i++) {
			BankAccountResponseDTO dto = new BankAccountResponseDTO();
			mapper.map(accountList.get(i), dto);
			list.add(dto);
		}
		return list;
	}



}
