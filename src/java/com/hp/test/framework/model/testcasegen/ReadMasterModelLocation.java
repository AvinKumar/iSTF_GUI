/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.model.testcasegen;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author sayedmo
 */
public class ReadMasterModelLocation {

    static Logger log = Logger.getLogger(ReadMasterModelLocation.class.getName());
    public static String XMLModelLocation;
    public static String DB_Location;
    

    
    
    public static void main(String ar[]) throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        
        //ReadSourceLocation rs=new ReadSourceLocation();
        ReadProperties rp = new ReadProperties();
        ReadMasterModelLocation.XMLModelLocation = rp.getProperty("MasterXMLModelLocation");
        ReadMasterModelLocation.DB_Location = rp.getProperty("DB_Location");
        
        //Check if the Master model file location exists
        if(!new File(XMLModelLocation).isDirectory()) {
            log.error("User specified Master Model file location not exists: " + XMLModelLocation);
            throw new NotDirectoryException(XMLModelLocation);
        }
        //Check if the DB exists. If not, try to copy system DB to the user specified location 
        if(!new File(DB_Location).isFile()) {
            log.info("User specified Database not exists: " + DB_Location);
            if(ar.length>0) {
                if(new File(ar[0]).isFile()) {
                    Files.copy(new File(ar[0]).toPath(), new File(DB_Location).toPath());
                    log.info("Copied system default database: " + ar[0] + " to: " + DB_Location);
                }
                else {
                    log.error("System DB not found: " + ar[0]);
                    throw new FileNotFoundException(ar[0]);
                }
            }
            else {
                log.error("System default DB not specified.");
                throw new FileNotFoundException("User specified Database not exists: " + DB_Location + "\nSystem default DB not specified.");
            }
        }

        Filewalker1 fw = new Filewalker1();
        Update_DM_MASTERMODELXML_REF udm = new Update_DM_MASTERMODELXML_REF();
        fw.walk(ReadMasterModelLocation.XMLModelLocation);
        udm.openDatabaseConnection();
        udm.getMasterModelData();
        ConvertFiletoString convertfiletostring=new ConvertFiletoString();
         
        for (String path : Filewalker1.Master_Model_list.keySet()) {
            if (!Filewalker1.Testcase_not_generated_list.containsKey(path)) {
                String[] temp_ar = Filewalker1.Master_Model_list.get(path).split("::");
                String xmlString=convertfiletostring.convertFiletoString(path);
                udm.UpdateData1(path, temp_ar[1], temp_ar[0], temp_ar[2], temp_ar[3],xmlString);
                log.info("Inserted Master Model File in to the DB" + path);
            }
        }
        SplitModelXML splitxml = new SplitModelXML();
        udm.closeconnection();
        for (String Abpath : Filewalker1.Master_Model_list.keySet()) {
            log.info("Started splitting Master Model File" + Abpath);
            splitxml.ReadMasterLModelXML(Abpath);
            log.info("End of splitting Master Model File" + Abpath);
            log.info("**************************************************************");
        }

        log.info("Started splitting Master ModelFile");
        udm.openDatabaseConnection();
    if( SplitModelXML.Modelxml_list.isEmpty())
    {
        log.info("No Master Model Files are present to split OR Test Cases might have been generated already for all the Files; Please check");
        System.exit(1);
    }
        for (String abpath : SplitModelXML.Modelxml_list.keySet()) {
            
            File f = new File(abpath);
            String filename = f.getName();
            f = null;
            String[] temp_ar = filename.split(",");
            int master_gid = Integer.valueOf(temp_ar[0]);
            String[] temp_ar_data = temp_ar[1].split("_");
            String productname = temp_ar_data[0];
            String featurename = temp_ar_data[1];

            udm.insertdataMasterModel(abpath, ".xml", filename, productname, featurename, master_gid);

        }
        udm.closeconnection();
        log.info("Successful inserted splitted model files to DM_MOEDLXML_REF Table");
        log.info("**************************************************************");

        log.info("Started generating Templates ");
        TestCaseGenerator testcasegenerator = new TestCaseGenerator();

