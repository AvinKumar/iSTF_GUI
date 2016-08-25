/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.DBUtils;

import com.hp.test.framework.model.testcasegen.ReadProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author $hpedservice
 */
public class DatabaseUtils {

    public static Connection connection = null;
    public static Statement statement;

    static Logger log = Logger.getLogger(DatabaseUtils.class.getName());

    public static void UpdateSingleValue(String TableName,String UpdateValue,String ColumnName,String ConditionColumn,String ConditionValue) throws ClassNotFoundException {
        //BasicConfigurator.configure();
        ReadProperties rp = new ReadProperties();
        System.out.println("Updating column \""+ColumnName +"\" with the Value"+"\""+UpdateValue+"\""+ "WHERE "+ ConditionColumn+ " = " +ConditionValue);
        Class.forName("org.sqlite.JDBC");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + rp.getProperty("DB_Location"));
                    
            statement = connection.createStatement();
            statement.setQueryTimeout(30); 
            String Update_query="UPDATE " +TableName +" set " + ColumnName+"="+"\'"+UpdateValue+"\'"+" WHERE "+ConditionColumn +"=" +Integer.valueOf(ConditionValue); 
            System.out.println("Update Query"+Update_query);// set timeout to 30 sec.
            statement.executeQuery(Update_query);
            
            statement.close();
            connection.close();
            
        } catch (SQLException e) {

            statement=null;
            connection=null;
            
            log.error(e.getMessage());
        }

    }

}
