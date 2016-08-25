package com.hp.test.framework.APICommonUtils;


import com.hp.test.framework.DBUtils.openkwysConnection;
import com.hp.test.framework.model.testcasegen.CreateDirectory;
import com.hp.test.framework.model.testcasegen.ReadProperties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Properties;

/**
 *
 * @author yanamalp
 */
public class GetTestCaseParams {

    public static Map<String, String> Locators = new HashMap<String, String>();
    public ArrayList<String> Sqite_Testcase_List = new ArrayList<String>();
    static Logger log = Logger.getLogger(GetTestCaseParams.class.getName());
    private ReadProperties rp = null;
    
    public GetTestCaseParams() {
        this.rp = null;
    }

    // For user supplied properties
    public GetTestCaseParams(final Properties prop) {
        rp = new com.hp.test.framework.model.testcasegen.ReadProperties(prop);
        log.info("User constructed properties used.");
    }
    
    /*
    public static void main(String ar[]) throws SQLException, ClassNotFoundException, IOException {   
        Properties prop = new Properties();
        try {
            FileInputStream in = new FileInputStream("conf/app.properties");
            prop.load(in);
            in.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    
        GetTestCaseParams gtp = new GetTestCaseParams(prop);

        gtp.getTestCaseParams("what ever", "C:\\Users\\frank\\Desktop\\NewGen_jelly\\UserData\\GoogleSearchObjectRepo.xml");

    }
    */
    
    /* This method will return the Object with GID and TESTCASE, read all rows in sqlite database
    based on the feature name provided, Can be called in Data Provider of Testng Class
    */
    
    public Object[][] getDataFromSQLite(String feature) throws ClassNotFoundException {
        Object[][] obj = null;
        openkwysConnection kwys = new openkwysConnection();
        try {
            if( rp==null ) kwys.openDatabaseConnection();
            else kwys.openDatabaseConnection(rp);
            
            System.out.println("Executing Query: " + "SELECT DM_TESTCASE .GID, DM_TESTCASE.TESTCASE, DM_TESTCASE.RESULTS, DM_TESTCASE.JSON_RESULT FROM DM_TESTCASE INNER JOIN DM_SCENARIO_REF ON DM_TESTCASE.TEMPLATE_GID = DM_SCENARIO_REF.GID INNER JOIN DM_MODELXML_REF ON DM_SCENARIO_REF.MODEL_GID_REF=DM_MODELXML_REF.GID AND DM_MODELXML_REF.FEATURENAME=\"" + feature + "\"");
            ResultSet rs = kwys.statement_kwys.executeQuery("SELECT DM_TESTCASE .GID, DM_TESTCASE.TESTCASE, DM_TESTCASE.RESULTS, DM_TESTCASE.JSON_RESULT FROM DM_TESTCASE INNER JOIN DM_SCENARIO_REF ON DM_TESTCASE.TEMPLATE_GID = DM_SCENARIO_REF.GID INNER JOIN DM_MODELXML_REF ON DM_SCENARIO_REF.MODEL_GID_REF=DM_MODELXML_REF.GID AND DM_MODELXML_REF.FEATURENAME=\"" + feature + "\"");
            //ResultSet rs = kwys.statement_kwys.executeQuery("SELECT * FROM DM_TESTCASE");
            while (rs.next()) {
                String temp = rs.getString("GID") + "|" + rs.getString("TESTCASE");
                Sqite_Testcase_List.add(temp);
            }
            //}
            kwys.statement_kwys.close();
            kwys.connection_kwys.close();
            kwys = null;
            log.info("Closed database connections");

            obj = new Object[Sqite_Testcase_List.size()][2];
            for (int i = 0; i < Sqite_Testcase_List.size(); i++) {
                String temp_ar[] = Sqite_Testcase_List.get(i).split("\\|");
                obj[i] = new Object[]{temp_ar[0], temp_ar[1]};
            }

        } catch (Exception e) {
            System.out.println("Error in Getting GIDS" + e.fillInStackTrace());
        }
        return obj;
    }

    /* This method will return the  api filed as key and the values are in list 
    
     input is Testcase String and Mapping File path as String.
    
     for example if uer having the model element as 
    
     <login1_username_webedit>user1</login1_username_webedit>
     <login2_username_webedit>user2</login2_username_webedit>
    
     for the above model elements the corresponding mapping is like below
     <login1>username<login1>
     <login2>username<login2>
    
     and this function will return output as 
     username-->[user1,user2]..
     */
    public Map<String, List<String>> getTestCaseParams(String Testcase, String MappingFilePath) throws IOException, ClassNotFoundException {
        if( rp==null ) rp = new com.hp.test.framework.model.testcasegen.ReadProperties();    
        Map<String, String> TestCase_paramList = new HashMap<String, String>();
        
        
        //Escape XML special characters
        //Testcase=StringEscapeUtils.escapeXml(Testcase);
        
        BufferedWriter fw;
        File f = null;
        //f = File.createTempFile("tmp", ".xml", new File("C:/temp"));
        CreateDirectory.createdir(rp.getProperty("TempPath"));
        f = File.createTempFile("tmp", ".xml", new File(rp.getProperty("TempPath")));
        //  f = File.createTempFile("tmp", ".xml", new File(mp.getProperty("TEMP_LOCATION")));
        fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));

        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        fw.write(Testcase);
        
        fw.close();

        String path = f.getAbsolutePath();
        Locators.clear();
        TestCase_paramList.putAll(GetLocators(path));

        Locators.clear();
        Map<String, String> mapping_api_list = new HashMap<String, String>();
        mapping_api_list = GetLocators(MappingFilePath);//"C:\\ISTF\\DS\\DS_POC\\DS_Search_Mapping.xml");

        Map<String, List<String>> Api_Value_list = new HashMap<>();

        for (String Key : mapping_api_list.keySet()) {
            String Value = "";
            Boolean found = false;
            for (String TestcaseKey : TestCase_paramList.keySet()) {
                if (TestcaseKey.contains(Key)) {
                    Value = TestCase_paramList.get(TestcaseKey);
                    found = true;
                    break;
                }

            }
            if (found) {
                if (!Api_Value_list.containsKey(mapping_api_list.get(Key))) {
                    List<String> temp_list = new ArrayList<String>();
                    temp_list.add(Value);
                    Api_Value_list.put(mapping_api_list.get(Key), temp_list);
                } else {
                    List<String> temp_list = new ArrayList<String>();
                    temp_list = Api_Value_list.get(mapping_api_list.get(Key));
                    temp_list.add(Value);
                    Api_Value_list.put(mapping_api_list.get(Key), temp_list);

                }

            }

        }

        if (f.exists()) {
            f.delete();
        }

        return Api_Value_list;

    }

    public static Map<String, String> GetLocators(String Testcases_path) throws IOException {
        //   ReadTestCases.verfiy=verification;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc;

            // deletes file when the virtual machine terminate
            doc = documentBuilder.parse(new File(Testcases_path));
            //   doc = documentBuilder.parse(f);
            getData(null, doc.getDocumentElement());
            //   System.out.println("purush");
        
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return Locators;
    }

    public static void getData(Node parentNode, Node node) {

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE: {

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
