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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yanamalp
 */
public class CreateReadableTestCase {
static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateReadableTestCase.class.getName());
    static int j=1;
   public static  BufferedWriter fw; 
   // public static void main(String ar[]) throws IOException {
    public static void createRedableTests(ArrayList orderList, Map<String, Map<String, String>> mapping_list,String Object_repo_path) throws IOException, ClassNotFoundException, SQLException {
       
          log.info("Genrating Readable Testcase in the File");
            ModelProperties mp=new ModelProperties();
            int i=0;
                     
            String url = mp.getProperty("URL");
            String startbrowservalue= "\""+url+"\"/>";
          
            i=i+1;
            fw.write("TestCase Number  "+ j +",,,");
            fw.write("\n");
          //   fw.write("\n***********************************\n" );
            CreateReadableTestCase.j=j+1;
            fw.write(",Step "+ i +", Open the URL"+startbrowservalue+",,");
            fw.write("\n");
           // fw.write("\n");
           // fw.write("");
            Map<String, List<String>> act_exp;
            Map<String, String> Locators;
            ReadTestCases.verfiy = false;
            Locators = ReadLocators.GetLocators(Object_repo_path);
        //    act_exp = ReadTestCases.GetObjects(test_path);
         for(int j=0;j<orderList.size();j++)
         {
            
            List<String> Actions = new ArrayList<String>();
            if(!mapping_list.containsKey(orderList.get(j)))
                continue;
            Map<String,String> Actions_list = mapping_list.get(orderList.get(j));
            for (String item : Actions_list.keySet()) {
                 i=i+1;
               String value = Actions_list.get(item);
               
               if(value.contains("database"))
               {
                   
                   String temp_ar[]=value.split("\\.");
                   String database_name=temp_ar[1];
                   String table_name=temp_ar[2];
                   String column_name=temp_ar[3];
                   database_name=database_name+".dat";
              //    value=getvaluefromDB(database_name,table_name,column_name,GID);
               }
               
               String temp_ar[]=item.split(":");
               String Object_type=temp_ar[1];
               String Field_name=orderList.get(j)+":"+ temp_ar[0];
          
               String locator_mapping=orderList.get(j)+"_"+ item;
               locator_mapping=locator_mapping.replace(":", "_");
               // String temp_ar1[] = temp_ar[0].split("_");
                String Object_id = Locators.get(locator_mapping);
                
               if(orderList.get(j).toString().equalsIgnoreCase("ExpectedResponse")) 
               {
                   // fw.write("<sel:");
                      //  fw.write("verifytext id=\"" + Object_id + "\" expected=\"" + value + "\"/>");
                      //  fw.write("\n");
                        fw.write(",Step "+ i +",, Expected Response Value \" "+  value+"\"");
                        fw.write("\n");
                        break;
                       
               }
                   
                
                
         
                switch (Object_type.toUpperCase()) {
                    case "WEBEDIT": {

                      
                        fw.write(",Step "+ i +", Enter "+ Field_name +" with the value \""+ value+"\",,");
                        fw.write("\n");
                        break;
                    }
                    
                     case "TEXTAREA": {

                       
                       
                       fw.write(",Step "+ i +", Enter "+ Field_name +" with the value \""+ value+"\",,");
                        fw.write("\n");
                        break;
                    }


                    case "WEBBUTTON": {

                        fw.write(",Step "+ i +", Click on the button \""+ Field_name + "\",,");
                        fw.write("\n");
                        break;
                    }
                    
                     case "LINK": {

                         fw.write(",Step "+ i +", Click on the link \""+ value+"\",," );
                        fw.write("\n");
                        
                        break;
                    }
                    
                     case "LIST": {

                        fw.write(",Step "+ i +", Select \""+ value + "\" From the List "+ Field_name +",," );
                        fw.write("\n");
                        break;
                    }
                     
                     case "CHECKBOX": {
                         if(value.toLowerCase().equals("on"))
                         {
                         fw.write(",Step "+ i +", select the check box  "+ Field_name +",," );
                        fw.write("\n");
                         }else
                         {
                              fw.write(",Step "+ i +", Unselect the check box  "+ Field_name+",," );
                              fw.write("\n");
                         }
                        break;
                    }
                    
                        case "FRAME": {

                        fw.write("<sel:");
                        fw.write("switchtoframe id=\"" + Object_id + "\" />");
                        fw.write("\n");
                        break;
                    }
                    
                }

            }


         }
    

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
    
    
    public static String getvaluefromDB(String DatabaseName,String TableName,String ColumnName,int GID) throws ClassNotFoundException, SQLException
    {
        String value="";
        ModelProperties mp=new ModelProperties();
        
        String dblocation=mp.getProperty("EXEPECTED_RESULTS_DB_LOCATION");
        mp=null;
        
        
         Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dblocation + DatabaseName );
          //    if(connection.)
            statement = connection.createStatement();
            String Sql_string="SELECT " + ColumnName +"  FROM " + TableName + " WHERE GID=" + GID;
            //System.out.println("**************************"+Sql_string);
            ResultSet rs = statement.executeQuery(Sql_string);
            while (rs.next()) {

             //   maptestcasesapi(rs.getString("TESTCASE"));
              value=rs.getString(ColumnName);
            //    System.out.println("**********************************************************");

            }
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
        } catch (SQLException e) {

           log.error("Error in getting GID from DM_TESTCASE table"+e.getMessage());
              
        } 

        finally {
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
