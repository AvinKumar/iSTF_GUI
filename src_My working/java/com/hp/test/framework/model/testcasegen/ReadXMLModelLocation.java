/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.model.testcasegen;

/**
 *
 * @author sayedmo11
 */
//import ReadModelXML.*;
import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;


/**
 *
 * @author sayedmo
 */
public class ReadXMLModelLocation {

    public static String XMLModelLocation;
    public static String DB_Location;

    public void ReadXMLMODELPath() {
        //ReadSourceLocation rs=new ReadSourceLocation();
        ReadProperties rp = new ReadProperties();
        ReadXMLModelLocation.XMLModelLocation = rp.getProperty("XMLModelLocation");
        ReadXMLModelLocation.DB_Location = rp.getProperty("DB_Location");

        Filewalker fw = new Filewalker();
        fw.walk(ReadXMLModelLocation.XMLModelLocation);

    }
}

class Filewalker {

    public long GID = 0;

    static Logger log = Logger.getLogger(Filewalker.class.getName());
    Update_DM_MODELXML_REF ur;

    public void walk(String path) {
        BasicConfigurator.configure();
        ur = new Update_DM_MODELXML_REF();
        try {
            ur.openDatabaseConnection();
        } catch (Exception e) {
            log.error("Error in opening Database Connection" + e.getMessage());
        }
        getpaths(path);
    }

    public void getpaths(String path) {
        log.info("started getting all the templates paths :::" + path);
        File root = new File(path);
        File[] list = root.listFiles();
        if (list == null) {
            return;
        }
        for (File f : list) {
            if (f.isDirectory()) {
                getpaths(f.getAbsolutePath());

            } else {
                System.out.println("File:" + f.getAbsoluteFile());
                String filename = f.getName();
                String[] fullname = filename.split("_");
                String[] productname_temp = fullname[0].split(",");
                String productname=productname_temp[1];
                System.out.println(fullname[0]);
                String featurename = fullname[1];
                String Abpath = f.getAbsolutePath();
                String extension = "";
                //String MasterGid="select GID from DM_MASTERMODELXML_REF where PRODUCTNAME='" + productname + "' and FEATURENAME='" + featurename + "' limit 1";
                int i = filename.lastIndexOf('.');
                if (i >= 0) {
                    extension = filename.substring(i + 1);
                }
                try {
                    GID = GID + 1;
                    ur.UpdateData(GID, Abpath, extension, filename, productname, featurename);
                } catch (Exception e) {
                    log.error("Error in updating paths in DM_MODELXML_REF Table :::" + e.getMessage());
                }
            }
        }
    }

}

class Update_DM_MODELXML_REF {

    Connection connection = null;
    Statement statement;

    static Logger log = Logger.getLogger(Update_DM_MODELXML_REF.class.getName());

    public void openDatabaseConnection() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + ReadXMLModelLocation.DB_Location);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
        } catch (SQLException e) {
            log.error("issue in opening connection" + e.getMessage());
        }

    }

    public void UpdateData(Long GID, String path, String extension, String filename, String productname, String featurename) throws ClassNotFoundException {
        String MasterGid = null;
        System.out.println("*************************************************************************");
        Class.forName("org.sqlite.JDBC");
        try {
            
            String query="select GID from DM_MASTERMODELXML_REF where PRODUCTNAME='" + productname + "' and FEATURENAME='" + featurename + "' limit 1 ";
            ResultSet rs = statement.executeQuery(query);
            
            while (rs.next()) {
                MasterGid=rs.getString("GID");
            }
            String value = "'" + path + "'" + "," + "'" + extension + "'" + "," + "'" + filename + "'" + "," + "'" + productname + "'" + "," + "'" + featurename + "'";
            //statement.executeUpdate("INSERT INTO DM_MODELXML_REF(MODEL_PATH,MODEL_EXTENSION,MODEL_NAME, PRODUCTNAME, FEATURENAME) values(" + value + ") WHERE NOT EXISTS (SELECT MODEL_NAME FROM DM_MODELXML_REF WHERE MODEL_NAME='" + filename + "')");
             statement.executeUpdate("INSERT INTO DM_MODELXML_REF (MODEL_PATH, MODEL_EXTENSION, MODEL_NAME, PRODUCTNAME, FEATURENAME, MASTER_GID)SELECT * FROM (SELECT '" + path + "', '" + extension + "','" + filename + "', '" + productname + "', '" + featurename + "', '"+MasterGid+"') AS tmp WHERE NOT EXISTS (SELECT MODEL_NAME FROM DM_MODELXML_REF WHERE MODEL_NAME = '" + filename + "') LIMIT 1");
        } catch (SQLException e) {
            log.error("Error in inserting data in to DM_MODELXML_REF Table" + e.getMessage());
        }
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
