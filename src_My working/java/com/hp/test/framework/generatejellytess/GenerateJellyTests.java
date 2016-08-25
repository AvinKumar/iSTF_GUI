/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.generatejellytess;

import com.hp.test.framework.DBUtils.DatabaseUtils;
import static com.hp.test.framework.generatejellytess.CreateReadableTestCase.fw;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileDeleteStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author yanamalp
 */
public class GenerateJellyTests {

    public static Map<String, String> Locators = new HashMap<String, String>();
    // public static   ArrayList<String> Verifications=new ArrayList<String>();
    public static boolean verfiy = false;
    public static int inc = 0;
    public static ModelProperties mp = new ModelProperties();
    public static Connection connection = null;
    public static String alm_test_location = "";
    public static String Jelly_Tests_location = "";
    public static String temp_jelly_Tests_location = "";
    public static String CLEAN_JELLY_TESTS = "";
    public static String glb_Feature_Name = "";
static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GenerateJellyTests.class.getName());
    public static void exemappingmodel() {
        boolean clean_files = false;

        Map<Integer, String> Master_ModelList = new HashMap<Integer, String>();

        try {
            Class.forName("org.sqlite.JDBC");

            log.info("TestCase DB Location" + mp.getProperty("MODEL_DB_LOCATION"));
            alm_test_location = mp.getProperty("ALM_FORMAT_STORE_LOC");
            Jelly_Tests_location = mp.getProperty("JELLY_TESTS_LOCATION");
            CLEAN_JELLY_TESTS = mp.getProperty("CLEAN_JELLY_TESTS");

            if (CLEAN_JELLY_TESTS.toLowerCase().equals("yes")) {
                clean_files = true;
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + mp.getProperty("MODEL_DB_LOCATION"));
            //  int GID_int = Integer.parseInt(GID);

            Statement Master_statement = connection.createStatement();
            ResultSet rs_master = Master_statement.executeQuery("SELECT GID,FEATURENAME FROM DM_MASTERMODELXML_REF WHERE TESTCASE_GEN_STATUS LIKE 'COMPLETED' AND JELLYCASE_GEN_STATUS IS NULL  ORDER BY GID");

            while (rs_master.next()) {
                Master_ModelList.put(rs_master.getInt("GID"), rs_master.getString("FEATURENAME"));

            }
            rs_master = null;
            Master_statement = null;
           // connection.close();
        } catch (SQLException e) {

           log.error("Error in getting data from DM_MASTERMODELXML_REF table to generate Jelly and ALM TESTS");

        } catch (ClassNotFoundException e) {
           log.error("Exception in exemapping model function" + e.getMessage());
        }

        if (Master_ModelList.isEmpty()) {
            log.info("********************************************************************************************************************************************");
            log.info("Jelly Testcases are already generated for all the Models; Please check the column status \"JELLYCASE_GEN_STATUS\" in \"DM_MASTERMODELXML_REF\"");
            log.info("********************************************************************************************************************************************");
            throw new RuntimeException("Runtime Exception");
          //  System.exit(0);
        }

        for (int key : Master_ModelList.keySet()) {
            GenerateJellyTests.CreateJellyTestsFolder(Jelly_Tests_location, clean_files, Master_ModelList.get(key));
            temp_jelly_Tests_location = Jelly_Tests_location + Master_ModelList.get(key);
            glb_Feature_Name = Master_ModelList.get(key);
            log.info("Started generating Jelly Tests for the Feature::" + glb_Feature_Name);
            log.info("***************************************************");
            genjellyTests(String.valueOf(key), Master_ModelList.get(key));
            try {
                DatabaseUtils.UpdateSingleValue("DM_MASTERMODELXML_REF", "COMPLETED", "JELLYCASE_GEN_STATUS", "GID", String.valueOf(key));
            } catch (ClassNotFoundException e) {
              log.error("Exception in updating DM_MASTERMODELXML_REF table" + e.getMessage());
            }
            log.info("End of generating Jelly Tests for the Feature::" + glb_Feature_Name);

        }

    }

    public static void genjellyTests(String Parent_model_gid, String Feature_name) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String sql_query = "SELECT DR.GID as MODEL_GID,DSR.GID AS SCENARIO_GID,DT.GID AS TEST_GID,DR.MODEL_PATH,DT.TESTCASE,DT.RESULTS  FROM DM_TESTCASE DT, DM_MODELXML_REF DR, DM_SCENARIO_REF DSR where DT.TEMPLATE_GID=DSR.GID and DSR.MODEL_GID_REF=DR.GID AND DR.MASTER_GID=" + Integer.parseInt(Parent_model_gid) + " ORDER BY DR.GID";
            ResultSet rs = statement.executeQuery(sql_query);

            String GID_temp = "";
            String Test_case = "";
            String Model_xml_path = "";
            int results = 0;
            CreateReadableTestCase.fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(alm_test_location + Feature_name + ".csv"), "UTF-8"));
            CreateReadableTestCase.fw.write("TestCaseNumber,StepName,Step Description,ExpectedResults\n");
            while (rs.next()) {
                GID_temp = Parent_model_gid + "_" + rs.getString("MODEL_GID") + "_" + rs.getString("SCENARIO_GID") + "_" + rs.getString("TEST_GID"); // rs.getInt("GID");
                log.info("Generating Jelly Testcase for  MasterModel GID::" + Parent_model_gid + " MODEL XML GID::" + rs.getString("MODEL_GID") + " TEST SCENARIO GID::" + rs.getString("SCENARIO_GID") + " TESTCASE GID::" + rs.getString("TEST_GID"));
                Test_case = rs.getString("TESTCASE");
                results = rs.getInt("RESULTS");
                Model_xml_path = rs.getString("MODEL_PATH");

                maptestcasesapi(GID_temp, Test_case, results, Model_xml_path);
                // break;
            }
            CreateReadableTestCase.fw.close();
            CreateReadableTestCase.fw = null;
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            if (statement != null) {
                statement.close();
            }

        } catch (SQLException e) {

            log.error("Error in getting records from DM_TESTCASE to Execute API or to generate Jelly tests");

        } catch (IOException e) {
            log.error("exception in IO" + e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error("" + e.getMessage());
        }

    }

    public static void maptestcasesapi(String GID, String Testcase, int expresults, String Model_xml_path) throws IOException, ClassNotFoundException {
        Map<String, String> Locators1;
        Map<String, Map<String, String>> mapping_list = new HashMap<>();
        BufferedWriter fw;
        File f = null;
        f = File.createTempFile("tmp", ".xml", new File(mp.getProperty("TEMP_LOCATION")));

//        String update_exp_results_temp = mp.getProperty("UPDATE_RESULTS");
//
//        boolean update_exp_results = false;
//        if (update_exp_results_temp.equals("yes")) {
//            update_exp_results = true;
//
//        }
        fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        fw.write(Testcase);
        fw.close();

        String path = f.getAbsolutePath();
        Locators.clear();
        Locators1 = GetLocators(path);
        // f.delete();
        for (String key : Locators1.keySet()) {
            //  System.out.println("key" + key);
            String temp_ar[] = key.split("_");
            String parent = temp_ar[0];
            String child = temp_ar[1];
            String UI_object = temp_ar[2];

            if (!mapping_list.containsKey(parent)) {
                // System.out.println("parent"+parent);
                Map<String, String> innerMap = mapping_list.get(key);
                if (innerMap == null) {
                    mapping_list.put(parent, innerMap = new HashMap<>()); // Java version >= 1.7
                }
                innerMap.put(child + ":" + UI_object, Locators1.get(key));

                mapping_list.put(parent, innerMap);
            } else {
                Map<String, String> innerMap = mapping_list.get(parent);
                innerMap.put(child + ":" + UI_object, Locators1.get(key));
                mapping_list.put(parent, innerMap);
                //  mapping_list.put(parent, mapping_list.get(parent)+ "^"+child +":"+ UI_object + ":"+ Locators1.get(key));
            }
        }

        Locators.clear();
        //Map<String, String> mapping_api_list = GetLocators(mp.getProperty("UI_API_MAPPING_XML_PATH"));
        // Generating jelly scripts
//        String generate_jelly_tests = mp.getProperty("GENERATE_JELLY_TESTS").toLowerCase();

        //   ArrayList order_list = GetRootnodes.GetrootLocators(mp.getProperty("MODEL_XML_PATH"));---Remove
        ArrayList order_list = GetRootnodes.GetrootLocators(Model_xml_path);

        try {

            String jellyFile = temp_jelly_Tests_location + "\\" + GID + "_" + glb_Feature_Name + ".xml";// mp.getProperty("JELLY_TESTS_LOCATION") + "Jelly_" + model_name + "_GID" + GID;
            String[] temp_ar=GID.split("_");
            int TestCase_GID=Integer.valueOf(temp_ar[3]);
            CreateJellyTestCase.createUITests(TestCase_GID,order_list, mapping_list, jellyFile, mp.getProperty("OBJECT_REPOSITORY_XML_PATH"));
            CreateReadableTestCase.createRedableTests(order_list, mapping_list, mp.getProperty("OBJECT_REPOSITORY_XML_PATH"));

        } catch (SQLException e) {
            log.error("Exception in updating Expected Results" + e.getMessage());
        }

        //**** end of Generating Jelly script file
    }

    public static Map<String, String> GetLocators(String Testcases_path) {
        //   ReadTestCases.verfiy=verification;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc;

            // deletes file when the virtual machine terminate
            doc = documentBuilder.parse(new File(Testcases_path));
            //   doc = documentBuilder.parse(f);
            getData(null, doc.getDocumentElement());
            //   System.out.println("purush");

        } catch (Exception exe) {
           log.error("Error in getting locators" + exe.getMessage());
        }
        Map<String, List<String>> actions_exp = new HashMap<String, List<String>>();

        return Locators;

    }

    public static void getData(Node parentNode, Node node) {

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE: {
                if (node.toString().contains("Verification")) {
                    System.out.println("******" + node.toString());
                    verfiy = true;
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

    public static void main(String ar[]) throws ClassNotFoundException, SQLException {

        GenerateJellyTests.exemappingmodel();
    }

    public static void CreateJellyTestsFolder(String path, boolean CleanFiles, String Feature_name) {
        File f = new File(path);

        if (!f.exists()) {
            log.info("Jelly Testcase Output path not exits");
            return;
        }
        f = new File(path + Feature_name);

        if (f.exists() && !CleanFiles) {
            log.info("Path already exists; Jelly Tests will be overridden");
            return;
        }

        if (f.exists() && CleanFiles) {
            try {
                for (File file : f.listFiles()) {
                    FileDeleteStrategy.FORCE.delete(file);

                }
                log.info("Cleaned all the files in the location" + path);
            } catch (IOException e) {
                log.error("Exception in cleaning files" + e.getMessage());
            }

            log.info("Cleaned previously generated files properly");
        } else {
            try {
                f.mkdirs();
            } catch (Exception e) {
                log.error("Exception in creating Directory" + e.getMessage());
            }
            log.info("Direcory Created " + f.getAbsolutePath());
        }

    }
}
