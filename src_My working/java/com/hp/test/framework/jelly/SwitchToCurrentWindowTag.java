package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Description: Use this class if you want to switch from a frame to an iframe(s) within it. 
 * Ex: Can be used to switch from FrameA to an Overlay Pop-Up which is an iFrame inside it.
 *
 * @author avin
 */
public class SwitchToCurrentWindowTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(SwitchToFrameTag.class);
    private String id;
    

    
    public String getid() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of SwitchToCurrentWindowTag");
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");
        
       // String currentWindow = driver.CurrentWindowHandle;
        
        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        try {
            driver.switchTo().frame(locator);
        } catch (Exception e) {
            logger.error("Exception while switching to Current Window"+ "\n" + e.getMessage());
        }
        String command = "driver.switchTo().frame(" + locator + ");\n";
        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(command);
            } catch (IOException ex) {
                System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }
        
        logger.info("End of Execution of SwitchToCurrentWindowTag");
    }
    
}
