/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.objrepo;

/**
 *
 * @author sayedmo
 */


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import org.jdom2.Document;

public class ReadXMLFileUsingDom {

    public static Map<String, Set<String>> mapmainnode = new HashMap< String, Set<String>>();
    Map<String, List<String>> mapsubnode = new HashMap< String, List<String>>();

    NodeList nodeList1;
    List<String> content;
    List<String> Paremeters;
    List<String> Paremeters1;
    Set<String> ParentContent = new HashSet<String>();
    MaptoXML mx = new MaptoXML();

    
    
    public void ReadXMLModel(String xmlFilepath) {
        try {
                mapmainnode.clear();
                mapsubnode.clear();
            File xmlFile = new File(xmlFilepath);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String NodeName, NodeContent;
            //ParentContent = new ArrayList<String>();
            ParentContent = new HashSet<String>();
            Paremeters = new ArrayList<String>();

            Document document = documentBuilder.parse(xmlFile);
            Element root = document.getDocumentElement();
            
            root.normalize();
            NodeList nodeList = root.getChildNodes();

            System.out.println("Root element :" + document.getDocumentElement().getNodeName());
            System.out.println("===============================");

           

            for (int count = 0; count < nodeList.getLength(); count++) {
                Node elemNode = nodeList.item(count);
                if (elemNode.getNodeType() == Node.ELEMENT_NODE) {

                    NodeName = elemNode.getNodeName();
                    NodeContent = elemNode.getTextContent();

                    
                          if (elemNode.getChildNodes().getLength() > 1) {
                        nodeList1 = elemNode.getChildNodes();

                        nodeList1 = elemNode.getChildNodes();

                        printNodeList(nodeList1);

                    }

                    
                  

              

                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     *
     * @param nodeList
     */
    public void printNodeList(NodeList nodeList) {
        List<String> elements = new ArrayList<>();
        content = new ArrayList<>();

        for (int count = 0; count < nodeList.getLength(); count++) {
            Node elemNode = nodeList.item(count);
            if (elemNode.getNodeType() == Node.ELEMENT_NODE) {
                //elements.add((Element) elemNode);
                Node PName=elemNode.getParentNode();
                String ParentName=PName.getNodeName();
                String ParentAttributeValue = null;
                int i=0;
                if (PName.hasAttributes()) {
                    NamedNodeMap nodeMap = PName.getAttributes();
                    Node node = nodeMap.item(i);
                ParentAttributeValue=node.getNodeValue();
                }
              
                String NodeName =ParentName+"_"+elemNode.getNodeName()+"_"+ParentAttributeValue;
                String NodeContent = elemNode.getTextContent();
   
                
                       if (mapmainnode.containsKey(NodeName)) {
                        //List<String> lastSavedParameters = mapmainnode.get(NodeName);
                        Set<String> lastSavedParameters = mapmainnode.get(NodeName);
                        //ParentContent.addAll(lastSavedParameters);
                    } else {
                        ParentContent = new HashSet<String>();
                    }
                    ParentContent.add(NodeContent);
                    mapmainnode.put(NodeName, ParentContent);
                

            }

        }

    }

}
