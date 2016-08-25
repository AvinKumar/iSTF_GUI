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
import static com.hp.test.framework.model.testcasegen.GenerateCombinations.combinations;
import static com.hp.test.framework.model.testcasegen.ReadTemplateXML.mapmainnode1;
import static com.hp.test.framework.model.testcasegen.ReadXMLFileUsingDom.mapmainnode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;


public class TestCaseGenerator {

    public static ArrayList<String> ModelXMLList = new ArrayList<String>();
    public static ArrayList<String> TemplateXMLList = new ArrayList<String>();
    public String FileCount = null;

    static Logger log = Logger.getLogger(TestCaseGenerator.class.getName());
    //public static ArrayList<String> Template = new ArrayList<String>();
    String ModelXMLFile, Template, TemplatePath, ModelXMLName, TemplateXMLFile, TemplateXMLName, TestCase;
    String[] TemplateXMLName1, ModelXMLName1;
    String modelgid, getmodelgid, templategid, gettemplategid, modelgidtestcase, getmodelgidtestcase, modifyvalue, lastdmtestcasegid;
    int Random, lastgidDMT;

    Connection connection = null;
    Statement statement = null;
    PreparedStatement prestatement = null;

    public static int counter = 0;
    public static Map<String, String> template_list = new HashMap<String, String>();

    /**
     * @param ar
     * @throws java.lang.Exception
     */
    public void CreateTemplatefromModel(String modelxml_filename, int model_gid) throws SQLException {
        ReadXMLFileUsingDom ReadModelXML = new ReadXMLFileUsingDom();

        ReadProperties rp = new ReadProperties();
        int batch_inc = 0;
        File f = new File(modelxml_filename);

        ModelXMLFile = modelxml_filename;
        ModelXMLName = f.getName();
        f = null;
        ModelXMLName1 = ModelXMLName.split("\\.");
        System.out.println(ModelXMLName + ModelXMLName1);
        ModelXMLFile = rp.getProperty("XMLModelLocation") + "\\" + ModelXMLName;
        log.info("Started generating TestScenarios from Model XML file(s) ::> " + ModelXMLFile);

        ReadModelXML.ReadXMLModel(ModelXMLFile);

        List<Map<String, String>> list = new LinkedList<Map<String, String>>();
        combinations(mapmainnode, list);

        int listsize = list.size();
        int i = 0;

        getmodelgid = String.valueOf(model_gid);
        for (Map<String, String> combination : list) {

            //String Template;
            String xml = MaptoXML.toXML(combination, "root");
            
            //   log.info("Print XML String"+xml);
            //System.out.println( combination );
            Template = getmodelgid + "_" + ModelXMLName1[0] + "Template" + i;
            //log.info("template path"+Template);
            //If modelxmllocation does not exists create it
            //Create Store path if not exists
            CreateDirectory.createdir(rp.getProperty("StorePath"));
            TemplatePath = rp.getProperty("StorePath") + "\\" + Template + ".xml";
            template_list.put(TemplatePath, getmodelgid);
            try {

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rp.getProperty("StorePath") + "\\" + Template + ".xml"), "UTF-8"));
                writer.write(xml + System.getProperty("line.separator"));

