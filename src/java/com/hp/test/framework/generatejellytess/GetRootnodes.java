/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.generatejellytess;

 import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yanamalp
 */
public class GetRootnodes {
    
   public static  ArrayList<String> orderof_Elements_list;
static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRootnodes.class.getName());
    
    public static  ArrayList GetrootLocators(String Testcases_path) {
        //   ReadTestCases.verfiy=verification;
        orderof_Elements_list=new ArrayList<String>();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc;
            doc = documentBuilder.parse(new File(Testcases_path));
            getorderofnodes(null, doc.getDocumentElement());
            
        } catch (Exception exe) {
           log.error("Error in getting nodes"+exe.getMessage());
                   
        }
              
return orderof_Elements_list;
    }

    public static void getorderofnodes(Node parentNode, Node node) {

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE: {
             
                if (node.hasChildNodes()) {
                    
                    NodeList list = node.getChildNodes();
                    int size = list.getLength();
                    if(size > 1)
                    {
                        // System.out.println("******" + node.toString());
                         String temp_ar[]=node.toString().split(":");
                         String node_temp=temp_ar[0].substring(1);
                         orderof_Elements_list.add(node_temp);
                    }
                    for (int index = 0; index < size; index++) {
                        getorderofnodes(node, list.item(index));
                    }
                }

                break;
            }

            case Node.TEXT_NODE: {
                String data = node.getNodeValue();

                if (data.trim().length() > 0) {
                 //   System.out.println((parentNode.getNodeName()+"::"+node.getNodeValue()));
                }
                break;
            }

        }
    }
}
    

