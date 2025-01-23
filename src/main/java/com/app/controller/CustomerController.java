package com.app.controller;

import com.app.dto.TransactionRequestDTO;
import com.app.service.IAccountService;
import com.app.service.ITransactionService;
import com.app.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
	
	@Autowired
	private IAccountService accService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ITransactionService transactionService;

	@GetMapping("/accdetails")
	public ResponseEntity<?> getAccountDetails(@RequestHeader(value = "Authorization") String token) {
		return ResponseEntity.ok(accService.getBankAccountDetails(token));
	}
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfileDetails(@RequestHeader(value = "Authorization") String token) {
		return ResponseEntity.ok(userService.getProfileDetails(token));
	}
	
	@PutMapping("/send")
	public ResponseEntity<?> transferAmount(@RequestHeader(value = "Authorization") String token,
			@RequestBody @Valid TransactionRequestDTO transactionDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.transferAmount(token, transactionDTO));
	}
	
	@GetMapping("/transactions")
	public ResponseEntity<?> getTransactionDetailsByUsername(@RequestHeader(value = "Authorization") String token) {
		return ResponseEntity.ok(transactionService.getTransactionDetailsByUsername(token));
	}
	
	@GetMapping("/fetchTransactions")
	public ResponseEntity<?> getCustomTransactionDetails(@RequestHeader(value = "Authorization") String token,
			@RequestParam String startDate, @RequestParam String endDate) {
		return ResponseEntity.ok(transactionService.getCustomTransactionDetails(token, startDate, endDate));
	}
	
	@PutMapping("/deleteAccount")
	public ResponseEntity<?> deleteBankAccount(@RequestHeader(value = "Authorization") String token) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.deleteBankAccount(token));
	}
}
