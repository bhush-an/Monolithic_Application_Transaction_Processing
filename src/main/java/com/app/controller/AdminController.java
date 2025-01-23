package com.app.controller;

import com.app.dto.TransactionRequestDTO;
import com.app.dto.UpdateStatusDTO;
import com.app.service.IAccountService;
import com.app.service.ITransactionService;
import com.app.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private ITransactionService transactionService;
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfileDetails(@RequestHeader(value = "Authorization") String token) {
		return ResponseEntity.ok(userService.getProfileDetails(token));
	}
	
	@PutMapping("/updateStatus")
	public ResponseEntity<?> updateUserStatus(@RequestBody @Valid UpdateStatusDTO updateSatusDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserStatus(updateSatusDTO));
	}
	
	@GetMapping("/account")
	public ResponseEntity<?> getCustomAccount(@RequestParam(required = false) String account, 
			@RequestParam(required = false) String username, @RequestParam(required = false) String accType) {
		return ResponseEntity.ok(accountService.findAccountByParameter(account, username, accType));
	}

	@GetMapping("/transactions")
	public ResponseEntity<?> getAllTransactions(@RequestHeader(value = "Authorization") String token) {
		return ResponseEntity.ok(transactionService.getAllTransactions(token));
	}
	
	@PutMapping("/updateBalance")
	public ResponseEntity<?> updateCustomerBalance(@RequestHeader(value = "Authorization") String token,
			@RequestBody @Valid TransactionRequestDTO transactionDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.updateBalance(token, transactionDTO));
	}
}
