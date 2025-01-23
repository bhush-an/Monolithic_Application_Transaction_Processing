package com.app.service;

import com.app.dto.ForgotRequestDTO;
import com.app.dto.ForgotResponseDTO;
import com.app.dto.ResetPasswordRequestDTO;
import com.app.dto.ResetPasswordResponseDTO;
import jakarta.validation.Valid;

public interface IOtpService {

	ForgotResponseDTO forgotPassword(@Valid ForgotRequestDTO forgotPasswordDTO);

	ResetPasswordResponseDTO resetPassword(@Valid ResetPasswordRequestDTO resetPasswordDTO);
	
}
