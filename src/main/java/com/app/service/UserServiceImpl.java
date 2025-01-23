package com.app.service;

import com.app.dto.*;
import com.app.entities.*;
import com.app.repository.IAccountRepository;
import com.app.repository.IAddressRepository;
import com.app.repository.ICredentialRepository;
import com.app.repository.IUserRespository;
import com.app.utils.GenerateUserId;
import com.app.utils.GenerateUserIdImpl;
import com.app.utils.JWTUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
	
	@Value("${pincode.api}")
	private String pincodeAPI;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private JWTUtils utils;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Value("${EXP_TIMEOUT}")
	private String jwtExpiry;
	
	@Autowired
	private IUserRespository userRepo;
	
	@Autowired
	private IAddressRepository addressRepo;
	
	@Autowired
	private ICredentialRepository credRepo;
	
	@Autowired
	private IAccountRepository accRepo;

	@Override
	public ResponseDTO registerCustomerDetails(@Valid UserRequestDTO userDTO) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		User user = mapper.map(userDTO, User.class);
		User userId = userRepo.findByEmailId(userDTO.getEmailId());
		if (userId == null) {
			Field field = user.getClass().getDeclaredField("userId");
			field.setAccessible(true);
			if (field.isAnnotationPresent(GenerateUserId.class)) {
				String generateId = GenerateUserIdImpl.generate();
				field.set(user, generateId);
			}
		}
		else {
			user.setUserId(userId.getUserId());
		}
		User persistentUser = userRepo.save(user);
//		return new UserResponseDTO("User created successfully! User ID : " + persistentUser.getUserId(), 
//				LocalDateTime.now(), persistentUser.getFirstName(),persistentUser.getLastName(), 
//				persistentUser.getEmailId(), persistentUser.getContactNumber(),
//				persistentUser.getDob(), persistentUser.getGender());
		return ResponseDTO.builder().message("User created successfully! User ID : " + persistentUser.getUserId())
				.userDetails(mapper.map(persistentUser, UserResponseDTO.class))
				.build();
	}

	@Override
	public ResponseDTO registerAddressDetails(String userId, @Valid AddressRequestDTO addressDTO) {
		Address address = mapper.map(addressDTO, Address.class);
		User user = userRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Invalid User ID!"));
		address.setUser(user);
		ResponseEntity<String> response = restTemplate.getForEntity(pincodeAPI + addressDTO.getPincode(), String.class);
		JSONArray jsonArray = new JSONArray(response.getBody());
		if (response.getStatusCode().equals(HttpStatus.OK) && 
				jsonArray.getJSONObject(0).getString("Status").equalsIgnoreCase("Success")) {
	        JSONObject object = jsonArray.getJSONObject(0).getJSONArray("PostOffice").getJSONObject(0);
	        address.setCity(object.getString("Block"));
	        address.setState(object.getString("State"));
	        address.setCountry(object.getString("Country"));
		} else {
		    throw new RuntimeException("Invalid Pincode : " + addressDTO.getPincode());
		}
		Address persistentAddress = addressRepo.save(address);
//		return new AddressResponseDTO("Address added successfully for User ID : " + persistentAddress.getUser().getUserId(),
//				persistentAddress.getBuilding(), persistentAddress.getPincode(), persistentAddress.getCity(), 
//				persistentAddress.getState(), persistentAddress.getCountry());
		return ResponseDTO.builder().message("Address added successfully for User ID : " + persistentAddress.getUser().getUserId())
				.addressDetails(mapper.map(persistentAddress, AddressResponseDTO.class))
				.build();
	}

	@Override
	public ResponseDTO registerCustomerAccountDetails(String userId, @Valid CustomerAccountRequestDTO accountDTO) {
		Credential credential = mapper.map(accountDTO, Credential.class);
		Account account = mapper.map(accountDTO, Account.class);
		if (credRepo.findByUsername(accountDTO.getUsername()).isPresent()) {
			throw new RuntimeException("Username already exists! Please try some other username.");
		}
		User user = userRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Invalid User ID!"));
		credential.setPassword(encoder.encode(credential.getPassword()));
		credential.setUserId(user);
		credential.setStatus(Status.INACTIVE);
		credential.setRole(RoleEnum.ROLE_CUSTOMER);
		Credential persistentCredential = credRepo.save(credential);
		credRepo.flush();
		
		Credential username = credRepo.findByUsername(persistentCredential.getUsername())
				.orElseThrow(() -> new RuntimeException("Invalid Username!"));
		account.setUsername(username);
		account.setBalance(0);
		Account persistentAccount = accRepo.save(account);
//		return new CustomerAccountResponseDTO("Account successfully created! Account ID : " + persistentAccount.getAccountId(), 
//				LocalDateTime.now(), persistentCredential.getUsername(), persistentAccount.getAccType(), persistentAccount.getBalance(), 
//				persistentCredential.getStatus(), persistentCredential.getRole(), LocalDateTime.now(), LocalDateTime.now());
		return ResponseDTO.builder().message("Account successfully created! Account ID : " + persistentAccount.getAccountId())
				.customerDetails(mapper.map(persistentCredential, CustomerAccountResponseDTO.class))
				.build();
	}

	@Override
	public ResponseDTO registerAdminAccountDetails(String userId, @Valid AdminAccountRequestDTO accountDTO) {
		Credential credential = mapper.map(accountDTO, Credential.class);
		if (credRepo.findByUsername(accountDTO.getUsername()).isPresent()) {
			throw new RuntimeException("Username already exists! Please try some other username.");
		}
		User user = userRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Invalid User ID!"));
		credential.setPassword(encoder.encode(credential.getPassword()));
		credential.setUserId(user);
		credential.setStatus(Status.INACTIVE);
		credential.setRole(RoleEnum.ROLE_ADMIN);
		Credential persistentCredential = credRepo.save(credential);
//		return new AdminAccountResponseDTO("Admin Account successfully created!", LocalDateTime.now(), 
//				persistentCredential.getUsername(), persistentCredential.getStatus(), persistentCredential.getRole(), 
//				LocalDateTime.now(), LocalDateTime.now());
		return ResponseDTO.builder().message("Admin Account successfully created!")
				.adminDetails(mapper.map(persistentCredential, AdminAccountResponseDTO.class))
				.build();
	}

	@Override
	public ResponseDTO loginWithCredentials(@Valid LoginRequestDTO loginDTO) {
		Credential credential = credRepo.findByUsername(loginDTO.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials!"));
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), 
				loginDTO.getPassword());
		Authentication authenticationDetails = manager.authenticate(authToken);
		if (credential.getStatus().equals(Status.ACTIVE)) {
			try {
//				return new LoginResponseDTO("Authentication successful!", utils.generateJwtToken(authenticationDetails), 
//					(Long.parseLong(jwtExpiry)) / (60 * 1000));
				return ResponseDTO.builder().message("Authentication successful!")
						.loginDetails(LoginResponseDTO.builder()
								.jwt(utils.generateJwtToken(authenticationDetails))
								.expiresInMin((Long.parseLong(jwtExpiry)) / (60 * 1000))
								.build())
						.build();
			} catch (BadCredentialsException e) {
				throw new RuntimeException("Invalid Credentials! Please try again.");
			}
		}
