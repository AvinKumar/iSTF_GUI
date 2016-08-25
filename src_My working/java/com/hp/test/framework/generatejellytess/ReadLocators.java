package com.hp.test.framework.generatejellytess;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yanamalp
 */
public class ReadLocators {
    public static  Map<String,String> Locators=new HashMap<String,String>();
 // public static   ArrayList<String> Verifications=new ArrayList<String>();
     public static boolean verfiy=false;
     public static int inc=0;
      static final Logger log = Logger.getLogger(ReadLocators.class.getName());
    public static  Map<String,String> GetLocators(String Testcases_path){
    //   ReadTestCases.verfiy=verification;
    try{
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc; 

        doc = documentBuilder.parse(new File(Testcases_path));
        getData(null, doc.getDocumentElement());
     
    }catch(Exception exe){
        log.error(exe.getMessage());
    }
   // Map<String,List<String>> actions_exp=new  HashMap<String,List<String>>();
   
    
   return Locators;
}

public static  void getData(Node parentNode, Node node){

    switch(node.getNodeType()){
        case Node.ELEMENT_NODE:{

            if(node.hasChildNodes()){
                NodeList list = node.getChildNodes();
                int size = list.getLength();

                for(int index = 0; index < size; index++){
                    getData(node, list.item(index));
                }
            }

            break;
        }

        case Node.TEXT_NODE:{
            String data = node.getNodeValue();

            if(data.trim().length() > 0){
              Locators.put(parentNode.getNodeName(),node.getNodeValue());
            }
            break;
        }

    }
}
    
}
