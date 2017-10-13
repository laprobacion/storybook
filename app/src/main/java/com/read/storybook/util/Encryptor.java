package com.read.storybook.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class Encryptor {
	 private static String key = "Bar12345Bar12345"; // 128 bit key
	 private static String salt = "RandomInitVector"; // 16 bytes IV

	 public static String encrypt(String value) {
		 try {
			 DESKeySpec keySpec = new DESKeySpec(key.getBytes("UTF-8"));
			 SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			 SecretKey skey = keyFactory.generateSecret(keySpec);

			 Cipher cipher = Cipher.getInstance("DES");
			 cipher.init(Cipher.ENCRYPT_MODE, skey);
			 String encrypedPwd = Base64.encodeToString(cipher.doFinal(value.getBytes("UTF-8")), Base64.DEFAULT);
			 return encrypedPwd;
		 } catch (Exception e) {
		 }
		 return value;
	    }

	public static String decrypt(String encryptedPwd) {
		String decrypted = null;
		try {
			DESKeySpec keySpec = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey skey = keyFactory.generateSecret(keySpec);

			byte[] encryptedWithoutB64 = Base64.decode(encryptedPwd, Base64.DEFAULT);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			byte[] plainTextPwdBytes = cipher.doFinal(encryptedWithoutB64);
			decrypted = new String(plainTextPwdBytes);
			return decrypted;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decrypted;
	}


}
