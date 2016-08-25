package com.hp.test.framework.DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hp.test.framework.model.testcasegen.ReadProperties;

/**
 *
 * @author sayedmo
 */
public class UpdateResultsFromApplication {
    

    Connection connection = null;

    Statement statement = null;
/*
 *  Updates Search results in DB, Table: DM_TESTCASE, Column: RESULTS
    *OR 
     * Update Json Results in the DB, if the updateResults=yes in the properties file.
     * Table: DM_TESTCASE, Column: JSON_RESULT
 */
    public void updateresults(String GID, String ExpectedResults, String ColumnName) throws Exception {
        
    	ReadProperties rp=new com.hp.test.framework.model.testcasegen.ReadProperties();	
        Integer exp_results=0; 
        try {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + rp.getProperty("DB_Location"));
        
        statement = connection.createStatement();

             //update into DB
                    statement.executeUpdate("UPDATE DM_TESTCASE SET " + ColumnName + " = '" + ExpectedResults + "' WHERE GID='" + GID + "'");

                } catch (SQLException e) {
                    
                    System.out.println("*******" + e.getCause());
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }

                }
            }
}