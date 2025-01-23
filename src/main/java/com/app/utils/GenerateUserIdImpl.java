package com.app.utils;

import java.util.Random;

public class GenerateUserIdImpl {
	
	public static String generate() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for(int i=0; i<6; i++) {
			sb.append(random.nextInt(10));
		}
		return ("USR-").concat(sb.toString());
	}

}
