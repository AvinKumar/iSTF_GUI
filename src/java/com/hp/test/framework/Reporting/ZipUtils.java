/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.Reporting;

/**
 *
 * @author yanamalp
 */
//Import all needed packages

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils
{
static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ZipUtils.class.getName());
private List<String> fileList;
private static   String OUTPUT_ZIP_FILE = "";
private static   String SOURCE_FOLDER="";
public ZipUtils()
{
   fileList = new ArrayList<String>();
}

public static void ZipFolder(String SOURCE_FOLDER,String OUTPUT_ZIP_FILE)
{
    ZipUtils.OUTPUT_ZIP_FILE=OUTPUT_ZIP_FILE;
    ZipUtils.SOURCE_FOLDER=SOURCE_FOLDER;
    System.out.println("source"+ZipUtils.SOURCE_FOLDER);
   ZipUtils appZip = new ZipUtils();
   appZip.generateFileList(new File(SOURCE_FOLDER));
   appZip.zipIt(OUTPUT_ZIP_FILE);
}

public void zipIt(String zipFile)
{
   byte[] buffer = new byte[1024];
   String source = "";
   FileOutputStream fos = null;
   ZipOutputStream zos = null;
   try
   {
      try
      {
         source = SOURCE_FOLDER.substring(SOURCE_FOLDER.lastIndexOf("\\") + 1, SOURCE_FOLDER.length());
      }
     catch (Exception e)
     {
        source = SOURCE_FOLDER;
     }
     fos = new FileOutputStream(zipFile);
     zos = new ZipOutputStream(fos);

     log.info("Output to Zip : " + zipFile);
     FileInputStream in = null;

     for (String file : this.fileList)
     {
       
        ZipEntry ze = new ZipEntry(source + File.separator + file);
        zos.putNextEntry(ze);
        try
        {
           in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
           int len;
           while ((len = in.read(buffer)) > 0)
           {
              zos.write(buffer, 0, len);
           }
        }
        finally
        {
           in.close();
        }
     }

     zos.closeEntry();
     log.info("Folder successfully compressed");

  }
  catch (IOException ex)
  {
     ex.printStackTrace();
  }
  finally
  {
     try
     {
        zos.close();
     }
     catch (IOException e)
     {
        e.printStackTrace();
     }
  }
}

public void generateFileList(File node)
{

  // add file only
  if (node.isFile())
  {
     fileList.add(generateZipEntry(node.toString()));

  }

  if (node.isDirectory())
  {
     String[] subNote = node.list();
     for (String filename : subNote)
     {
        generateFileList(new File(node, filename));
     }
  }
}

private String generateZipEntry(String file)
{
   return file.substring(SOURCE_FOLDER.length() + 1, file.length());
}
}    
