package com.app.repository;

import com.app.entities.Credential;
import com.app.entities.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOtpRepository extends JpaRepository<OTP, Long> {
	
	OTP findByEmailId(String email);
	
	Credential findByUsername(String username);

}
