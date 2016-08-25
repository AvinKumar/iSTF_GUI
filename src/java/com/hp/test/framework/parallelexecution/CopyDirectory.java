package com.hp.test.framework.parallelexecution;



import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
 
public class CopyDirectory
{

    /**
     * This function recursively copy all the sub folder and files from sourceFolder to destinationFolder
     * */
    public void copyFolder(File sourceFolder, File destinationFolder) throws IOException
    {
        //Check if sourceFolder is a directory or file
        //If sourceFolder is file; then copy the file directly to new location
        if (sourceFolder.isDirectory())
        {
            //Verify if destinationFolder is already present; If not then create it
            if (!destinationFolder.exists())
            {
                destinationFolder.mkdir();
                System.out.println("Directory created :: " + destinationFolder);
            }
             
            //Get all files from source directory
            String files[] = sourceFolder.list();
             
            //Iterate over all files and copy them to destinationFolder one by one
            for (String file : files)
            {
                File srcFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);
                 
                //Recursive function call
                copyFolder(srcFile, destFile);
            }
        }
        else
        {
            //Copy the file content from one place to another
            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied :: " + destinationFolder);
        }
    }
    
    /* change the jelly path value in properties file */
    public void ModifyLine(String proppath, String basejellypath, String jellytosubstitute) throws IOException  
    {
        
    	basejellypath=basejellypath.replaceAll("\\\\", "/");
    	jellytosubstitute=jellytosubstitute.replaceAll("\\\\", "/");
    	System.out.println("base jelly path from modify method:"+basejellypath);
    	System.out.println("jelly to replace path from modify method:"+jellytosubstitute);
    	File file = new File(proppath);
        String fileContext = FileUtils.readFileToString(file);
        fileContext = fileContext.replace("\\\\", "/");
        
        fileContext = fileContext.replaceAll(basejellypath, jellytosubstitute);
        FileUtils.write(file, fileContext);
    }
    
}
