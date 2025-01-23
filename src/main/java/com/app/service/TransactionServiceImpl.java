package com.app.service;

import com.app.dto.ResponseDTO;
import com.app.dto.TransactionRequestDTO;
import com.app.dto.TransactionResponseDTO;
import com.app.entities.*;
import com.app.repository.IAccountRepository;
import com.app.repository.ICredentialRepository;
import com.app.repository.ITransactionRecordRepository;
import com.app.repository.ITransactionRepository;
import com.app.utils.JWTUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements ITransactionService {

	@Autowired
	private ITransactionRepository transactionRepo;

	@Autowired
	private ITransactionRecordRepository transactionRecordRepo;

	@Autowired
	private ICredentialRepository credRepo;

	@Autowired
	private IAccountRepository accRepo;

	@Autowired
	private JWTUtils utils;

	@Override
	public ResponseDTO transferAmount(String token, @Valid TransactionRequestDTO transactionDTO) {
		String sender = utils.getUserNameFromJwtToken(token);
		Credential senderCred = credRepo.findByUsername(sender).orElseThrow();
		Account senderAccount = accRepo.findByUsername(senderCred).orElseThrow();

		Credential receiverCred = credRepo.findByUsername(transactionDTO.getReceiver()).orElseThrow(
				() -> new UsernameNotFoundException("Invalid Username! Please provide existing username."));
		Account receiverAccount = accRepo.findByUsername(receiverCred)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid Account ID!"));

		if (sender.equals(receiverCred.getUsername())) {
			throw new RuntimeException("Amount cannot be transferred to self account!");
		}
		if (senderCred.getStatus().equals(Status.ACTIVE)) {
			if (receiverCred.getStatus().equals(Status.ACTIVE)) {
				if (senderAccount.getBalance() > transactionDTO.getAmount()) {
					try {
						// update balance in account table
						senderAccount.setBalance(senderAccount.getBalance() - transactionDTO.getAmount());
						receiverAccount.setBalance(receiverAccount.getBalance() + transactionDTO.getAmount());
						accRepo.save(senderAccount);
						accRepo.save(receiverAccount);
						// update transaction table
						Transaction transaction = new Transaction();
						transaction.setSender(sender);
						transaction.setReceiver(receiverCred);
						Transaction persistentTransaction = transactionRepo.save(transaction);
						// update transaction record table
						TransactionRecord s = new TransactionRecord();
						s.setCredit(0);
						s.setDebit(transactionDTO.getAmount());
						s.setBalance(senderAccount.getBalance());
						s.setUtr(persistentTransaction);
						TransactionRecord r = new TransactionRecord();
						r.setCredit(transactionDTO.getAmount());
						r.setDebit(0);
						r.setBalance(receiverAccount.getBalance());
						r.setUtr(persistentTransaction);
						transactionRecordRepo.save(s);
						transactionRecordRepo.save(r);
						// return response DTO
						TransactionResponseDTO response = new TransactionResponseDTO(persistentTransaction.getUtr(),
								sender, receiverCred.getUsername(), persistentTransaction.getTransactionDateTime(),
								s.getCredit(), s.getDebit(), s.getBalance());
						return ResponseDTO.builder().message("Transaction completed successfully!")
								.transactionDetails(response).build();
					} catch (Exception e) {
						throw new RuntimeException("Unexpected Error! Something went wrong!");
					}
				} else {
					throw new RuntimeException("Insufficient Balance!");
				}
			} else {
				throw new RuntimeException("Receiver's account is not ACTIVE!");
			}
		} else {
			throw new RuntimeException("Sender's account is not ACTIVE!");
		}
	}

	@Override
	public List<TransactionResponseDTO> getTransactionDetailsByUsername(String token) {
		String username = utils.getUserNameFromJwtToken(token);
		Credential credential = credRepo.findByUsername(username).orElseThrow();
		List<TransactionResponseDTO> results = null;
		if (credential.getRole().equals(RoleEnum.ROLE_CUSTOMER)) {
			results = transactionRepo.getAllTransactionsByUsername(username);
		} else {
			throw new RuntimeException("Access Denied for ADMIN!");
		}
		return results;
	}

	@Override
	public List<TransactionResponseDTO> getCustomTransactionDetails(String token, String startDate, String endDate) {
		String username = utils.getUserNameFromJwtToken(token);
		Credential credential = credRepo.findByUsername(username).orElseThrow();
		List<TransactionResponseDTO> results = null;
		if (credential.getRole().equals(RoleEnum.ROLE_CUSTOMER)) {
			results = transactionRepo.getAllTransactionsByDate(username,
					LocalDateTime.parse(startDate.concat("T00:00:00")),
					LocalDateTime.parse(endDate.concat("T00:00:00")));
		} else {
			throw new RuntimeException("Access Denied for ADMIN!");
		}
		if (results.isEmpty()) {
			throw new RuntimeException("No Transactions found!");
		}
		return results;
	}

	@Override
	public List<TransactionResponseDTO> getAllTransactions(String token) {
		String username = utils.getUserNameFromJwtToken(token);
		Credential credential = credRepo.findByUsername(username).orElseThrow();
		List<TransactionResponseDTO> results = null;
		if (credential.getRole().equals(RoleEnum.ROLE_ADMIN)) {
			results = transactionRepo.getAllTransactions();
		} else {
			throw new RuntimeException("Access Denied for CUSTOMER!");
		}
		return results;
	}

	@Override
	public ResponseDTO updateBalance(String token, @Valid TransactionRequestDTO transactionDTO) {
		String sender = utils.getUserNameFromJwtToken(token);
		Credential senderCred = credRepo.findByUsername(sender).orElseThrow();

		Credential receiverCred = credRepo.findByUsername(transactionDTO.getReceiver()).orElseThrow(
				() -> new UsernameNotFoundException("Invalid Username! Please provide existing username."));
		Account receiverAccount = accRepo.findByUsername(receiverCred)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid Account ID!"));

		if (senderCred.getRole().equals(RoleEnum.ROLE_CUSTOMER)) {
			throw new RuntimeException("Access Denied for CUSTOMER!");
		}
		if (sender.equals(receiverCred.getUsername())) {
			throw new RuntimeException("Amount cannot be transferred to self account!");
		}

		if (senderCred.getStatus().equals(Status.ACTIVE)) {
			if (receiverCred.getStatus().equals(Status.ACTIVE)) {
				try {
					// update balance in account table
					receiverAccount.setBalance(receiverAccount.getBalance() + transactionDTO.getAmount());
					accRepo.save(receiverAccount);
					// update transaction table
					Transaction transaction = new Transaction();
					transaction.setSender(sender);
					transaction.setReceiver(receiverCred);
					Transaction persistentTransaction = transactionRepo.save(transaction);
					// update transaction record table
					TransactionRecord r = new TransactionRecord();
					r.setCredit(transactionDTO.getAmount());
					r.setDebit(0);
					r.setBalance(receiverAccount.getBalance());
					r.setUtr(persistentTransaction);
					transactionRecordRepo.save(r);
					// return response DTO
					TransactionResponseDTO response = new TransactionResponseDTO(persistentTransaction.getUtr(), sender,
							receiverCred.getUsername(), persistentTransaction.getTransactionDateTime(), r.getCredit(),
							r.getDebit(), r.getBalance());
					return ResponseDTO.builder().message("Transaction completed successfully!")
							.transactionDetails(response).build();
				} catch (Exception e) {
					throw new RuntimeException("Unexpected Error! Something went wrong!");
				}
			} else {
				throw new RuntimeException("Receiver's account is not ACTIVE!");
			}
		} else {
			throw new RuntimeException("Admin's account is not ACTIVE!");
		}
	}

	// if data is wrapped up into Object[]
//	@Override
//	public List<TransactionResponseDTO> getTransactionDetailsByUsername(String token) {
//		String username = utils.getUserNameFromJwtToken(token);
//		Credential credential = credRepo.findByUsername(username).orElseThrow();
//		List<TransactionResponseDTO> transactions = new ArrayList<>();
//		if (credential.getRole().equals(RoleEnum.ROLE_CUSTOMER)) {
//			List<Object[]> results = transactionRepo.getAllTransactionsByUsername(username);
//	        for (Object[] result : results) {
//	            TransactionResponseDTO dto = new TransactionResponseDTO(
//	                    (String) result[0], // utr
//	                    (String) result[1], // sender
//	                    (String) result[2], // receiver
//	                    ((LocalDateTime) result[3]), // transactionDateTime
////	                    ((Timestamp) result[3]).toLocalDateTime(), // transactionDateTime
//	                    (Double) result[4], // credit
//	                    (Double) result[5], // debit
//	                    (Double) result[6]  // balance
//	            );
//	            transactions.add(dto);
//	        }
//		} else {
//			throw new RuntimeException("Access Denied for ADMIN!");
//		}
//        return transactions;
//	}

}
