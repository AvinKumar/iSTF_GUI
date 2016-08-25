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


//import static XMLDOM.GenerateCombinations.combinations;
//import XMLDOM.MaptoXML;
//import XMLDOM.ReadTemplateXML;
//import XMLDOM.ReadXMLTemplatewithoutDB;
//import static XMLDOM.ReadXMLTemplatewithoutDB.mapmainnode;
import static com.hp.test.framework.model.testcasegen.GenerateCombinations.combinations;
import static com.hp.test.framework.model.testcasegen.ReadXMLFileUsingDom.mapmainnode;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;


public class TestCaseGeneratorwithoutDB {

    public static ArrayList<String> ModelXMLList = new ArrayList<String>();
    public static ArrayList<String> TemplateXMLList = new ArrayList<String>();
    public String FileCount = null;

    static Logger log = Logger.getLogger(TestCaseGeneratorwithoutDB.class.getName());
    //public static ArrayList<String> Template = new ArrayList<String>();
    String ModelXMLFile, Template, TemplatePath, ModelXMLName, TemplateXMLFile, TemplateXMLName, TestCase;
    String[] TemplateXMLName1, ModelXMLName1;
    String modelgid, getmodelgid, templategid, gettemplategid, modelgidtestcase, getmodelgidtestcase;
    
    Connection connection = null;
    Statement statement = null;
    PreparedStatement prestatement = null;
    ConnectDB c = new ConnectDB();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

//        ReadXMLModelLocation xmlmodel = new ReadXMLModelLocation();
//        xmlmodel.ReadXMLMODELPath();
        
//        ReadXMLTemplatewithoutDB readxmlwithoutdb=new ReadXMLTemplatewithoutDB();
//        readxmlwithoutdb.ReadXMLModel("C:\\Testtemp\\Supervisor_Search_Metadata1.xml");
        
        TestCaseGeneratorwithoutDB t = new TestCaseGeneratorwithoutDB();
        //t.CreateTemplatefromModel();
        t.CreateTestCasesFromTemplate();
        //updateresults u = new updateresults();
        //u.UpdateFinalResults();
        
