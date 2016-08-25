/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.htmparse;

import static com.hp.test.framework.DBUtils.DatabaseUtils.connection;
import com.hp.test.framework.model.testcasegen.ReadProperties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yanamalp
 */
public class GenerateEnvWiseReport {

    public static Connection connection;
    public static Statement statement;

   

    public void openDatabaseConnection() throws ClassNotFoundException {
        ReadProperties rp = new ReadProperties();
        String temp_path = rp.getProperty("TempPath");
        temp_path = temp_path + "\\Reports.dat";
        Class.forName("org.sqlite.JDBC");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + temp_path);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateEnvReport(File SourceFile, String TargetPath, String Run) throws FileNotFoundException, IOException {

        //String[] CsvFile = TargetPath.split("/.");
        BufferedReader in = new BufferedReader(new FileReader(SourceFile));
       //  String TargetPath = filename[0]+"/index1.html";
        BufferedWriter out = new BufferedWriter(new FileWriter(TargetPath+"/EnvWiseReport/index.html"));

        Map<String, Integer> OverAllStatus = new HashMap<String, Integer>();
        Map<String, Map<String, Integer>> envwiseCounts = new HashMap<>();
        Map<String, Map<String, String>> TestWiseStatus = new HashMap<>();
        Map<String, String> TestCaseDetails_list = new HashMap<>();
        Map<String, String> Environment_list = new HashMap<String, String>();
        int temp_counter = 0;
        String TestCase_Env = "";
        String str;
        ResultSet resultset;
        String Csv_path=TargetPath+"/EnvWiseReport/RunCounts.csv";
        try {
            PrintWriter runs = new PrintWriter(new BufferedWriter(new FileWriter(Csv_path, true)));
            statement = connection.createStatement();
            runs.print(Run + ",");
            // Getting overall status of the Execution
            resultset = statement.executeQuery("select status,count(*) as counts from TESTCASE_STATUS group by status");
            while (resultset.next()) {
                String status = resultset.getString("status");
                int count = Integer.valueOf(resultset.getString("counts"));

                OverAllStatus.put(status, count);

            }

            int count = 0;
            if (OverAllStatus.containsKey("Pass")) {
                count = OverAllStatus.get("Pass");
            }
            runs.print(count + ",");
            count = 0;
            if (OverAllStatus.containsKey("Fail")) {
                count = OverAllStatus.get("Fail");
            }
            runs.print(count + ",");
            count = 0;
            if (OverAllStatus.containsKey("Skip")) {
                count = OverAllStatus.get("Skip");
            }
            runs.print(count);
            runs.print("\n");
            runs.close();
            // Getting the  Counts environment wise
            resultset = statement.executeQuery("select Environment,status,count(*) as counts from TESTCASE_STATUS group by Environment,status order by Environment");

            while (resultset.next()) {
                Map<String, Integer> test_status_list = new HashMap<String, Integer>();
                String Environment = resultset.getString("Environment");
                String status = resultset.getString("status");
                String counts = resultset.getString("counts");
                test_status_list.put(status, Integer.valueOf(counts));
                if (!envwiseCounts.containsKey(Environment)) {

                    envwiseCounts.put(Environment, test_status_list);
                } else {

                    Map<String, Integer> temp_list = envwiseCounts.get(Environment);
                    temp_list.put(status, Integer.valueOf(counts));

                    envwiseCounts.put(Environment, temp_list);

                }

            }

            //Generating Report  BY TestCase for each  Environment
            //****************************************************
            resultset = statement.executeQuery("select parent.TestCaseDesc,parent.Environment,parent.status from TESTCASE_STATUS as parent where TestCaseDesc =(select TestCaseDesc from TESTCASE_STATUS as child where parent.TestCaseDesc=child.TestCaseDesc) group by parent.TestCaseDesc ,parent.Environment");
            while (resultset.next()) {
                Map<String, String> test_status_list = new HashMap<String, String>();
                String TestCaseDesc = resultset.getString("TestCaseDesc");
                String Environment = resultset.getString("Environment");
                Environment_list.put(Environment, "exits");
                String status = resultset.getString("status");
                test_status_list.put(Environment, status);
                if (!TestWiseStatus.containsKey(TestCaseDesc)) {

                    TestWiseStatus.put(TestCaseDesc, test_status_list);
                } else {

                    Map<String, String> temp_list = TestWiseStatus.get(TestCaseDesc);
                    temp_list.put(Environment, status);

                    TestWiseStatus.put(Environment, temp_list);

                }

            }

          //  int env_size=Environment_list.size();
            for (String Testcase : TestWiseStatus.keySet()) {

                Map<String, String> temp_env_status = TestWiseStatus.get(Testcase);
                Testcase = Testcase.replaceAll("\"", "\'");
                //   String temp="[ \""+Testcase+"\",";
                int temp_i = 0;
                String temp = "data.setCell(" + temp_counter + ", " + temp_i + ", \"" + Testcase + "\",null,{'className': 'italic-testcase-font large-font'});\n";
                TestCase_Env = TestCase_Env + temp;
                int i = 1;
                for (String env : Environment_list.keySet()) {
                     String Status="";
                    if(temp_env_status.containsKey(env))
                     Status = temp_env_status.get(env);
                    else
                     Status="NotExecuted";
                    
                    String Replacechar = "-";
                    
                   if (Status.equals("NotExecuted")) {
                        Replacechar = "NE";
                        temp = "data.setCell(" + temp_counter + ", " + i + ", '" + Replacechar + "',null,{'className': '.italic-green-font large-font'});\n"; 
                   }
                        else if (Status.equals("Pass")) {
                        Replacechar = "✔";
                        temp = "data.setCell(" + temp_counter + ", " + i + ", '" + Replacechar + "',null,{'className': '.italic-green-font large-font'});\n";
                    } else if (Status.equals("Fail")) {
                        Replacechar = "✗";
                        temp = "data.setCell(" + temp_counter + ", " + i + ", '" + Replacechar + "',null,{'className': 'italic-purple-font large-font'});\n";
                    } else {
                        temp = "data.setCell(" + temp_counter + ", " + i + ", '" + Replacechar + "',null,{'className': 'italic-purple-font large-font'});\n";
                    }
//                    if(i==1)
//                     temp=temp+"'"+Replacechar+"'";
//                    else
//                       temp=temp+",'"+Replacechar+"'"; 

                    TestCase_Env = TestCase_Env + temp;
                    i = i + 1;
                }
//               temp=temp+"]";
//               if(temp_counter==1)
//                TestCase_Env=TestCase_Env+temp;
//               else
//                 TestCase_Env=TestCase_Env+",\n"+temp;
                temp_counter = temp_counter + 1;
            }

            //getting the Testcase details environment wise
            //*********************************************
            for (String env : envwiseCounts.keySet()) {

                resultset = statement.executeQuery("select TestCaseDesc,status from TESTCASE_STATUS where Environment like '" + env + "' order by status");
                String overallString = "";
                int i = 1;
                while (resultset.next()) {
                    String str_temp;
                    String Testcase = resultset.getString("TestCaseDesc");
                    Testcase = Testcase.replaceAll("\"", "\'");
                    String status = resultset.getString("status");
                    if (i == 1) {
                        str_temp = "[\"" + Testcase + "\",'" + status + "']\n";
                    } else {
                        str_temp = ",\n[\"" + Testcase + "\",'" + status + "']";
                    }
                    i = i + 1;
                    overallString = overallString + str_temp;
                }
                TestCaseDetails_list.put(env, overallString);
            }

        } catch (SQLException e) {
            System.out.println("Error in Getting data from reports" + e.getMessage());
        }
        String temp_envron_count_wise = "";
        int grand_passcount = 0;
        int grand_failcount = 0;
        int grand_skipcount = 0;
        int i = 1;
        for (String env : envwiseCounts.keySet()) {
            int passcount = 0;
            int failcount = 0;
            int skipcount = 0;
            int envTotalCases = 0;
            Map<String, Integer> temp_list = envwiseCounts.get(env);

            if (temp_list.containsKey("Pass")) {
                passcount = temp_list.get("Pass");
            }
            if (temp_list.containsKey("Fail")) {
                failcount = temp_list.get("Fail");
            }
            if (temp_list.containsKey("Skip")) {
                skipcount = temp_list.get("Skip");
            }
            envTotalCases = passcount + failcount + skipcount;
            grand_passcount = grand_passcount + passcount;
            grand_failcount = grand_failcount + failcount;
            grand_skipcount = grand_skipcount + skipcount;
            String temp = "";
            if (i == envwiseCounts.size()) {
                int total_count = grand_passcount + grand_failcount + grand_skipcount;
                temp = ",['" + env + "',  {v: 10000, f: '" + passcount + "'}, {v: 10000, f: '" + failcount + "'},  {v: 10000, f: '" + skipcount + "'},{v: 10000, f: '" + envTotalCases + "'}]";
                temp_envron_count_wise = temp_envron_count_wise + temp;
                env = "Total";
                temp = ",[' ',  {v: 10000, f: ' '}, {v: 10000, f: ' '},  {v: 10000, f: ' '},{v: 10000, f: ' '}]";
                temp_envron_count_wise = temp_envron_count_wise + temp;
                temp = ",['" + env + "',  {v: 10000, f: '" + grand_passcount + "'}, {v: 10000, f: '" + grand_failcount + "'},  {v: 10000, f: '" + grand_skipcount + "'},{v: 10000, f: '" + total_count + "'}]";
                temp_envron_count_wise = temp_envron_count_wise + temp;
                break;
            }
            if (i == 1) {
                temp = "['" + env + "',  {v: 10000, f: '" + passcount + "'}, {v: 10000, f: '" + failcount + "'},  {v: 10000, f: '" + skipcount + "'},{v: 10000, f: '" + envTotalCases + "'}]";
            } else {
                temp = ",['" + env + "',  {v: 10000, f: '" + passcount + "'}, {v: 10000, f: '" + failcount + "'},  {v: 10000, f: '" + skipcount + "'},{v: 10000, f: '" + envTotalCases + "'}]";
            }
            // ['Win 7',  {v: 10000, f: '100'}, {v: 10000, f: '20'},  {v: 10000, f: '5'},{v: 10000, f: '125'}],
            temp_envron_count_wise = temp_envron_count_wise + temp;
            i = i + 1;
        }
       //************************************************************************* 
        //Readig Template file and updting the Data for Environments and Testcases
        //**************************************************************************
        while ((str = in.readLine()) != null) {
            
            
            if(str.contains("<LINE_CHART_DATA>"))
            {
                
                out.write(LineChartData(Csv_path));
                continue;
            }

            if (str.contains("<TotalPassed>")) {
                int count = 0;
                if (OverAllStatus.containsKey("Pass")) {
                    count = OverAllStatus.get("Pass");
                }
                str = str.replace("<TotalPassed>", String.valueOf(count));
                out.write(str);
                out.write("\n");

                continue;
            }

            if (str.contains("<TotalFailed>")) {
                int count = 0;
                if (OverAllStatus.containsKey("Fail")) {
                    count = OverAllStatus.get("Fail");
                }
                str = str.replace("<TotalFailed>", String.valueOf(count));
                out.write(str);
                out.write("\n");
                continue;
            }

            if (str.contains("<TotalSkipped>")) {
                int count = 0;
                if (OverAllStatus.containsKey("Skip")) {
                    count = OverAllStatus.get("Skip");
                }
                str = str.replace("<TotalSkipped>", String.valueOf(count));
                out.write(str);
                out.write("\n");
                continue;
            }

            if (str.contains("<ENV_WISE_REPORT>")) {
                str = str.replace("<ENV_WISE_REPORT>", temp_envron_count_wise);
                out.write(str);
                out.write("\n");
                continue;
            }

            if (str.contains("<TESTCASES_DETAILS_ENVIRONMENT_WISE_FUN>")) {

                for (String localenv : TestCaseDetails_list.keySet()) {
                    String temp_env = localenv.replaceAll(" ", "");
                    out.write("google.setOnLoadCallback(" + temp_env + ");");
                    out.write("\n");
                }

                continue;
            }

            if (str.contains("<FUNCTIONS_FOR_ENVIRONMENT_WISE_TESTCASES>")) {

                for (String localenv : TestCaseDetails_list.keySet()) {
                    String temp_env = localenv.replaceAll(" ", "");
                    out.write(DrawEnvironMentTablefun(temp_env, TestCaseDetails_list.get(localenv), temp_env));
                    // out.write("google.setOnLoadCallback("+temp_env+");");
                    out.write("\n");
                    out.write("\n");

                }

                continue;
            }

            if (str.contains("<FUNCTIONS_FOR_TESTCASEWISE_REPORT>")) {

                out.write(DrawTestCaseWiseTableFun(Environment_list, TestCase_Env, temp_counter));
                // out.write("google.setOnLoadCallback("+temp_env+");");
                out.write("\n");
                out.write("\n");

                continue;
            }

            if (str.contains("<TABLE_DETAILS1>")) {

                for (String localenv : TestCaseDetails_list.keySet()) {
                    String temp_env = localenv.replaceAll(" ", "");
                    out.write(getTableDetails(temp_env));
                    // out.write("google.setOnLoadCallback("+temp_env+");");
                    out.write("\n");
                    out.write("\n");

                }

                continue;
            }

            out.write(str);
            out.write("\n");
        }

        in.close();
        out.close();

    }

