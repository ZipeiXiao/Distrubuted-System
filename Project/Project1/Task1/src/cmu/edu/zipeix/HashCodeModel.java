package cmu.edu.zipeix;

/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Sept. 21, 2019
 *
 * This simple program asks the user to enter a string of text data, and to make a
 * choice of two hash functions - MD5 or SHA-256. When the submit button is pressed, the
 * original text will be echoed back to the browser along with the name of the hash, and the
 * hash value. The hash values sent back to the browser should be displayed in two forms: as
 * hexadecimal text and as base 64 notation.
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class HashCodeModel {

	/**
	 * To compute the SHA-256 hashes
	 * @param str text input by user
	 * @return SHA-256 hashes in Hex format
	 */
	static String getSHA256Hex(String str) {
		MessageDigest messageDigest = null;

		try {
			// use the SHA-256 method to calculate hash
			messageDigest = MessageDigest.getInstance("SHA-256");

			messageDigest.reset();

			// calculate the hashValue of input text
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// Get the byte result of the hash Value
		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	/**
	 * To compute the MD5 hashes
	 * @param str text users input
	 * @return MD5 hashes in Hex format
	 */
	static String getMD5Hex(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			// calculate the hashValue of input text
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	/**
	 * To compute the MD5 Hash values
	 * @param str text user input
	 * @return MD5 Hash values in base 64 format
	 */
	static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			// calculate the hashValue of input text
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		// Base64 encoding
		Base64 base64 = new Base64();
		return base64.encodeToString(byteArray);
	}

	/**
	 * To compute SHA256 Hash
	 * @param str text input
	 * @return SHA256 Hash Value in base 64 format
	 */
	static String getSHA256Str(String str) {
		MessageDigest messageDigest = null;

		try {
			// use the SHA-256 method to calculate hash
			messageDigest = MessageDigest.getInstance("SHA-256");

			messageDigest.reset();

			// calculate the hashValue of input text
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		// Base64 encoding
		Base64 base64 = new Base64();
		return base64.encodeToString(byteArray);
	}
}
