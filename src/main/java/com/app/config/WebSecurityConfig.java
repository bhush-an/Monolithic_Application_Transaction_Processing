package com.app.config;

import com.app.filters.JWTRequestFilter;
import com.app.service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig {

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private JWTRequestFilter filter;
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
	
	@Bean
	protected AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(myUserDetailsService);
		authProvider.setPasswordEncoder(encoder);
		return authProvider;
	}
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
		.exceptionHandling(auth -> auth.authenticationEntryPoint(
				(request, response, ex) -> {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }))
        .authorizeHttpRequests(
        		request -> request
        		.requestMatchers("/api/user/**", 
        				"/v2/api-docs", 
        				"/v3/api-docs", 
        				"/v3/api-docs/**",
        				"/swagger-resources",
        				"/swagger-resources/**",
        				"/configuration/ui",
        				"/configuration/security",
        				"/swagger-ui/**",
        				"/webjars/**",
        				"/swagger-ui.html").permitAll()
        		.requestMatchers("/api/admin/**").hasRole("ADMIN")
        		.requestMatchers("/api/customer/**").hasRole("CUSTOMER")
        		.requestMatchers(HttpMethod.OPTIONS).permitAll()
        		.anyRequest().authenticated())
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
}
