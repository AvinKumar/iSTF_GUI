/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.model.testcasegen;

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

public class ReadXMLTemplatewithoutDB {

    public static Map<String, Set<String>> mapmainnode = new HashMap< String, Set<String>>();
    Map<String, List<String>> mapsubnode = new HashMap< String, List<String>>();

    NodeList nodeList1;
    List<String> content;
    List<String> Paremeters;
    List<String> Paremeters1;
    Set<String> ParentContent = new HashSet<String>();
   

    public void ReadXMLModel(String xmlFilepath) {
        try {

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

            if (root.hasAttributes()) {
                NamedNodeMap nodeMap = root.getAttributes();

                for (int i = 0; i < nodeMap.getLength(); i++) {

                    Node node = nodeMap.item(i);
                    System.out.println("Product Name: " + node.getNodeName());
                    System.out.println("Feature Name: " + node.getNodeValue());

                }
            }

            for (int count = 0; count < nodeList.getLength(); count++) {
                Node elemNode = nodeList.item(count);
                if (elemNode.getNodeType() == Node.ELEMENT_NODE) {

                    // get node name and value
                    //System.out.println("\nNode Name =" + elemNode.getNodeName() + " [OPEN]");
                    //System.out.println("Node Content =" + elemNode.getTextContent());
                    //System.out.println("Node Value =" + elemNode.getNodeValue());
                    NodeName = elemNode.getNodeName();
                    NodeContent = elemNode.getTextContent();


                    
                          if (elemNode.getChildNodes().getLength() > 1) {
                        nodeList1 = elemNode.getChildNodes();

                        nodeList1 = elemNode.getChildNodes();

                        //printNodeList(elemNode.getChildNodes());
                        printNodeList(nodeList1);

                    }

                    if (elemNode.hasAttributes()) {
                        NamedNodeMap nodeMap = elemNode.getAttributes();

                        for (int i = 0; i < nodeMap.getLength(); i++) {

                            Node node = nodeMap.item(i);
                            System.out.println("attr name : " + node.getNodeName());
                            System.out.println("attr value : "
                                    + node.getNodeValue());

                        }
                    }

                    for (String key : mapmainnode.keySet()) {
                        System.out.println("------------------------------------------------");
                        System.out.println("Iterating Parent Map foreach loop");
                        System.out.println("key: " + key + " value: " + mapmainnode.get(key));
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

                // get child node name and value
                //System.out.println("\nNode Name =" + elemNode.getNodeName() + " [OPEN]");
                //System.out.println("Node Content =" + elemNode.getTextContent());
                String NodeName = elemNode.getNodeName();
                String NodeContent = elemNode.getTextContent();
                //elements.add(SubNodeName);
                
                
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
