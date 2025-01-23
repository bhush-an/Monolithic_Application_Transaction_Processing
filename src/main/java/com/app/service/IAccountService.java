package com.app.service;

import com.app.dto.BankAccountResponseDTO;

import java.util.List;

public interface IAccountService {

	BankAccountResponseDTO getBankAccountDetails(String token);

	List<BankAccountResponseDTO> findAccountByParameter(String account, String username, String accType);

}
