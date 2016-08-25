
package com.hp.test.framework.jelly;

import java.io.File;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;

/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class DeleteDirectoryTag extends TagSupport {
    
    private String path;
    static Logger logger = Logger.getLogger(DeleteDirectoryTag.class);
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of DeleteDirectoryTag function");
        try {
            deleteDir(new File(path));
        } catch (Exception e) {
            logger.error("Exception Occurred while deleting the directory"+ "\n" + e.getMessage());
        }
        logger.info("Completed Execution of DeleteDirectoryTag function");
    }
    
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
    
}
