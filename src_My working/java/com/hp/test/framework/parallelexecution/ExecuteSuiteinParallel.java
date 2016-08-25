package com.hp.test.framework.parallelexecution;

import java.io.File;


public class ExecuteSuiteinParallel {
	public static ExecutionProperties ep=new ExecutionProperties();
	public static void main(String[] args) {
		
		CopyDirectory cpdir=new CopyDirectory();
		cmdtest executesuite=new cmdtest();
		String suitepath = null;
		
		String MainJellyPath=ep.getProperty("MAIN_JELLY_TESTS_LOCATION");
		File file = new File(MainJellyPath);
		
		String[] names = file.list();
		int i=0;
		String srcpath,destpath, propertypath, jellypathtosubstitute;
		
		srcpath=ep.getProperty("BUILD_SOURCE_LOCATION");

		String basejellypath=ep.getProperty("JELLY_TESTS_LOCATION");
		
		
		
		for(String name : names)
		{
		    if (new File(MainJellyPath+"\\" + name).isDirectory())
		    {
		        System.out.println("Name of Suite Directory:"+name);
		        suitepath=MainJellyPath+"\\"+name+"\\";
		        System.out.println("Path of Suite Directory:"+suitepath);
		              	        
		    }
		    jellypathtosubstitute=suitepath;
		    
		    System.out.println("Jelly file to be substituted:"+jellypathtosubstitute);
		    //Source directory which you want to copy to new location
		    destpath=srcpath+i;
		    System.out.println("Destion path to copy:"+destpath);
		    propertypath=destpath+"\\conf\\Model_File_TestCaseGen.properties";
		    System.out.println("Path of the dot properties file:"+propertypath);
	        File sourceFolder = new File(srcpath);
	         
	        //Target directory where files should be copied
	        File destinationFolder = new File(destpath);
	      try {  
	      cpdir.copyFolder(sourceFolder, destinationFolder);
	      cpdir.ModifyLine(propertypath,basejellypath,jellypathtosubstitute);
	      } catch(Exception e) {
	    	  System.out.println("Exception occurred while copy or modifying"+e.getMessage());
	      }
	      
	      executesuite.executetestsuite(destpath);
	      i++;
		}
	}

}
