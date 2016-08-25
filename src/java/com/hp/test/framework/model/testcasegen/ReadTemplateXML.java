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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class ReadTemplateXML {

    public static Map<String, Set<String>> mapmainnode1 = new HashMap< String, Set<String>>();
    public static Map<String, String> mapsubnode1 = new HashMap< String, String>();
    //Map<String, List<String>> mapsubnode = new HashMap< String, List<String>>();
    Set<String> ParentContent1 = new HashSet<String>();
    Set<String> SplitDBValue = new HashSet<String>();
    Set<String> DBParam = new HashSet<String>();

    Connection connection = null;
    Statement statement;
    PreparedStatement prestatement;
    ConnectDB c = new ConnectDB();

    static Logger log = Logger.getLogger(ReadTemplateXML.class.getName());

    public void ReadTemplateXMLMethod(String xmlFilepath) {
        try {

            File xmlFile = new File(xmlFilepath);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = documentBuilder.parse(xmlFile);
            Element root = document.getDocumentElement();

            root.normalize();
            NodeList nodeList = root.getChildNodes();
            //System.out.println("Root name "+ nodeList);
     //       System.out.println("Root element :" + document.getDocumentElement().getNodeName());

       //     System.out.println("===============================");

            if (root.hasChildNodes()) {

                //printNodeList(document.getChildNodes());
                printNodeList(nodeList);

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void printNodeList(NodeList nodeList) {
        List<Element> elements = new ArrayList<>();

        for (int count = 0; count < nodeList.getLength(); count++) {
            Node elemNode = nodeList.item(count);
            if (elemNode.getNodeType() == Node.ELEMENT_NODE) {
                elements.add((Element) elemNode);

                // get node name and value
                //System.out.println("\nNode Name =" + elemNode.getNodeName() + " [OPEN]");
                //System.out.println("Node Content =" + elemNode.getTextContent());
                //System.out.println("Node Value =" + elemNode.getNodeValue());
                String NodeName = elemNode.getNodeName();
                String NodeContent = elemNode.getTextContent();

                if (mapmainnode1.containsKey(NodeName)) {
                    //List<String> lastSavedParameters = mapmainnode.get(NodeName);
                    Set<String> lastSavedParameters = mapmainnode1.get(NodeName);
                    //ParentContent.addAll(lastSavedParameters);
                } else {
                    ParentContent1 = new HashSet<String>();
                }
                ParentContent1.add(NodeContent);
                mapmainnode1.put(NodeName, ParentContent1);

                if (elemNode.hasAttributes()) {
                    NamedNodeMap nodeMap = elemNode.getAttributes();

                    for (int i = 0; i < nodeMap.getLength(); i++) {

                        Node node = nodeMap.item(i);
                    //    System.out.println("attr name : " + node.getNodeName());
                      //  System.out.println("attr value : "
                       //         + node.getNodeValue());

                    }

                }

                if (elemNode.hasChildNodes()) {

                    //recursive call if the node has child nodes
                    printNodeList(elemNode.getChildNodes());

                    // get node name and value
//                System.out.println("\nNode Name =" + elemNode.getNodeName() + " [OPEN]");
//                System.out.println("Node Content =" + elemNode.getTextContent());
                    //System.out.println("Node Value =" + elemNode.getNodeValue());
                    String SubNodeName = elemNode.getNodeName();
                    String SubNodeContent = elemNode.getTextContent();

                    mapsubnode1.put(SubNodeName, SubNodeContent);

                }

                //System.out.println("Node Name =" + elemNode.getNodeName()
                // + " [CLOSE]");
                for (String key : mapmainnode1.keySet()) {
                  //  System.out.println("------------------------------------------------");
                   // System.out.println("Test Case Iterating Parent Map foreach loop");
                    //System.out.println("key: " + key + " value: " + mapmainnode1.get(key));
                    String Key = key;
                    String Value = mapmainnode1.get(key).toString();
                    Value = Value.replaceAll("\\[", "").replaceAll("\\]", "");
                    
                    String pattern = "DB";
                    //if (Value.contains("DB")) {
		    if (Value.startsWith("DB.")) {	

                        String[] SplitDBValue1 = Value.split("\\.");
                        String TABLE, COLUMN;
                        TABLE = SplitDBValue1[1];
                        COLUMN = SplitDBValue1[2];
//                            for (String SplitDBValue11 : SplitDBValue1) {
//                                System.out.println(SplitDBValue11);
//                                System.out.println("Hi I am Here inside");
//                            }
                     //   System.out.println("Table and Column Name:" + SplitDBValue1[1] + SplitDBValue1[2]);

                        try {
                            String query = "select " + COLUMN + " from " + TABLE + " where " + COLUMN + " IS NOT NULL AND " + COLUMN + " <> ''";
                            System.out.println(query);
                            connection = c.ConnectToolDB();

                            PreparedStatement pst = connection.prepareStatement(query);
                            ResultSet rs = pst.executeQuery();
                            DBParam = new HashSet<String>();
                            //rs.next();
                            while (rs.next()) {
                                String querydata1 = rs.getString(COLUMN);

                                DBParam.add(querydata1);
                                mapmainnode1.put(Key, DBParam);
                            }

                            pst.close();
                            rs.close();
                        } catch (SQLException e) {
                            log.error("Issue in getting value for Database Column"+e.getMessage());
                        }

                    }

                }

            }

        }

    }

}
