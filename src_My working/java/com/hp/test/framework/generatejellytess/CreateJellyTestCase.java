package com.hp.test.framework.generatejellytess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yanamalp
 */
public class CreateJellyTestCase {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateJellyTestCase.class.getName());
    static int i = 1;

    // public static void main(String ar[]) throws IOException {
    public static void createUITests(int GID, ArrayList orderList, Map<String, Map<String, String>> mapping_list, String Tests_output_file, String Object_repo_path) throws IOException, ClassNotFoundException, SQLException {
        log.debug("Started generating Jelly Test Cases for the file " + Tests_output_file);
        ModelProperties mp = new ModelProperties();
        String jelly_testcase_path = Tests_output_file;//+ "_" + CreateJellyTestCase.i + ".xml";
        CreateJellyTestCase.i = i + 1;
        // fw = new FileWriter(jelly_testcase_path);
        BufferedWriter fw;

        fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jelly_testcase_path), "UTF-8"));
        fw.write("<?xml version=\"1.0\"?>");
        fw.write("\n");
        fw.write("<j:jelly ");
        fw.write("\n");
        fw.write("xmlns:j=\"jelly:core\"");
        fw.write("\n");
        fw.write("xmlns:x=\"jelly:xml\" ");
        fw.write("\n");
        fw.write("xmlns:spc=\"jelly:com.hp.test.spc.tags.SPCTagLibrary\" ");
        fw.write("\n");
        fw.write("xmlns:sel=\"jelly:com.hp.test.framework.jelly.FrameworkTagLibrary\">");
        fw.write("\n");
    //        String url = mp.getProperty("URL");
        //        String startbrowservalue= "<sel:startbrowser   url=\""+url+"\"/>";
        //        //fw.write("<sel:startbrowser   url=\"http://16.89.90.43\"/>");
        //        fw.write(startbrowservalue);

        fw.write("\n");
        fw.write("");
        Map<String, List<String>> act_exp;
        Map<String, String> Locators;
        ReadTestCases.verfiy = false;
        Locators = ReadLocators.GetLocators(Object_repo_path);
        //    act_exp = ReadTestCases.GetObjects(test_path);
        for (int j = 0; j < orderList.size(); j++) {
            List<String> Actions = new ArrayList<String>();
            if (!mapping_list.containsKey(orderList.get(j))) {
                continue;
            }
            Map<String, String> Actions_list = mapping_list.get(orderList.get(j));
            for (String item : Actions_list.keySet()) {

                String value = Actions_list.get(item);

                if (value.contains("database")) {

                    String temp_ar[] = value.split("\\.");
                    String database_name = temp_ar[1];
                    String table_name = temp_ar[2];
                    String column_name = temp_ar[3];
                    database_name = database_name + ".dat";
                    value = getvaluefromDB(database_name, table_name, column_name, GID);
                }

                String temp_ar[] = item.split(":");
                String Object_type = temp_ar[1];
                String locator_mapping = orderList.get(j) + "_" + item;
                locator_mapping = locator_mapping.replace(":", "_");
                // String temp_ar1[] = temp_ar[0].split("_");
                String Object_id = Locators.get(locator_mapping);

                if (orderList.get(j).toString().equalsIgnoreCase("ExpectedResponse")) {
                    fw.write("<sel:");
                    fw.write("verifytext id=\"" + Object_id + "\" expected=\"" + value + "\"/>");
                    fw.write("\n");
                    continue;
                }

                // Call user function
//                int index = temp_ar[0].indexOf('_');
//                if(temp_ar[0].substring(index+1, index+5).equalsIgnoreCase("auto")) {
//                    // System.out.println(temp_ar[0]);
//                    fw.write("<sel:");
//                    fw.write("userfunctioncall id=\"" + Object_id + "\" value=\"" + value + "\"/>");
//                    fw.write("\n");
//                    continue;
//                }
                switch (Object_type.toUpperCase()) {
                    case "WEBEDIT": {
                        value = value.replaceAll("\"", "&quot;");
                        fw.write("<sel:");
                        fw.write("type id=\"" + Object_id + "\" value=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "TEXTAREA": {
                        value = value.replaceAll("\"", "&quot;");
                        fw.write("<sel:");
                        fw.write("type id=\"" + Object_id + "\" value=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "IMPORT": {

                        fw.write("<sel:");
                        fw.write("uploadfile id=\"" + Object_id + "\" value=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "WEBBUTTON": {

                        fw.write("<sel:");
                        fw.write("click id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "RADIO": {

                        fw.write("<sel:");
                        fw.write("click id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "LINK": {

                        fw.write("<sel:");
                        fw.write("clicklink id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "CLICKLINKINTABLE": {

                        fw.write("<sel:");
                        fw.write("clicklinkintable id=\"" + Object_id + "\" value=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "LIST": {

                        fw.write("<sel:");
                        fw.write("select id=\"" + Object_id + "\" value=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "COMBOBOX": {

                        fw.write("<sel:");
                        fw.write("click id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "CHECKBOX": {

                        fw.write("<sel:");
                        fw.write("check id=\"" + Object_id + "\" value=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "FRAME": {

                        fw.write("<sel:");
                        fw.write("switchtoframe id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    
                     case "IFRAME": {

                        fw.write("<sel:");
                        fw.write("switchtoiframe id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "RIGHTCLICKLINK": {
                        fw.write("<sel:");
                        fw.write("rightclick id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    
/* The following commented out cases is obsolete now. */                    
//                    case "WEBELEMENT": {
//                        fw.write("<sel:");
//                        fw.write("verify id=\"" + Object_id + "\" />");
//                        fw.write("\n");
//                        break;
//                    }
//                    case "WSFRAME": {
//                        fw.write("<sel:");
//                        fw.write("Frame id=\"" + Object_id + "\" />");
//                        fw.write("\n");
//                        break;
//                    }

                    case "WEBFILE": {
                        fw.write("<sel:");
                        fw.write("click id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "WEBLIST": {
                        fw.write("<sel:");
                        fw.write("select id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "WEBTABLE": {
                        fw.write("<sel:");
                        fw.write("select id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "CLICKCHECKBOXBASEDONTEXTLINKINTABLE": {
                        fw.write("<sel:");
                        fw.write("clickcheckboxbasedontextlinkintable id=\"" + Object_id + "\" value=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "CLICKRADIOBASEDONTEXTLINKINTABLE": {
                        fw.write("<sel:");
                        fw.write("clickradiobasedontextlinkintable id=\"" + Object_id + "\" value=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }
                    case "WAIT": {

                        fw.write("<sel:");
                        fw.write("sleep value=\"" + value + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "IMPLICITWAIT": {

                        fw.write("<sel:");
                        fw.write("implicitwait time=\"" + value + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "REFRESHBROWSER": {

                        fw.write("<sel:refreshbrowser />");
                        fw.write("\n");
                        break;
                    }

                    case "PRESSKEY": {

                        fw.write("<sel:");
                        fw.write("presskey id=\"" + Object_id + "\" value=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "MOUSEHOVER": {

                        fw.write("<sel:");
                        fw.write("mousehover id=\"" + Object_id + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "DOUBLECLICK": {

                        fw.write("<sel:");
                        fw.write("doubleclick id=\"" + Object_id + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "ACCEPTPOPUP": {

                        fw.write("<sel:acceptpopup />");
                        fw.write("\n");
                        break;
                    }
                    case "CANCELPOPUP": {

                        fw.write("<sel:cancelpopup />");
                        fw.write("\n");
                        break;
                    }
                    case "DELETECOOKIES": {

                        fw.write("<sel:deletecookies />");
                        fw.write("\n");
                        break;
                    }
                    case "DELETEDIRECTORY": {

                        fw.write("<sel:");
                        fw.write("deletedirectory path=\"" + value + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "AUTOITCLICKOKONPRINT": {

                        fw.write("<sel:autoitclickokonprint />");
                        fw.write("\n");
                        break;
                    }
                    case "SWITCHTOCHILDBROWSER": {

                        fw.write("<sel:switchtochildbrowser />");
                        fw.write("\n");
                        break;
                    }
                    case "SWITCHTOPARENTBROWSER": {

                        fw.write("<sel:switchtoparentbrowser />");
                        fw.write("\n");
                        break;
                    }

                    case "PRESSKEYONBROWSER": {

                        fw.write("<sel:");
                        fw.write("presskeyonbrowser value=\"" + value + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "WAITUNTILELEMENTCLICKABLE": {

                        fw.write("<sel:");
                        fw.write("waituntilelementclickable id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "WAITUNTILELEMENTVISIBLE": {

                        fw.write("<sel:");
                        fw.write("waituntilelementvisible id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "CLICKOKONWINDIALOG": {

                        fw.write("<sel:");
                        fw.write("clickokonwindialog />");
                        fw.write("\n");
                        break;
                    }

                    case "SELECTSAVEANDOKONDOWNLOADDIALOG": {

                        fw.write("<sel:");
                        fw.write("selectsaveandokondownloaddialog />");
                        fw.write("\n");
                        break;
                    }

                    case "FTPFROMLINUXTOWINDOWS": {

                        fw.write("<sel:");
                        fw.write("ftpfromlinuxtowindows value=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }
                    case "GETVARIABLE": {

                        fw.write("<sel:");
                        fw.write("getvariable id=\"" + Object_id + "\" var=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "VERIFYTEXT": {
                        //value=value.replaceAll("\"", "&quot;");
                        fw.write("<sel:");
                        fw.write("verifytext id=\"" + Object_id + "\" expected=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "VERIFYELEMENTEXIST": {

                        fw.write("<sel:");
                        fw.write("verifyelementexist id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "VERIFYELEMENTNOTEXIST": {

                        fw.write("<sel:");
                        fw.write("verifyelementnotexist id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "VERIFYENABLE": {

                        fw.write("<sel:");
                        fw.write("verifyenable id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "VERIFYDISABLE": {

                        fw.write("<sel:");
                        fw.write("verifydisable id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "VERIFYDISPLAYED": {

                        fw.write("<sel:");
                        fw.write("verifydisplayed id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "VERIFYNOTDISPLAYED": {

                        fw.write("<sel:");
                        fw.write("verifynotdisplayed id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "VERIFYTITLEOFPAGE": {

                        fw.write("<sel:");
                        fw.write("verifytitleofpage expected=\"" + value + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "VERIFYTABLE": {

                        fw.write("<sel:");
                        fw.write("verifytable id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }

                    case "VERIFYTEXTNOTEXIST": {
                        fw.write("<sel:");
                        fw.write("verifytextnotexist id=\"" + Object_id + "\" expected=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }
                    case "VERIFYURL": {
                        fw.write("<sel:");
                        fw.write("verifyurl expected=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    case "POPUPVERIFYTEXT": {
                        value = value.replaceAll("\"", "&quot;");
                        fw.write("<sel:");
                        fw.write("popupverifytext expected=\"" + value + "\"/>");
                        fw.write("\n");
                        break;
                    }

                    case "SELSTRING": {
                        fw.write("<sel:");
                        fw.write(value + "/>");
                        fw.write("\n");
                        break;
                    }

                }

            }
        }
//          fw.write("<sel:stopbrowser />");
        fw.write("\n");
        fw.write("</j:jelly>");
        fw.close();
        fw = null;

    //    }
    }

    public static ArrayList readTestFiles(String path) {
        ArrayList<String> testcases = new ArrayList<String>();
        File root = new File(path);
        File[] list = root.listFiles();
        if (list == null) {
            return null;
        }
        for (File f : list) {
            if (f.isDirectory()) {
                readTestFiles(f.getAbsolutePath());

            } else {
                // log.info("File:" + f.getAbsoluteFile());
                String filename = f.getName();
                String Abpath = f.getAbsolutePath();
                String extension = "";
                int i = filename.lastIndexOf('.');
                if (i >= 0) {
                    extension = filename.substring(i + 1);
                }
                try {
                    // GID = GID + 1;
                    testcases.add(Abpath);
                    //  ur.UpdateData(GID, Abpath, extension);
                } catch (Exception e) {
                    //   log.error("Error in updating paths in DM_REFERENCe Table :::" + e.getMessage());
                }
            }
        }

        return testcases;
    }

    public static String getvaluefromDB(String DatabaseName, String TableName, String ColumnName, int GID) throws ClassNotFoundException, SQLException {
        String value = "";
        ModelProperties mp = new ModelProperties();

        String dblocation = mp.getProperty("EXEPECTED_RESULTS_DB_LOCATION");
        mp = null;

        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dblocation + DatabaseName);
            //    if(connection.)
            statement = connection.createStatement();
            String Sql_string = "SELECT " + ColumnName + "  FROM " + TableName + " WHERE GID=" + GID;
            //System.out.println("**************************"+Sql_string);
            ResultSet rs = statement.executeQuery(Sql_string);
            while (rs.next()) {

                //   maptestcasesapi(rs.getString("TESTCASE"));
                value = rs.getString(ColumnName);
                //    System.out.println("**********************************************************");

            }
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
        } catch (SQLException e) {

            log.error("Error in getting GID from DM_TESTCASE table" + e.getMessage());

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return value;

    }
}
