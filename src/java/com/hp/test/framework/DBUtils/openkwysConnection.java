package com.hp.test.framework.DBUtils;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.hp.test.framework.model.testcasegen.ReadProperties;

public class openkwysConnection {
    

public Connection connection_kwys = null;
        public Statement statement_kwys;
        private ReadProperties rp = null;        
        static Logger log = Logger.getLogger(openkwysConnection.class.getName());

        public void openDatabaseConnection(final ReadProperties readprop)throws ClassNotFoundException
        {
            rp = readprop;
            log.info( "Using user supplied read properties.");
            this.openDatabaseConnection();
        }
        
       public void openDatabaseConnection()throws ClassNotFoundException
       {
          //BasicConfigurator.configure();
           if( rp==null ) rp = new com.hp.test.framework.model.testcasegen.ReadProperties();
          log.info( " opening Database Connection");
          Class.forName("org.sqlite.JDBC");
           try
             {
                  connection_kwys = DriverManager.getConnection("jdbc:sqlite:"+rp.getProperty("DB_Location"));
                  statement_kwys = connection_kwys.createStatement();
                  statement_kwys.setQueryTimeout(30);  // set timeout to 30 sec.
            }
            catch(SQLException e)
            {
                log.error(" Issue in Opening database Connection" );
                log.error(e.getMessage());
            }
  
       
       
       }
       
}