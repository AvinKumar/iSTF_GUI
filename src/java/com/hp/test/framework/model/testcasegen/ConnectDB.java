package com.hp.test.framework.model.testcasegen;


import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.log4j.Logger;

/**
 *
 * @author sayedmo
 */
public class ConnectDB {

   private Connection connection = null;
    private static Logger log = Logger.getLogger(ConnectDB.class);

    public static Connection ConnectToolDB() {
        final ReadProperties rp = new ReadProperties();
        final String dbPath = rp.getProperty("DB_Location");
        final String connectString = String.format("jdbc:sqlite:%s", dbPath);
        log.info("Openning connection to db at " + dbPath);
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(connectString);
        } catch (Exception e) {
            log.fatal("Couldn't open/connect to db found in " + dbPath, e);
            return null;
        }

    }

}