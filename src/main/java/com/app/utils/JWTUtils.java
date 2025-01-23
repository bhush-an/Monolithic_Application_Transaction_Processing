package com.app.utils;

import com.app.entities.Credential;
import com.app.entities.User;
import com.app.repository.ICredentialRepository;
import com.app.repository.IUserRespository;
import com.app.service.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JWTUtils {

	@Value("${SECRET_KEY}")
	private String jwtSecret;
	
	@Value("${EXP_TIMEOUT}")
	private String jwtExpiry;
	
	@Autowired
	private ICredentialRepository credRepo;
	
	@Autowired
	private IUserRespository userRepo;
	
	private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	public String generateJwtToken(Authentication authentication) {
		CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
		Credential credential = credRepo.findByUsername(userPrincipal.getUsername()).orElseThrow();
		User user = userRepo.findByUserId(credential.getUserId().getUserId()).orElseThrow();
		return Jwts.builder()
				.subject(userPrincipal.getUsername())
				.issuedAt(new Date())
				.expiration(new Date(new Date().getTime() + Long.parseLong(jwtExpiry)))
				.signWith(getSignInKey())
				.claim("userId", user.getUserId())
				.compact();
	}
	
	private <T> T extractClaims(String token, Function<Claims, T> claimsFunction) {
		return claimsFunction.apply(
				Jwts.parser().verifyWith(getSignInKey()).build()
				.parseSignedClaims(token)
				.getPayload()
				);
	}
	
	public String getUserNameFromJwtToken(String token) {
		if (token.startsWith("Bearer ")) {
			String authToken = token.substring(7);
			return extractClaims(authToken, Claims::getSubject);
		}
		return extractClaims(token, Claims::getSubject);
	}
	
	public String getUserIdFromJwtToken(String token) {
		if (token.startsWith("Bearer ")) {
			String authToken = token.substring(7);
			return extractClaims(authToken, claims -> claims.get("userId", String.class));
		}
		return extractClaims(token, claims -> claims.get("userId", String.class));
	}
	
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().verifyWith(getSignInKey()).build()
			.parseSignedClaims(authToken);
			return true;
		} catch (Exception e) {
			log.error("Invalid JWT : " + e.getMessage());
		}
		return false;
	}
}
