package com.hp.test.framework.jelly;

import com.hp.test.framework.model.testcasegen.*;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;

import java.util.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * @author frank
 */
public class UserFunctionCallTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(UserFunctionCallTag.class);
    private String id;
    private String value;
    private String function;
    
    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of UserFunctionCall");
          
        ArrayList<String> inputStr = new ArrayList<String>(Arrays.asList(value.split(" ")));
        
        ReadMasterModelLocation ud=new ReadMasterModelLocation();
        
        ReadXMLModelLocation t=new ReadXMLModelLocation();
        
        t.ReadXMLMODELPath();
        
        
        String aClass = inputStr.get(0);
        String aMethod = inputStr.get(1);
        
        Class AnotherClass[] = new Class[] {String.class, String.class};
        Object anotherObject[] = new Object[]{inputStr.toString(), new String("To be used")};
     
        try {
            // System.out.println("Try invoke UserFunctionCall");
            invoke( aClass,
                    aMethod,
                    AnotherClass,
                    anotherObject);
            } 
            catch (Exception ex) {
                java.util.logging.Logger.getLogger(UserFunctionCallTag.class.getName()).log(Level.SEVERE, "Exception in user function", ex);
            }
        logger.info("End of Execution of UserFunctionCall");        
    }
    
    public static void invoke(String aClass, String aMethod, Class[] params, Object[] args) throws Exception {
        Class cls = Class.forName(aClass);
        Method mtd = cls.getDeclaredMethod(aMethod, params);
        Object ist = cls.newInstance();
        Object ret = mtd.invoke(ist, args);
    }
}