        //Clear the template directory every time at the end
//        ReadProperties rp = new ReadProperties();
//        File directory = new File(rp.getProperty("StorePath") + "\\");
//        t.purgeDirectory(directory);
    }

   
    public void CreateTestCasesFromTemplate() throws SQLException {
        ReadTemplateXML ReadTemplate = new ReadTemplateXML();
        ReadXMLTemplatewithoutDB readxmlwithoutdb=new ReadXMLTemplatewithoutDB();
        ReadProperties rp = new ReadProperties();     
        int batch_inc1=0;
        
        log.info("Started Listing Template XM files   " + rp.getProperty("StorePath"));
        File directory = new File(rp.getProperty("StorePath") + "\\");
        int subfile = 0;
        
        
        try {
            File[] fList = directory.listFiles();
            for (File file : fList) {
                if (file.isFile()) {
                    if (file.getName().contains("xml")) {
                        TemplateXMLList.add(file.getName());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Issue in Listing files" + e.getStackTrace());
        }
        log.info("End of Listing XML template files ");
        for (String modelxml_filename : TemplateXMLList) {
            TemplateXMLFile = modelxml_filename;
            TemplateXMLName = modelxml_filename;
            TemplateXMLName1 = TemplateXMLName.split("\\.");
            TemplateXMLFile = rp.getProperty("StorePath") + "\\" + modelxml_filename;
            log.info("started parsing file" + TemplateXMLFile);
            
            //ReadTemplate.ReadTemplateXMLMethod(TemplateXMLFile);
            readxmlwithoutdb.ReadXMLModel(TemplateXMLFile);
            List<Map<String, String>> list1 = new LinkedList<Map<String, String>>();
            //combinations(mapmainnode1, list1);
            combinations(mapmainnode, list1);
            
            int listsize=list1.size();
            log.info("List map Size for mapmainnode1:=" +listsize);
            
            int i = 0;
            connection = ConnectDB.ConnectToolDB();
            connection.setAutoCommit(false);
            String query = "INSERT INTO DM_TESTCASE(TESTCASE, TEMPLATE_GID, MODEL_GID) values (?,?,?)";
            prestatement=connection.prepareStatement(query);
            gettemplategid=getTemplategid();
            getmodelgidtestcase=getModelgidtestcase();
            for (Map<String, String> combination : list1) {

                //String Template;
                TestCase = MaptoXML.toXML(combination, "root");
                log.info(TestCase);
                //System.out.println( combination );

                //updateQueryTable();
                //list1.clear();
                try {
                     
                    //String query = "INSERT INTO DM_TEMPLATE_REF(TEMPLATE_PATH, TEMPLATE_EXTENSION, TEMPLATE_NAME,MODEL_GID_REF) values ('" + TemplatePath + "','.xml','" + Template + "',(select GID from DM_MODELXML_REF where MODEL_NAME='" + ModelXMLName + "'))";
                    
                     
                    
                    prestatement.setString(1, TestCase);
                    prestatement.setString(2, gettemplategid);
                    prestatement.setString(3, getmodelgidtestcase);
                    //prestatement.setString(4, getmodelgid);
                    
                    prestatement.addBatch();
                    
                    }catch (SQLException e) {

                    log.info("Test Cases Exception Caught*******" + e.getStackTrace());

        
                }
                batch_inc1=batch_inc1+1;
                if(batch_inc1==1000){
                   prestatement.executeBatch(); 
                   connection.commit();
                   log.info("Saved a Batch of Queries");
                   connection.setAutoCommit(false);
                   batch_inc1=0;
                }
                
            }
              prestatement.executeBatch();
             connection.commit();
             log.info("Saved a Batch of Queries");
             connection.setAutoCommit(false);
              if (prestatement != null) {
                prestatement.close();
              }
            mapmainnode.clear();
            list1.clear();
            log.info("mapmainnode and list1 cleared");
        }
    }

   
    
      
       public String getTemplategid() throws SQLException {
        //connection = ConnectDB.ConnectToolDB();
        
        
        ResultSet rs = null;
        try {
            //connection.setAutoCommit(false);
            statement = connection.createStatement();
            //statement = connection.createStatement();

            String query = "select GID from DM_SCENARIO_REF where TEMPLATE_NAME='" + TemplateXMLName1[0] + "'";
            log.info(query);
            
            //prestatement = connection.prepareStatement(query);
            //prestatement.addBatch();
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
            rs.next();
            templategid = rs.getString("GID");
            log.info("***Get Template GID the Records***"+templategid);
            //connection.commit();
//            if (prestatement != null) {
//                prestatement.close();
//            }
           rs.close();
        } catch (SQLException e) {

            log.error("Exception Caught while querying for template ID*******" + e.getCause());

        }
        
        return templategid;
        
    }
                
        public String getModelgidtestcase() throws SQLException {
        //connection = ConnectDB.ConnectToolDB();
        
        
        ResultSet rs = null;
        try {
            //connection.setAutoCommit(false);
            statement = connection.createStatement();
            //statement = connection.createStatement();

            String query = "select MODEL_GID_REF from DM_SCENARIO_REF where TEMPLATE_NAME ='" + TemplateXMLName1[0] + "'";
            log.info(query);
            
            //prestatement = connection.prepareStatement(query);
            //prestatement.addBatch();
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
            rs.next();
            modelgidtestcase = rs.getString("MODEL_GID_REF");
            log.info("***Get Model GID for testcase***"+modelgidtestcase);
            //connection.commit();
//            if (prestatement != null) {
//                prestatement.close();
//            }
           rs.close();
        } catch (SQLException e) {

            log.error("Exception Caught query for model id for test cases*******" + e.getCause());

        }
        
        return modelgidtestcase;
        
    }
}