                writer.close();

            } catch (IOException e) {
                System.out.println(e);
            }
            i = i + 1;
        }

        mapmainnode.clear();
        list.clear();
        // log.info("mapmainvode and list cleared");

        connection = ConnectDB.ConnectToolDB();
        connection.setAutoCommit(false);
        String query = "INSERT INTO DM_SCENARIO_REF(TEMPLATE_PATH, TEMPLATE_EXTENSION, TEMPLATE_NAME,MODEL_GID_REF,XML_FILE) values (?,?,?,?,?)";
        prestatement = connection.prepareStatement(query);
        batch_inc = 0;
        ConvertFiletoString convertfiletostring = new ConvertFiletoString();
        for (String temp_path : template_list.keySet()) {
            i++;
            try {

                File temp_file = new File(temp_path);
                String xmlString = convertfiletostring.convertFiletoString(temp_path);
                //Unescape XML special characters
                xmlString=StringEscapeUtils.unescapeXml(xmlString);
                prestatement.setString(1, temp_path);
                prestatement.setString(2, ".xml");
                prestatement.setString(3, temp_file.getName());
                temp_file = null;
                prestatement.setString(4, template_list.get(temp_path));
                prestatement.setString(5, xmlString);

                prestatement.addBatch();

            } catch (SQLException e) {

                log.error("Exception Caught*******" + e.getStackTrace());

            }
            batch_inc = batch_inc + 1;
            if (batch_inc == 200) {
                prestatement.executeBatch();
                connection.commit();
                log.debug("Saved a batch of Queries");
                connection.setAutoCommit(false);
                batch_inc = 0;
            }

        }

        prestatement.executeBatch();
        connection.commit();
        prestatement.clearBatch();
        log.info("Saved a batch of Queries");
        connection.setAutoCommit(false);
        if (prestatement != null) {
            prestatement.close();
        }
        log.info("Completed generating TestScenarios for the Model Xml file" + modelxml_filename + "  Generated " + template_list.size() + " TestScenarios");
        template_list.clear();
        connection.close();
    }
    // }

    public void CreateTestCasesFromTemplate(String Scenario_file, int scenario_gid) throws SQLException {
        ReadTemplateXML ReadTemplate = new ReadTemplateXML();

        ReadProperties rp = new ReadProperties();
        int batch_inc1 = 0;

        File f = new File(Scenario_file);

        ModelXMLFile = Scenario_file;
        TemplateXMLName = f.getName();
        f = null;
        TemplateXMLFile = Scenario_file;

        TemplateXMLName1 = TemplateXMLName.split("\\.");
        TemplateXMLFile = rp.getProperty("StorePath") + "\\" + TemplateXMLName;
        log.info("started parsing file" + TemplateXMLFile);
        ReadTemplate.ReadTemplateXMLMethod(TemplateXMLFile);

        List<Map<String, String>> list1 = new LinkedList<Map<String, String>>();
        combinations(mapmainnode1, list1);

        connection = ConnectDB.ConnectToolDB();
        connection.setAutoCommit(false);
        String query = "INSERT INTO DM_TESTCASE(TESTCASE, TEMPLATE_GID, MODEL_GID, BVT, REGRESSION) values (?,?,?,?,?)";
        prestatement = connection.prepareStatement(query);
        gettemplategid = String.valueOf(scenario_gid);
        lastgidDMT = Integer.parseInt(getLastgidFromDMtestCase());
        for (Map<String, String> combination : list1) {

            for (String key : combination.keySet()) {
                Random = 10 + (int) (Math.random() * (1000000));

                if (key.startsWith("iSTFAutoGen")) {
                    modifyvalue = combination.get(key);
                    modifyvalue = modifyvalue + Random;
                    combination.put(key, modifyvalue);
                } else if (key.startsWith("iSTFAutoGid")) {
                    modifyvalue = combination.get(key);
                    lastgidDMT = lastgidDMT + 1;
                    modifyvalue = modifyvalue + lastgidDMT;
                    combination.put(key, modifyvalue);
                }

            }
            //lastgidDMT++;
            Map<String, String> sortedcombination = new TreeMap<>(combination);
            TestCase = MaptoXML.toXML(sortedcombination, "root");
             //UNescape XML special characters
            //TestCase=StringEscapeUtils.unescapeXml(TestCase);
            //TestCase = MaptoXML.toXML(combination, "root");
            try {

                prestatement.setString(1, TestCase);
                prestatement.setString(2, gettemplategid);
                prestatement.setString(3, getmodelgidtestcase);
                prestatement.setString(4, "yes");
                prestatement.setString(5, "yes");

                if (!nameExists(TestCase)) {
                    prestatement.addBatch();
                }
            } catch (SQLException e) {

                log.info("Test Cases Exception Caught" + e.getMessage());

            }
            batch_inc1 = batch_inc1 + 1;
            if (batch_inc1 == 1000) {
                prestatement.executeBatch();
                connection.commit();
                connection.setAutoCommit(false);
                batch_inc1 = 0;
            }

        }

        prestatement.executeBatch();
        connection.commit();

        connection.setAutoCommit(false);
        if (prestatement != null) {
            prestatement.close();
        }
        mapmainnode1.clear();
        log.info("Generated " + list1.size() + " TestCases for the Template====>" + Scenario_file);
        list1.clear();

        //  log.info("mapmainnode1 and list1 cleared");
    }

    //    updateModelStatus(ModelXMLName);
    public void updateTemplateTable() {
        connection = ConnectDB.ConnectToolDB();

        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            //statement = connection.createStatement();
            String query = "INSERT INTO DM_SCENARIO_REF(TEMPLATE_PATH, TEMPLATE_EXTENSION, TEMPLATE_NAME,MODEL_GID_REF) values ('" + TemplatePath + "','.xml','" + Template + "',(select GID from DM_MODELXML_REF where MODEL_NAME='" + ModelXMLName + "'))";
            System.out.println(query);
            System.out.print("***Inserted the Records***");
            //prestatement = connection.prepareStatement(query);
            statement.executeUpdate(query);
            if (prestatement != null) {
                prestatement.close();
            }
        } catch (SQLException e) {

            System.out.println("Exception Caught*******" + e.getCause());

        }

    }

    public void updateQueryTable() {
        connection = ConnectDB.ConnectToolDB();

        ResultSet rs = null;
        try {
            //connection.setAutoCommit(false);
            statement = connection.createStatement();
            //statement = connection.createStatement();

            String query = "INSERT INTO DM_TESTCASE(TESTCASE, TEMPLATE_GID, MODEL_GID) values ('" + TestCase + "',(select GID from DM_SCENARIO_REF where TEMPLATE_NAME='" + TemplateXMLName1[0] + "'),(select MODEL_GID_REF from DM_SCENARIO_REF where TEMPLATE_NAME ='" + TemplateXMLName1[0] + "'))";
            System.out.println(query);
            System.out.print("***Inserted the Records***");
            //prestatement = connection.prepareStatement(query);
            //prestatement.addBatch();
            statement.executeUpdate(query);
            if (prestatement != null) {
                prestatement.close();
            }
        } catch (SQLException e) {

            log.error("Exception Caught*******" + e.getCause());

        }

    }

    void purgeDirectory(File dir) {
//    for (File file: dir.listFiles()) {
//        if (file.isDirectory()) purgeDirectory(file);
//        file.delete();
        try {
            FileUtils.cleanDirectory(dir);
            log.info("Cleared the template directory");
        } catch (IOException e) {
            log.error("Clean directory issue" + e.fillInStackTrace());
        }
    }

    public String getModelgid() throws SQLException {
        //connection = ConnectDB.ConnectToolDB();

        ResultSet rs = null;
        try {
            //connection.setAutoCommit(false);
            statement = connection.createStatement();
            //statement = connection.createStatement();

            String query = "select GID from DM_MODELXML_REF where MODEL_NAME='" + ModelXMLName + "'";
            log.info(query);

            //prestatement = connection.prepareStatement(query);
            //prestatement.addBatch();
            PreparedStatement pst = connection.prepareStatement(query);
            rs = pst.executeQuery();
            rs.next();
            modelgid = rs.getString("GID");
            log.info("***Get Model GID the Records***" + modelgid);
            //connection.commit();
//            if (prestatement != null) {
//                prestatement.close();
//            }
            rs.close();
        } catch (SQLException e) {

            log.error("Exception Caught while query for model id*******" + e.getCause());

        }

        return modelgid;

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
            log.info("***Get Template GID the Records***" + templategid);
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
            log.info("***Get Model GID for testcase***" + modelgidtestcase);
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

    public boolean nameExists(String name) throws SQLException {
        long count = 0;
        boolean check = true;
        //String Query="select count(*) from DM_TESTCASE where TESTCASE='"+name+"'";
        //Escape sql special characters
        name=StringEscapeUtils.escapeSql(name); 
        String Query = "select * from DM_TESTCASE where TESTCASE='" + name + "'";
        // long count1 = DatabaseUtils.queryNumEntries(db,"MyTable", "name = ?", new String[] { name });
        ResultSet rs = null;
        try {
            
            PreparedStatement pst = connection.prepareStatement(Query);
            rs = pst.executeQuery();
            while (rs.next()) {
                //count=rs.getInt("TESTCASE");
                count++;
//         count = Integer.parseInt(rs.getString("count(*)"));
//            System.out.println(Integer.parseInt(rs.getString("count(*)")));
            }
            if (count > 0) {
                check = true;
            } else {
                check = false;
            }

        } catch (SQLException e) {
            System.out.println("Exception while retrieving count" + e.getMessage());
        }
        finally {
            if (rs != null) {
                rs.close();
            }
        }
  
        return check;
    }

    public boolean StatusCompleted(String name) {

        boolean check = true;
        //String Query="select count(*) from DM_TESTCASE where TESTCASE='"+name+"'";
        String Query = "select STATUS from DM_MODELXML_REF where MODEL_NAME='" + name + "'";
        // long count1 = DatabaseUtils.queryNumEntries(db,"MyTable", "name = ?", new String[] { name });
        try {
            connection = ConnectDB.ConnectToolDB();

            ResultSet rs = null;
            PreparedStatement pst = connection.prepareStatement(Query);
            rs = pst.executeQuery();
            rs.next();
            String status = rs.getString("STATUS");
            System.out.println("print value of" + status);
//      if  (status.contains("COMPLETED")){
            if (status != null && !status.isEmpty()) {
                check = true;
            } else {
                check = false;
            }

            // connection.commit();
            if (prestatement != null) {
                prestatement.close();
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Exception while checking whther the status is completed" + e.getMessage());
        }
        return check;
    }

    public void updateModelStatus(int GID) {
        connection = ConnectDB.ConnectToolDB();

        ResultSet rs = null;
        try {
            //connection.setAutoCommit(false);
            statement = connection.createStatement();
            //statement = connection.createStatement();

            String query = "update DM_MASTERMODELXML_REF set TESTCASE_GEN_STATUS='COMPLETED' where GID=" + GID;
            System.out.println(query);
            System.out.print("successfully updated the status in DM_MODELXML_REF table");
            statement.executeUpdate(query);
            if (prestatement != null) {
                prestatement.close();
            }
        } catch (SQLException e) {

            log.error("Exception Caught while updating status in DM_MODELXML_REF table" + e.getCause());

        }

    }

    public String getLastgidFromDMtestCase() throws SQLException {

        ResultSet rs = null;
        final String query = "select GID from DM_TESTCASE order by GID desc limit 1";
        log.info(query);
        try (final PreparedStatement pst = connection.prepareStatement(query)) {
            rs = pst.executeQuery();
            lastdmtestcasegid = "0";
            if (rs.next()) {
                lastdmtestcasegid = rs.getString("GID");
            }
            //if (lastdmtestcasegid.isEmpty()) {

            log.info("***Get Latest GID from the DM_TESTCASE Table***" + lastdmtestcasegid);
        } catch (SQLException e) {

            log.error("Exception Caught while querying for LatestGID from DM_TESTCASE", e);

        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return lastdmtestcasegid;

    }
}