        for (String Abpath : Filewalker1.Master_Model_list.keySet()) {
            udm.openDatabaseConnection();
            int GID = udm.getMasterModelGID(Abpath);

            udm.getModelXml_RefData(GID);
            udm.closeconnection();

            for (int model_gid : Update_DM_MASTERMODELXML_REF.Model_xml_table_data.keySet()) {
                try {
                    testcasegenerator.CreateTemplatefromModel(Update_DM_MASTERMODELXML_REF.Model_xml_table_data.get(model_gid), model_gid);
                } catch (SQLException e) {
                    log.info("Error in generating Templates for the model file" + Update_DM_MASTERMODELXML_REF.Model_xml_table_data.get(model_gid));
                }
            }
            Update_DM_MASTERMODELXML_REF.Model_xml_table_data.clear();
        }
        log.info("End of generating Templates ");
        log.info("**************************** ");

        log.info("Started generating TestCases from the Templates ");
        for (String Abpath : Filewalker1.Master_Model_list.keySet()) {
            udm.openDatabaseConnection();
            int GID = udm.getMasterModelGID(Abpath);

            udm.getModelXml_RefData(GID);
            for (int model_gid : Update_DM_MASTERMODELXML_REF.Model_xml_table_data.keySet()) {

                udm.getScenario_RefData(model_gid);
               
                for (int scenario_gid : Update_DM_MASTERMODELXML_REF.Scenario_xml_table_data.keySet()) {

                    try {
                        log.info("Started generating Testcases for the Template" + Update_DM_MASTERMODELXML_REF.Scenario_xml_table_data.get(scenario_gid));
                        testcasegenerator.CreateTestCasesFromTemplate(Update_DM_MASTERMODELXML_REF.Scenario_xml_table_data.get(scenario_gid), scenario_gid);
                    } catch (SQLException e) {
                        log.info("Error in generating TestCases for the Template file" + Update_DM_MASTERMODELXML_REF.Scenario_xml_table_data.get(scenario_gid));
                    }
                }
                Update_DM_MASTERMODELXML_REF.Scenario_xml_table_data.clear();
            
            }
            Update_DM_MASTERMODELXML_REF.Model_xml_table_data.clear();
            testcasegenerator.updateModelStatus(GID);
        }

        log.info("End of generating TestCases from the Templates ");
        log.info("********************************************** ");

    }

}

class Filewalker1 {

    static Logger log = Logger.getLogger(Filewalker.class.getName());
    Update_DM_MASTERMODELXML_REF ur;
    SplitModelXML splitxml = new SplitModelXML();
    public static Map<String, String> Master_Model_list;
    public static Map<String, String> Testcase_not_generated_list;

    public void walk(String path) {
        BasicConfigurator.configure();
        Master_Model_list = new HashMap<String, String>();
        Testcase_not_generated_list = new HashMap<String, String>();
        getpaths(path);
    }

    public void getpaths(String path) {
        log.info("Started listing  all the Master Model Files in the Directory :::" + path);
        File root = new File(path);
        File[] list = root.listFiles();
        if (list == null) {
            return;
        }
        for (File f : list) {
            if (f.isDirectory()) {
                getpaths(f.getAbsolutePath());

            } else {
                // log.info("File:" + f.getAbsoluteFile());
                String filename = f.getName();
                String[] fullname = filename.split("_");
                String productname = fullname[0];

                String featurename = fullname[1];
                String Abpath = f.getAbsolutePath();
                String extension = "";
                int i = filename.lastIndexOf('.');
                if (i >= 0) {
                    extension = filename.substring(i + 1);
                }
                try {

                    //   ur.UpdateData1(Abpath, extension, filename, productname, featurename);
                    Master_Model_list.put(Abpath, filename + "::" + extension + "::" + productname + "::" + featurename);

                } catch (Exception e) {
                    log.error("Error in updating paths in DM_MASTERMODELXML_REF Table :::" + e.getMessage());
                }
            }
        }
    }

}

class Update_DM_MASTERMODELXML_REF {

    Connection connection = null;
    Statement statement;
    PreparedStatement prestatement = null;
    
    public static Map<Integer, String> Model_xml_table_data = new HashMap<Integer, String>();
    public static Map<Integer, String> Scenario_xml_table_data = new HashMap<Integer, String>();
    public static long GID = 0;

    static Logger log = Logger.getLogger(Update_DM_MASTERMODELXML_REF.class.getName());