    public String DrawEnvironMentTablefun(String TableName, String TestCases, String FunctionName) {
        String str = "";

        str = " function " + FunctionName + "() {";
        str = str + "\n";
        str = str + "    var data = new google.visualization.DataTable();";
        str = str + "\n";
        //  data.addColumn('string', 'SiNo');
        str = str + "data.addColumn('string', 'Description');";
        str = str + "\n";
        str = str + "data.addColumn('string', 'status');";
        str = str + "\n";
        str = str + "   data.addRows([";
        str = str + "\n";
        str = str + TestCases;
        str = str + "\n";

        str = str + "    ]);";
        str = str + "\n";
        str = str + "   var table = new google.visualization.Table(document.getElementById('" + FunctionName + "'));";
        str = str + "\n";

        str = str + " table.draw(data, {showRowNumber: true, width: '100%', height: '100%'}); }";
        str = str + "\n";
        return str;
    }

    public String getTableDetails(String TableName) {
        String Details;

        Details = "  <h2>" + TableName + "( Execution Details)</h2>\n";
        Details = Details + "<ul>\n";
        Details = Details + "<li><div id=\"" + TableName + "\"></div></li>\n";
        Details = Details + "</ul>\n";

        return Details;
    }

    public String DrawTestCaseWiseTableFun(Map<String, String> env_list, String TestCaseStatus, int rows) {
        String str = "";

        str = "    function drawTestCaseTable() {\n";
        str = str + "   var data = new google.visualization.DataTable();\n";
        str = str + "	data.addColumn('string', 'TestCaseName');\n";

        for (String env : env_list.keySet()) {
            str = str + " data.addColumn('string', '" + env + "');\n";
        }

        str = str + "data.addRows(" + rows + ");\n";
        str = str + TestCaseStatus;

       // str = str + "  ]);\n";
        str = str + " var table = new google.visualization.Table(document.getElementById('table_Testcase'));\n";

        str = str + " table.draw(data, {showRowNumber: true, allowHtml: true, width: '100%', height: '100%'});\n}\n";
        return str;
    }
    
    public String LineChartData(String FilePath) throws IOException
            
    {
        
        
     String lineChartdata="";
     BufferedReader in = new BufferedReader(new FileReader(FilePath));
     String str="";
     lineChartdata="var chartData = [";
     while ((str = in.readLine()) != null) 
     {
         String temp[]=str.split(",");
         String run_data="{\n";
          run_data=run_data+"\"Run\":"+"\""+temp[0]+"\","; 
           run_data=run_data+"\"Passed\":"+temp[1]+",\n"; 
           run_data=run_data+"\"Failed\":"+temp[2]+",\n"; 
           run_data=run_data+"\"Skipped\":"+temp[3]+"\n },"; 
           lineChartdata=lineChartdata+ run_data; 
     }
      
               
            lineChartdata=lineChartdata+"];"    ;
            
     
     
       return lineChartdata;
    }   
        

}
