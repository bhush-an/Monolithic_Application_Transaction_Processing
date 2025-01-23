package com.app.service;

import com.app.dto.ResponseDTO;
import com.app.dto.TransactionRequestDTO;
import com.app.dto.TransactionResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface ITransactionService {

	ResponseDTO transferAmount(String token, @Valid TransactionRequestDTO transactionDTO);

	List<TransactionResponseDTO> getTransactionDetailsByUsername(String token);

	List<TransactionResponseDTO> getCustomTransactionDetails(String token, String startDate, String endDate);

	List<TransactionResponseDTO> getAllTransactions(String token);

	ResponseDTO updateBalance(String token, @Valid TransactionRequestDTO transactionDTO);

}
