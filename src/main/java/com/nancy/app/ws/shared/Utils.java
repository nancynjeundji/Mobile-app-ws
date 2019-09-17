package com.nancy.app.ws.shared;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {
	
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNPOQRSTUVWXYZ";
	
	//méthode pour generer un userID un peu plus securitaire
	public String  genererateUserId (int length) {
		return generatedRandomString(length);
	}

	private String generatedRandomString (int length) {
		StringBuffer returnedValue = new StringBuffer();
		
		for(int i=0; i<length; i++) {
			returnedValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return new String(returnedValue);
		
	}
}