    public void openDatabaseConnection() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + ReadMasterModelLocation.DB_Location);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
        } catch (SQLException e) {
            log.error("Issue in opening connection" + e.getMessage());
        }

    }

    public void UpdateData1(String path, String extension, String filename, String productname, String featurename,String XmlString) throws ClassNotFoundException {

        Class.forName("org.sqlite.JDBC");

        try {
            Class.forName("org.sqlite.JDBC");
            
            String Query="INSERT INTO DM_MASTERMODELXML_REF(MASTERMODEL_PATH, MASTERMODEL_EXTENSION, MASTERMODEL_NAME, PRODUCTNAME, FEATURENAME, XML_FILE) values(?,?,?,?,?,?)";
           
            //System.out.println("Hi I am print results"+Query);
            prestatement=connection.prepareStatement(Query);
            
            prestatement.setString(1, path);
            prestatement.setString(2, extension);
            prestatement.setString(3, filename);
            prestatement.setString(4, productname);
            prestatement.setString(5, featurename);
            prestatement.setString(6, XmlString);    
                
            prestatement.execute();
            
            ResultSet rs = statement.executeQuery("SELECT GID from DM_MASTERMODELXML_REF WHERE MASTERMODEL_PATH LIKE '" + path + "' AND MASTERMODEL_NAME LIKE '" + filename + "'");
            while (rs.next()) {
                GID = rs.getInt("GID");

            }
            //   statement.executeUpdate("INSERT INTO DM_MASTERMODELXML_REF (MASTERMODEL_PATH, MASTERMODEL_EXTENSION, MASTERMODEL_NAME, PRODUCTNAME, FEATURENAME)SELECT * FROM (SELECT '" + path + "', '" + extension + "','" + filename + "', '" + productname + "', '" + featurename + "') AS tmp WHERE NOT EXISTS (SELECT MASTERMODEL_NAME FROM DM_MASTERMODELXML_REF WHERE MASTERMODEL_NAME = '" + filename + "') LIMIT 1");
        } catch (SQLException e) {

            log.error("Error in inserting data in to DM_MASTERMODELXML_REF Table" + e.getMessage());
           throw new RuntimeException("Runtime Exception");
        }
    }

    public void insertdataMasterModel(String path, String extension, String filename, String productname, String featurename, int mater_gid) throws ClassNotFoundException {

        
        ConvertFiletoString convertfiletostring=new ConvertFiletoString();
        String xmlString=convertfiletostring.convertFiletoString(path);
        Class.forName("org.sqlite.JDBC");

        try {
            Class.forName("org.sqlite.JDBC");
//            String value = "'" + path + "'" + "," + "'" + extension + "'" + "," + "'" + filename + "'" + "," + "'" + productname + "'" + "," + "'" + featurename + "'" + "," + mater_gid+  ",'"+ xmlString + "'";
//            statement.executeUpdate("INSERT INTO DM_MODELXML_REF(MODEL_PATH, MODEL_EXTENSION, MODEL_NAME, PRODUCTNAME, FEATURENAME,MASTER_GID,XML_FILE) values(" + value + ")");

            String Query="INSERT INTO DM_MODELXML_REF(MODEL_PATH, MODEL_EXTENSION, MODEL_NAME, PRODUCTNAME, FEATURENAME,MASTER_GID,XML_FILE) values(?,?,?,?,?,?,?)";
           
            //System.out.println("Hi I am print results"+Query);
            prestatement=connection.prepareStatement(Query);
            
            prestatement.setString(1, path);
            prestatement.setString(2, extension);
            prestatement.setString(3, filename);
            prestatement.setString(4, productname);
            prestatement.setString(5, featurename);
            prestatement.setInt(6, mater_gid); 
            prestatement.setString(7, xmlString);    
                
            prestatement.execute();
            
            //   statement.executeUpdate("INSERT INTO DM_MASTERMODELXML_REF (MASTERMODEL_PATH, MASTERMODEL_EXTENSION, MASTERMODEL_NAME, PRODUCTNAME, FEATURENAME)SELECT * FROM (SELECT '" + path + "', '" + extension + "','" + filename + "', '" + productname + "', '" + featurename + "') AS tmp WHERE NOT EXISTS (SELECT MASTERMODEL_NAME FROM DM_MASTERMODELXML_REF WHERE MASTERMODEL_NAME = '" + filename + "') LIMIT 1");
        } catch (SQLException e) {

            log.error("Error in inserting data in to DM_MODELXML_REF Table" + e.getCause());
            throw new RuntimeException("Runtime Exception");
        }
    }

    public void getMasterModelData() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        // Map<String,String> Testcase_not_generated_list=new HashMap<String,String>();
        Map<String, String> Testcase_geneated_list = new HashMap<String, String>();
        for (String path : Filewalker1.Master_Model_list.keySet()) {
            int GID = 0;
            try {
                String[] filename_temp = Filewalker1.Master_Model_list.get(path).split("::");
                String filename = filename_temp[0];

                ResultSet rs = statement.executeQuery("SELECT MASTERMODEL_PATH,MASTERMODEL_NAME from DM_MASTERMODELXML_REF WHERE TESTCASE_GEN_STATUS IS NULL AND MASTERMODEL_PATH LIKE '" + path + "' AND MASTERMODEL_NAME LIKE '" + filename + "'");
                while (rs.next()) {
                    Filewalker1.Testcase_not_generated_list.put(rs.getString("MASTERMODEL_PATH"), rs.getString("MASTERMODEL_NAME"));

                }

                rs = statement.executeQuery("SELECT MASTERMODEL_PATH,MASTERMODEL_NAME from DM_MASTERMODELXML_REF WHERE TESTCASE_GEN_STATUS  LIKE 'COMPLETED' AND MASTERMODEL_PATH LIKE '" + path + "' AND MASTERMODEL_NAME LIKE '" + filename + "'");
                while (rs.next()) {
                    Testcase_geneated_list.put(rs.getString("MASTERMODEL_PATH"), rs.getString("MASTERMODEL_NAME"));

                }

                //   statement.executeUpdate("INSERT INTO DM_MASTERMODELXML_REF (MASTERMODEL_PATH, MASTERMODEL_EXTENSION, MASTERMODEL_NAME, PRODUCTNAME, FEATURENAME)SELECT * FROM (SELECT '" + path + "', '" + extension + "','" + filename + "', '" + productname + "', '" + featurename + "') AS tmp WHERE NOT EXISTS (SELECT MASTERMODEL_NAME FROM DM_MASTERMODELXML_REF WHERE MASTERMODEL_NAME = '" + filename + "') LIMIT 1");
            } catch (SQLException e) {

                log.error("Error in Getting  data in to DM_MASTERMODELXML_REF Table" + e.getMessage());
               throw new RuntimeException("Runtime Exception");
            }

        }
        if (Testcase_geneated_list.size() > 0) {
            log.info("****************************************************");
            for (String path : Testcase_geneated_list.keySet()) {
                Filewalker1.Master_Model_list.remove(path);
                log.info("TestCase already generated for the File" + path);
            }
            log.info("******************************************************");

        }

        if (Filewalker1.Testcase_not_generated_list.size() > 0) {

            log.info("******************************************************");
            for (String path : Filewalker1.Testcase_not_generated_list.keySet()) {
                // Filewalker1.Master_Model_list.remove(path);
                log.info("Master Model in DB but TestCases are not generated" + path);
            }
            log.info("******************************************************");
        }

        if (Filewalker1.Master_Model_list.size() > 0) {

            log.info("******************************************************");
            for (String path : Filewalker1.Master_Model_list.keySet()) {
                if (!Filewalker1.Testcase_not_generated_list.containsKey(path)) {
                    log.info("Master Model not in DB " + path);
                }
            }
            log.info("******************************************************");
        }
    }

    public void getScenario_RefData(int GID) {

        try {
            Class.forName("org.sqlite.JDBC");
            ResultSet rs = statement.executeQuery("SELECT GID,TEMPLATE_PATH from DM_SCENARIO_REF WHERE  MODEL_GID_REF=" + GID + " ORDER BY GID ASC");

            while (rs.next()) {
                Scenario_xml_table_data.put(rs.getInt("GID"), rs.getString("TEMPLATE_PATH"));
            }
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
            log.error("Error in getting DM_MODELXML_REF data for the Master Model_GID " + GID);
        }

    }

    public void getModelXml_RefData(int GID) {

        try {
            Class.forName("org.sqlite.JDBC");
            ResultSet rs = statement.executeQuery("SELECT GID,MODEL_PATH from DM_MODELXML_REF WHERE  MASTER_GID=" + GID);

            while (rs.next()) {
                Model_xml_table_data.put(rs.getInt("GID"), rs.getString("MODEL_PATH"));
            }
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
            log.error("Error in getting DM_MODELXML_REF data for the Master Model_GID " + GID);
        }

    }

    public int getMasterModelGID(String path) {
        int GID = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            ResultSet rs = statement.executeQuery("SELECT GID from DM_MASTERMODELXML_REF WHERE  MASTERMODEL_PATH LIKE '" + path + "'");

            while (rs.next()) {
                GID = rs.getInt("GID");
            }
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
            log.error("Error in getting MASTER MODEL GID for the path " + path);
        }
        return GID;
    }

    public void closeconnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Error in closing Connectiom" + e.getMessage());
        }
    }

    
}
