package com.hp.test.framework.DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class postgresDBConnection {
	Connection connection = null;
	public String dbConnect(String casename, String pgserver, String postgresDB, String pgusername, String pgpwd) throws  Exception{
	 // public String dbConnect(String casename) throws  Exception{	   
		  Statement stmt = null;
		  String sql = null;
		  String ret = null;
      try {
         Class.forName("org.postgresql.Driver");
         //connection = DriverManager.getConnection(dbconnect_string, db_id, db_pwd);
         connection = DriverManager.getConnection("jdbc:postgresql://"+pgserver+":5432/"+postgresDB, pgusername, pgpwd);
         System.out.println("Postgres database Opened successfully");
         
         stmt = connection.createStatement();
         sql = "SELECT ID FROM ASPEN.ENTITY WHERE NAME = \'"+casename+"\'";

         ResultSet rs = stmt.executeQuery(sql);
         while (rs.next()) {
        	 ret = rs.getString(1);
             //System.out.println("The query result is : " +ret);
         }
         stmt.close();
         connection.close();         
         
      } catch (Exception e) {
         e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
      }
      return ret;
   }
}