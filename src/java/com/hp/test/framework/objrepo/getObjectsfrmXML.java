/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.objrepo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yanamalp
 */
public class getObjectsfrmXML {

    public static Map<String, String> Locators = new HashMap<String, String>();

    public static Map<String, String> GetLocators(String Testcases_path) {
Locators.clear();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc;

            doc = documentBuilder.parse(new File(Testcases_path));

            getData(null, doc.getDocumentElement());

        } catch (Exception exe) {
            exe.printStackTrace();
        }
        Map<String, List<String>> actions_exp = new HashMap<String, List<String>>();

        return Locators;

    }

    public static void getData(Node parentNode, Node node) {

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE: {
                if (node.toString().contains("Verification")) {

                }
                if (node.hasChildNodes()) {
                    NodeList list = node.getChildNodes();
                    int size = list.getLength();

                    for (int index = 0; index < size; index++) {
                        getData(node, list.item(index));
                    }
                }

                break;
            }

            case Node.TEXT_NODE: {
                String data = node.getNodeValue();

                if (data.trim().length() > 0) {
                    Locators.put(parentNode.getNodeName(), node.getNodeValue());
                }
                break;
            }

        }
    }

}
