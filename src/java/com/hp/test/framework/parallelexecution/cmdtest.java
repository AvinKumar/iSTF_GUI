package com.hp.test.framework.parallelexecution;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class cmdtest {
    
	public void executetestsuite(String buildpath) {
        try {
        	

        	String path="cd"+" " +buildpath+" "+"&& ant report";
        	System.out.println("This is the final command:"+path);
        	
            String[] command = new String[3];
            command[0] = "cmd";
            command[1] = "/c";
            command[2] = path;

            Process p = Runtime.getRuntime().exec(command);
            System.out.println("Triggered the command"+p);

            /*BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String Error;
            while ((Error = stdError.readLine()) != null) {
                System.out.println(Error);
            }
            while ((Error = stdInput.readLine()) != null) {
                System.out.println(Error);
            } */
        } catch (Exception e) {
            System.out.println("Exception occurred:"+e.fillInStackTrace());
           
        }
    }
}