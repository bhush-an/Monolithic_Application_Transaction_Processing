package com.app.controller;

import com.app.dto.*;
import com.app.service.IImageService;
import com.app.service.IOtpService;
import com.app.service.IUserService;
import com.app.utils.JWTUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserAuthenticationController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IImageService imageService;
	
	@Autowired
	private IOtpService otpService;
	
	@Autowired
	private JWTUtils utils;
	
	@PostMapping("/registerCustomer")
	public ResponseEntity<?> registerCustomer(@RequestBody @Valid UserRequestDTO userRequestDTO) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerCustomerDetails(userRequestDTO));
	}
	
	@PostMapping("/registerAddress/{userId}")
	public ResponseEntity<?> registerAddress(@PathVariable String userId, @RequestBody @Valid AddressRequestDTO addressRequestDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerAddressDetails(userId, addressRequestDTO));
	}
	
	@PostMapping("/registerCustomerAccount/{userId}")
	public ResponseEntity<?> registerCustomerAccount(@PathVariable String userId, @RequestBody @Valid CustomerAccountRequestDTO accountDTO) {
			return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerCustomerAccountDetails(userId, accountDTO));
	}
	
	@PostMapping("/registerAdminAccount/{userId}")
	public ResponseEntity<?> registerAdminAccount(@PathVariable String userId, @RequestBody @Valid AdminAccountRequestDTO accountDTO) {
			return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerAdminAccountDetails(userId, accountDTO));
	}
	
	@PostMapping("/login") 
	public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequestDTO loginDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.loginWithCredentials(loginDTO));
	}
	
	@PostMapping("/forgot")
	public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotRequestDTO forgotPasswordDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(otpService.forgotPassword(forgotPasswordDTO));
	}
	
	@PutMapping("/reset")
	public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO resetPasswordDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(otpService.resetPassword(resetPasswordDTO));
	}

	@PostMapping("/profilePhoto")
	public ResponseEntity<?> uploadProfilePhoto(@RequestHeader(value = "Authorization") String token,
			@RequestParam MultipartFile imageFile) throws IOException {
		String usernameFromJWT = utils.getUserNameFromJwtToken(token);
		System.out.println("Uploaded image file name : " + imageFile.getOriginalFilename());
		System.out.println("Content type : " + imageFile.getContentType());
		System.out.println("Size : " + imageFile.getSize());
		return ResponseEntity.status(HttpStatus.CREATED).body(imageService.uploadPhoto(usernameFromJWT, imageFile));
	}
	
	@GetMapping(value = "/profilePhoto", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<?> viewProfilePhoto(@RequestHeader(value = "Authorization") String token) throws IOException {
		String usernameFromJWT = utils.getUserNameFromJwtToken(token);
		return ResponseEntity.status(HttpStatus.OK).body(imageService.getProfilePhoto(usernameFromJWT));
	}
}
