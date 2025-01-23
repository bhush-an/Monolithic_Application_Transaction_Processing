package com.app.service;

import com.app.dto.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {

	ResponseDTO uploadPhoto(String usernameFromJWT, MultipartFile imageFile) throws IOException;

	byte[] getProfilePhoto(String usernameFromJWT) throws IOException;

}
	