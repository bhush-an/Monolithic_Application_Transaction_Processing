package com.app.entities;

import lombok.Getter;

@Getter
public enum SecurityQuestion {
	ONE("Who was your childhood hero?"), TWO("What was your first car?"), THREE("What is the name of your first pet?"),
	FOUR("In which city were you born?"), FIVE("What was your favorite food as a child?"),
	SIX("What is your mother's maiden name?");

	private final String securityQuestion;

	SecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
}
