

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hp.test.framework.Reporting;

import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import java.util.Date;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;




/**
 *
 * @author PYanamalamanda
 */
public class GetdatafromDBS {

        Connection connection_KWYH = null;
        Statement statement_KWYH;
        Statement statement_KWYH1;
        PreparedStatement prestatement_KWYH;

        Connection connection_KWYG = null;
        Statement statement_KWYG;
        PreparedStatement prestatement_KWYG;


        public  ArrayList<String> Metadata_List = new ArrayList<String>();
        public  ArrayList<String> Keywords_List = new ArrayList<String>();
        
        static Logger log = Logger.getLogger(GetdatafromDBS.class.getName());

       public void openDatabase_KWYH(String databasename)throws ClassNotFoundException
       {
          //BasicConfigurator.configure();
          log.info( " opening Database Connection");
          
          Class.forName("org.sqlite.JDBC");
           try
             {
                //  connection_KWYH = DriverManager.getConnection("jdbc:sqlite:C:\\"+ "KWYH" +".dat");
                  connection_KWYH = DriverManager.getConnection("jdbc:sqlite:"+databasename);
                  statement_KWYH = connection_KWYH.createStatement();
                  statement_KWYH1 = connection_KWYH.createStatement();
                  statement_KWYH.setQueryTimeout(30);  // set timeout to 30 sec.
            }
            catch(SQLException e)
            {
                log.error(" Issue in Opening database Connection" );
                log.error(e.getMessage());
            }
       }


        public void openDatabase_KWYG(String databasename)throws ClassNotFoundException
       {
          //BasicConfigurator.configure();
       //   log.info( " opening Database Connection");
          // log.info( " opening Database Connection");
          
          Class.forName("org.sqlite.JDBC");
           try
             {
                //  connection_KWYG = DriverManager.getConnection("jdbc:sqlite:C:\\"+ "KWYG" +".dat");
                    connection_KWYG = DriverManager.getConnection("jdbc:sqlite:"+databasename);
                  statement_KWYG = connection_KWYG.createStatement();
                  statement_KWYG.setQueryTimeout(30);  // set timeout to 30 sec.
            }
            catch(SQLException e)
            {
                log.error(" Issue in Opening database Connection" );
                log.error(e.getMessage());
            }
       }


  

}