package com.hp.test.framework.MD5Hash.hashvalue;

import java.io.File;

/**
 * Test generating hash values from File.
 * @author sayedmo
 *
 */
public class TestFileHashGenerator {

	public static void FileHashGenerator(String filePath) {
		try {
			
			System.out.println("File Path: " + filePath);
			File file = new File(filePath);
			
			String md5Hash = HashGeneratorUtils.generateMD5(file);
			System.out.println("MD5 Hash: " + md5Hash);
			
			String sha1Hash = HashGeneratorUtils.generateSHA1(file);
			System.out.println("SHA-1 Hash: " + sha1Hash);

			String sha256Hash = HashGeneratorUtils.generateSHA256(file);
			System.out.println("SHA-256 Hash: " + sha256Hash);			

		} catch (HashGenerationException ex) {
			ex.printStackTrace();
		}
	}

}
