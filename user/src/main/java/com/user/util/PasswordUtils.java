package com.user.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {

	private static final int ITERATIONCOUNT = 1000;
	private static final int PASSKEYLENGTH = 256;
	private static final Random RANDOMVAL = new SecureRandom();
	private static final String KEYS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	public static String getSaltValue(int length) {
		StringBuilder saltValue = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			saltValue.append(KEYS.charAt(RANDOMVAL.nextInt(KEYS.length())));
		}
		return new String(saltValue);
	}
	
	public static String generatePasswordUsingSalt(String password, String salt) {
		byte[] generatedPassword = hash(password.toCharArray(), salt.getBytes());
		return Base64.getEncoder().encodeToString(generatedPassword);
	}

	public static byte[] hash(char[] password, byte[] salt) {
		//Password Based Encryption
		PBEKeySpec keySpec = new PBEKeySpec(password, salt, ITERATIONCOUNT, PASSKEYLENGTH);
		Arrays.fill(password, Character.MIN_VALUE);
		try {
			//Here I used Password based Key Derivative Function-Hash Message Authentication Code - SHA1 Algorithm
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(keySpec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error occurred when hash the password: " + e.getMessage(), e);
		} finally {
			keySpec.clearPassword();
		}
	}


	public static boolean authenticateByPassword(String userPassword, String generatedPassword, String salt) {
		// using same salt generate the password
		String passwordReAuthenticate = generatePasswordUsingSalt(userPassword, salt);
		// validate if both equal
		return  passwordReAuthenticate.equalsIgnoreCase(generatedPassword);
	}
}