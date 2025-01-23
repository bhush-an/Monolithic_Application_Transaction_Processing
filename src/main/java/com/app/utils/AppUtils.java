package com.app.utils;

import java.util.Random;

public class AppUtils {
	
	public static String generateOtp() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for(int i=0; i<6; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

}
