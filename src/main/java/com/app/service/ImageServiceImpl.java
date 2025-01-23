package com.app.service;

import com.app.dto.ImageResDTO;
import com.app.dto.ResponseDTO;
import com.app.entities.Credential;
import com.app.repository.ICredentialRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Transactional
public class ImageServiceImpl implements IImageService {
	
	@Value("${file.upload.location}")
	private String baseFolder;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ICredentialRepository credRepo;

	@Override
	public ResponseDTO uploadPhoto(String usernameFromJWT, MultipartFile imageFile) throws IOException {
		Credential credential = credRepo.findByUsername(usernameFromJWT)
				.orElseThrow(() -> new RuntimeException("Invalid Identifier!"));
		String fileExtension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
		String completePath = baseFolder + File.separator + usernameFromJWT + fileExtension;
		System.out.println("Complete Path = " + completePath);
		long copy = Files.copy(imageFile.getInputStream(), Paths.get(completePath), StandardCopyOption.REPLACE_EXISTING);
		System.out.println("No. of bytes copied = " + copy);
		credential.setPhoto(completePath);
		mapper.map(credential, ImageResDTO.class);
//		return new ImageResDTO("Image uploaded successfully!", completePath);
		return ResponseDTO.builder().message("Image uploaded successfully!")
				.imageDetails(ImageResDTO.builder()
						.imagePath(completePath)
						.build())
				.build();
				
	}

	@Override
	public byte[] getProfilePhoto(String usernameFromJWT) throws IOException {
		Credential credential = credRepo.findByUsername(usernameFromJWT)
				.orElseThrow(() -> new RuntimeException("Invalid Identifier!"));
		String path = credential.getPhoto();
		System.out.println("GET photo path = " + path);
		return Files.readAllBytes(Paths.get(path));
	}

}
