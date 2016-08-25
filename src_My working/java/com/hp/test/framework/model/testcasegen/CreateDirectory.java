package com.hp.test.framework.model.testcasegen;

import java.io.File;

/** This method is to create directories if not exists
 *
 * @author sayedmo
 */
public class CreateDirectory {
    
    public static void createdir(String path)
    {	
        File files = new File(path);
	if (!files.exists()) {
		if (files.mkdirs()) {
			System.out.println("Directories are created!");
		} else {
			System.out.println("Failed to create directories!");
		}
	}
    }
 
}

