/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.model.testcasegen;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author yanamalp
 */
public class ConvertFiletoString {
     static Logger log = Logger.getLogger(ConvertFiletoString.class.getName());
    public String convertFiletoString(String path)
    {
        File file=new File(path);
        BufferedReader br;
        String str="";
        try{
        
        br=new BufferedReader(new FileReader(file));
        String temp;
        while((temp=br.readLine())!=null)
        {
            str=str+temp +"\n";
        }
                  
        }
        catch(IOException e)
        {
            log.error("File not found"+e.getMessage());
        }
     
        return str;
    }
     
}
