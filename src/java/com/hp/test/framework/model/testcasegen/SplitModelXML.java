/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.model.testcasegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author sayedmo
 */
public class SplitModelXML {

    NodeList nodeList1;

    StringBuffer strb = new StringBuffer();
    String xmlstring, xml, ModelName, AttributeName;
    int i = 0, j = 0, k=0;
    Node elemNode1, elemNode2;
    
    
    ReadProperties rp = new ReadProperties();
    static Logger log = Logger.getLogger(SplitModelXML.class.getName());
    public static Map<String,String> Modelxml_list=new HashMap<String,String>();

    public void ReadMasterLModelXML(String xmlFilepath) throws SAXException, ParserConfigurationException, IOException {
        int GID=0;
        File xmlFile = new File(xmlFilepath);
        //DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        String NodeName, MainNodeName = null;
        
        //try {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);
        //Normalize the XML Structure   
        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();

        root.normalize();
        NodeList nodeList = root.getChildNodes();

        NodeList combination = root.getElementsByTagName("combinations");
        NodeList ChilnodesOfCombination = combination.item(0).getChildNodes();
     
        
         Update_DM_MASTERMODELXML_REF udm=new Update_DM_MASTERMODELXML_REF();
         try
         {
          udm.openDatabaseConnection();
          GID=udm.getMasterModelGID(xmlFilepath);
          udm.closeconnection();
         }
         catch(ClassNotFoundException e){log.info("Exception in Opening Connection");}
         
        int model_xmls_count=0; 
        outerloop:for (int count = 0; count < ChilnodesOfCombination.getLength(); count++) {

            Node elemNode = ChilnodesOfCombination.item(count);
            if (elemNode.getNodeType() == Node.ELEMENT_NODE) {

                strb.append("<");
                strb.append(document.getDocumentElement().getNodeName());
                strb.append(">");

                MainNodeName = elemNode.getNodeName();

                nodeList1 = elemNode.getChildNodes();
                for (j = 0; j < nodeList1.getLength(); j++) {
                    //Node elemNode1 = nodeList1.item(j);
                    elemNode1 = nodeList1.item(j);
                    if (elemNode1.getNodeType() == Node.ELEMENT_NODE) {

                        NodeName = elemNode1.getNodeName();
                        //System.out.print("Node Name is from combination list:" + NodeName);
                        if(NodeName.equalsIgnoreCase("all")){
                            //NodeName="flow";
                             NodeList flow = root.getElementsByTagName("flow");
                             NodeList Childnodesofflow = flow.item(0).getChildNodes();
                            
                          //NodeName=root.getTagName();
                          //System.out.print("HiIIII"+NodeName);
                             for (k= 0; k < Childnodesofflow.getLength(); k++) {
                                //Node elemNode1 = nodeList1.item(j);
                                elemNode2 = Childnodesofflow.item(k);
                                if (elemNode2.getNodeType() == Node.ELEMENT_NODE) {
                                NodeName = elemNode2.getNodeName(); 
                                xmlstring = readnodeelement(NodeName, xmlFile);
                                strb = strb.append(xmlstring);
                        }
                        }
                             
                             strb.append("</");
                strb.append(document.getDocumentElement().getNodeName());
                strb.append(">");
            //    System.out.println("**Final XML***" + strb);
                xml = strb.toString();
          
                try {
                    //ModelName = "Product_" + document.getDocumentElement().getNodeName() + "_" + MainNodeName;
                 // System.out.println("File:" + xmlFile.getAbsoluteFile());
                    String filename = xmlFile.getName();
                    String[] fullname = filename.split("_");
                    String productname = fullname[0];
                //    System.out.println(fullname[0]);
                    String featurename = fullname[1];
                    ModelName=String.valueOf(GID)+","+ productname+ "_" +featurename+ "_" + MainNodeName;
                    //If modelxmllocation does not exists create it
                     CreateDirectory.createdir(rp.getProperty("XMLModelLocation"));
                     String abpath_temp=  rp.getProperty("XMLModelLocation") + "\\" + ModelName + ".xml";     
                 
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rp.getProperty("XMLModelLocation") + "\\" + ModelName + ".xml"),"UTF-8"));
                    writer.write(xml + System.getProperty("line.separator"));

                    writer.close();
                    Modelxml_list.put(abpath_temp, productname+ "_" +featurename+ "_" + MainNodeName);
                    model_xmls_count++;
                } catch (IOException e) {
                    System.out.println(e);
                }
                      break outerloop;  
                        }
                        xmlstring = readnodeelement(NodeName, xmlFile);
                        strb = strb.append(xmlstring);

                    }
                }
                strb.append("</");
                strb.append(document.getDocumentElement().getNodeName());
                strb.append(">");
            //    System.out.println("**Final XML***" + strb);
                xml = strb.toString();
                try {
                    //ModelName = "Product_" + document.getDocumentElement().getNodeName() + "_" + MainNodeName;
                 // System.out.println("File:" + xmlFile.getAbsoluteFile());
                    String filename = xmlFile.getName();
                    String[] fullname = filename.split("_");
                    String productname = fullname[0];
                //    System.out.println(fullname[0]);
                    String featurename = fullname[1];
                    ModelName=String.valueOf(GID)+","+ productname+ "_" +featurename+ "_" + MainNodeName;
                    
                    //If modelxmllocation does not exists create it
                     CreateDirectory.createdir(rp.getProperty("XMLModelLocation"));
                     String abpath_temp=  rp.getProperty("XMLModelLocation") + "\\" + ModelName + ".xml";     
                    //BufferedWriter writer = new BufferedWriter(new FileWriter(rp.getProperty("XMLModelLocation") + "\\" + ModelName + ".xml", false));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rp.getProperty("XMLModelLocation") + "\\" + ModelName + ".xml", false),"UTF-8"));
                     writer.write(xml + System.getProperty("line.separator"));

                    writer.close();
                    Modelxml_list.put(abpath_temp, productname+ "_" +featurename+ "_" + MainNodeName);
                    model_xmls_count++;
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            //Clear String buffer
            strb.setLength(0);
            xml = null;

        }
        log.info("Genarted  \""+model_xmls_count+ "\"  Combinations for the Master Model"+xmlFilepath);
      
        
//        }catch(IOException|SAXException|ParserConfigurationException e) {
//            System.out.println("File not found or SAX XML Exception"+e.getMessage());
//            //throw new SAXException("Error: " + e.getMessage()); 
//        }
    }

    //public static void main(String ar[]) throws XMLStreamException, FileNotFoundException {
    public String readnodeelement(String nodename, File file)  {
        StringWriter sw = new StringWriter();
        StringBuffer sb = new StringBuffer();
        XMLOutputFactory of = XMLOutputFactory.newInstance();
        XMLEventWriter xw = null;
        XMLInputFactory f = XMLInputFactory.newInstance();
        
        try {
        //XMLEventReader xr = f.createXMLEventReader(new FileInputStream(rp.getProperty("MasterXMLModelLocation")));
          XMLEventReader xr = f.createXMLEventReader(new FileInputStream(file));
        String value = null;

        while (xr.hasNext()) {
            XMLEvent e = xr.nextEvent();
                
            if (e.isStartElement()) {
                StartElement element = (StartElement) e;
                //System.out.println("Start Element: " + element.getName());
                
                Iterator iterator = element.getAttributes();
                while (iterator.hasNext()) {
                    Attribute attribute = (Attribute) iterator.next();
                    QName name = attribute.getName();
                    value = attribute.getValue();
                    value="\"" + value + "\"";
                    //System.out.println("Attribute name/value: " + name + "/" + value);
                }
            }

            if (e.isStartElement()
                    && ((StartElement) e).getName().getLocalPart().equals(nodename)) {
                xw = of.createXMLEventWriter(sw);
            } else if (e.isEndElement()
                    && ((EndElement) e).getName().getLocalPart().equals(nodename)) {
                break;
            } else if (xw != null) {
                xw.add(e);
            }
        }
       
        xw.close();
        sb.append("<" + nodename + " " +"type="+value+">" + sw + "</" + nodename + ">");
        }catch(FileNotFoundException|XMLStreamException e) {
            System.out.println("SAX XML Exception"+e.getMessage());
        }
        //System.out.println("<Document>"+sw+"</Document>");
        // System.out.println(sb);
        return sb.toString();

    }

} 
    
//    public static void main(String ar[]) throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
//        SplitModelXML rd = new SplitModelXML();
//        rd.ReadMasterLModelXML("C:\\WS_POC\\MASTERMODELXML\\DS_Search_Test.xml");
//    }
//}
