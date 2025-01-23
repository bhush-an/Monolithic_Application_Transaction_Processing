package com.app;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@SpringBootApplication
public class IbsBackendApplication implements CommandLineRunner {
	
	@Value("${file.upload.location}")
	private String fileLocation;

	public static void main(String[] args) {
		SpringApplication.run(IbsBackendApplication.class, args);
	}
	
	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}
	
	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... args) throws Exception {
		File dir = new File(fileLocation);
		if (!dir.exists()) {
			System.out.println("New folder created = " + dir.mkdirs());
		} else {
			System.out.println("Folder already exists = " + fileLocation);
		}
	}
}
