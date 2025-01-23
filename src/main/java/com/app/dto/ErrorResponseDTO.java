package com.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {
	
	private String message;
	
	private LocalDateTime timeStamp;
	
	public ErrorResponseDTO(String message) {
		super();
		this.message = message;
		this.timeStamp=LocalDateTime.now();
	}
	

}
