
package com.hp.test.framework.jelly;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;

/**
 * Wraps a resource bundle to a context variable
 * @author sayedmo
 */
public class PropertyResourceBundleTag extends TagSupport {
    private String resourceBundleLocation;
    private String var;
    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getResourceBundleLocation() {
        return resourceBundleLocation;
    }

    public void setResourceBundleLocation(String resourceBundleLocation) {
        this.resourceBundleLocation = resourceBundleLocation;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        try {
            PropertyResourceBundle resourceBundle=new PropertyResourceBundle(new FileInputStream(resourceBundleLocation));
            this.context.setVariable(var, new ResourceBundleWrapper(resourceBundle));
        } catch (FileNotFoundException e) {
            throw new JellyTagException(e);
        } catch (IOException e) {
            throw new JellyTagException(e);
        }
    }
    
    public static class ResourceBundleWrapper{
        private ResourceBundle resourceBundle;
        
        public ResourceBundleWrapper(ResourceBundle resourceBundle) {
            super();
            this.resourceBundle = resourceBundle;
        }
        
        public String getString(String key, String arg1) {
            return get(key,arg1);
        }
        public String getString(String key, String arg1,String arg2) {
            return get(key,arg1,arg2);
        }
        public String getString(String key, String arg1,String arg2,String arg3) {
            return get(key,arg1,arg2,arg3);
        }
        public String getString(String key, String arg1,String arg2,String arg3,String arg4) {
            return get(key,arg1,arg2,arg3,arg4);
        }
        public String getString(String key, String arg1,String arg2,String arg3,String arg4,String arg5) {
            return get(key,arg1,arg2,arg3,arg4,arg5);
        }
        public String getString(String key, String arg1,String arg2,String arg3,String arg4,String arg5,String arg6) {
            return get(key,arg1,arg2,arg3,arg4,arg5,arg6);
        }
        public String getString(String key, String arg1,String arg2,String arg3,String arg4,String arg5,String arg6,String arg7) {
            return get(key,arg1,arg2,arg3,arg4,arg5,arg6,arg7);
        }
        public String getString(String key, String arg1,String arg2,String arg3,String arg4,String arg5,String arg6,String arg7,String arg8) {
            return get(key,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8);
        }
        public String get(String key,String... args) {
            String value=resourceBundle.getString(key);
            if(value==null) value=key;
            if(args!=null) {
                for(int i=0;i<args.length;i++) {
                    value=value.replace("{"+i+"}", args[i]);
                }
            }
            return value;
        }
    }
}
