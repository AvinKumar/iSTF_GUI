package com.hp.test.framework.Log4j;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author sayedmo
 */
public class PropConfigurator {
    static Logger log = Logger.getLogger(PropConfigurator.class.getName());
    public static void configure() {
        PropertyConfigurator.configure("conf/log4j.properties");
        
    }
}
