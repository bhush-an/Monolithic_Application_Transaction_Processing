package com.app.service;

import com.app.dto.ForgotRequestDTO;
import com.app.dto.ForgotResponseDTO;
import com.app.dto.ResetPasswordRequestDTO;
import com.app.dto.ResetPasswordResponseDTO;
import com.app.entities.Credential;
import com.app.entities.OTP;
import com.app.entities.User;
import com.app.repository.ICredentialRepository;
import com.app.repository.IOtpRepository;
import com.app.repository.IUserRespository;
import com.app.utils.AppUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class OtpServiceImpl implements IOtpService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private IUserRespository userRepo;
	
	@Autowired
	private ICredentialRepository credRepo;
	
	@Autowired
	private IOtpRepository otpRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Value("${spring.mail.username}")
	private String emailSender;

	@Override
	public ForgotResponseDTO forgotPassword(@Valid ForgotRequestDTO forgotPasswordDTO) {
		User user = userRepo.findByEmailId(forgotPasswordDTO.getEmailId());
		if (user == null) {
			throw new RuntimeException("Email ID does not exists!");
		}
		Credential credential = credRepo.findByUsername(forgotPasswordDTO.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Invalid Username!"));
		
		OTP existingOtp = otpRepo.findByEmailId(forgotPasswordDTO.getEmailId());
		if (existingOtp != null) {
			otpRepo.delete(existingOtp);
		}
		
		String otp = AppUtils.generateOtp();
		otpRepo.save(OTP.builder()
				.emailId(forgotPasswordDTO.getEmailId())
				.username(forgotPasswordDTO.getUsername())
				.otp(otp)
				.createdAt(LocalDateTime.now())
				.expiresAt(LocalDateTime.now().plusMinutes(2))
				.build());
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailSender);
		message.setTo(forgotPasswordDTO.getEmailId());
		message.setSubject("OTP Verification to Reset Password.");
		message.setText("Please enter OTP : " + otp + " \n(Valid for 2 minutes only!)");
		javaMailSender.send(message);
		return new ForgotResponseDTO("OTP sent successfully to registered Email ID!", user.getEmailId(), credential.getUsername());
	}

	@Override
	public ResetPasswordResponseDTO resetPassword(@Valid ResetPasswordRequestDTO resetPasswordDTO) {
		OTP otp = otpRepo.findByEmailId(resetPasswordDTO.getEmailId());
		if (otp == null) {
			throw new RuntimeException("OTP not sent!");
		} 
		if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("OTP is expired!");
		}
		if (otp.getOtp().equals(resetPasswordDTO.getOtp())) {
			try {
			String username = otp.getUsername();
			Credential cred = credRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid Username!"));
			cred.setPassword(encoder.encode(resetPasswordDTO.getPassword()));
			credRepo.save(cred);
			return new ResetPasswordResponseDTO("Password successfully updated!", resetPasswordDTO.getEmailId(), otp.getUsername());
			} catch (RuntimeException e) {
				throw new RuntimeException("Username does not exists!");
			}
		}
		return new ResetPasswordResponseDTO("Something went wrong!", resetPasswordDTO.getEmailId(), otp.getUsername());
	}

}
