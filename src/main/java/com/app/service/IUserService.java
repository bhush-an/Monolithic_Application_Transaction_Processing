package com.app.service;

import com.app.dto.*;
import jakarta.validation.Valid;

public interface IUserService {

	ResponseDTO registerCustomerDetails(@Valid UserRequestDTO userDTO) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;

	ResponseDTO registerAddressDetails(String userId, @Valid AddressRequestDTO addressDTO);

	ResponseDTO registerCustomerAccountDetails(String userId, @Valid CustomerAccountRequestDTO accountDTO);

	ResponseDTO registerAdminAccountDetails(String userId, @Valid AdminAccountRequestDTO accountDTO);

	ResponseDTO loginWithCredentials(@Valid LoginRequestDTO loginDTO);

	ResponseDTO getProfileDetails(String token);

	ResponseDTO updateUserStatus(@Valid UpdateStatusDTO updateSatusDTO);

	ResponseDTO deleteBankAccount(String token);

}
