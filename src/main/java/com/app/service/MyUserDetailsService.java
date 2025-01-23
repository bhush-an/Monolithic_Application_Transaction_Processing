package com.app.service;

import com.app.entities.Credential;
import com.app.repository.ICredentialRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private ICredentialRepository credRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Credential credential = credRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials! Please try again."));
		return new CustomUserDetails(credential);
	}

}
