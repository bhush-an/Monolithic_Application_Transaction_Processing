package com.app.utils;

import java.util.Random;

public class GenerateAccountId {

	public static String generateID() {
		Random random = new Random();
		long ID = (long) (1000000L + random.nextDouble() * 9000000L);
		return ("ACC").concat(String.valueOf(ID));
	}
}
