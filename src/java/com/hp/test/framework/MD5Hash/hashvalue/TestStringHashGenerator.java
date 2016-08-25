package com.hp.test.framework.MD5Hash.hashvalue;
/**
 * Test generating hash values from String.
 * @author sayed
 *
 */
public class TestStringHashGenerator {

	public static void StringHashGenerator(String inputString) {
		try {
			
			System.out.println("Input String: " + inputString);

			String md5Hash = HashGeneratorUtils.generateMD5(inputString);
			System.out.println("MD5 Hash: " + md5Hash);

			String sha1Hash = HashGeneratorUtils.generateSHA1(inputString);
			System.out.println("SHA-1 Hash: " + sha1Hash);

			String sha256Hash = HashGeneratorUtils.generateSHA256(inputString);
			System.out.println("SHA-256 Hash: " + sha256Hash);
		} catch (HashGenerationException ex) {
			ex.printStackTrace();
		}
	}

}
