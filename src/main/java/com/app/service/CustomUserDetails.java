package com.app.service;

import com.app.entities.Credential;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class CustomUserDetails implements UserDetails {
	
	private Credential cred;

	public CustomUserDetails(Credential cred) {
		super();
		this.cred = cred;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> rolesAuthorities = new ArrayList<SimpleGrantedAuthority>();
		rolesAuthorities.add(new SimpleGrantedAuthority(cred.getRole().name()));
		return rolesAuthorities;
	}

	@Override
	public String getPassword() {
		return cred.getPassword();
	}

	@Override
	public String getUsername() {
		return cred.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