//		return new LoginResponseDTO("Authentication failed since account is not ACTIVE!", "NA", 0);
		return ResponseDTO.builder().message("Authentication failed since account is not ACTIVE!")
				.loginDetails(LoginResponseDTO.builder()
						.jwt("NA")
						.expiresInMin(0)
						.build())
				.build();
	}

	@Override
	public ResponseDTO getProfileDetails(String token) {
		String userIdFromToken = utils.getUserIdFromJwtToken(token);
		User user = userRepo.findById(userIdFromToken).orElseThrow(() -> new RuntimeException("Invalid User!"));
		Address addr = addressRepo.findByUser(user);
//		return new ProfileResponseDTO(user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmailId(), 
//				user.getContactNumber(), user.getDob(), user.getGender(), new ProfileAddrResponseDTO(addr.getBuilding(), 
//						addr.getPincode(), addr.getCity(), addr.getState(), addr.getCountry()));
		return ResponseDTO.builder().userDetails(mapper.map(user, UserResponseDTO.class))
				.addressDetails(mapper.map(addr, AddressResponseDTO.class)).build();
	}

	@Override
	public ResponseDTO updateUserStatus(@Valid UpdateStatusDTO updateSatusDTO) {
		if (!(updateSatusDTO.getStatus().equals(Status.ACTIVE) || updateSatusDTO.getStatus().equals(Status.INACTIVE)
				|| updateSatusDTO.getStatus().equals(Status.BLOCK) || updateSatusDTO.getStatus().equals(Status.DELETE))) {
			throw new RuntimeException("Status should be any of ACTIVE / INACTIVE / BLOCKED");
		}
		Credential credential = credRepo.findByUsername(updateSatusDTO.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Username does not exists!"));
		credential.setStatus(updateSatusDTO.getStatus());
		Credential persistentCred = credRepo.save(credential);
		return ResponseDTO.builder().message("Status updated successfully!")
				.customerDetails(mapper.map(persistentCred, CustomerAccountResponseDTO.class)).build();
	}

	@Override
	public ResponseDTO deleteBankAccount(String token) {
		String usernameFromToken = utils.getUserNameFromJwtToken(token);
		Credential cred = credRepo.findByUsername(usernameFromToken).orElseThrow();
		cred.setStatus(Status.DELETE);
		Credential persistentCred = credRepo.save(cred);
		return ResponseDTO.builder().message("Bank account deleted successfully!")
				.customerDetails(mapper.map(persistentCred, CustomerAccountResponseDTO.class)).build();
	}

}
