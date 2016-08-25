package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class PressKeyTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(PressKeyTag.class);
    private String id;
    private String value;

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
        logger.info("Started Execution of PressKeyTag");
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        //System.out.println("User name:" + temp_ar[1] + "password:" + value + "ID" + id);
        String Command = "";
        switch (bylocator.toLowerCase()) {
            case "id": {
                logger.info("Executing case id");
                try {
                    driver.findElement(By.id(locator)).sendKeys(Keys.valueOf(value));
                    Command = "driver.findElement(By.id(" + locator + ")).sendKeys(Keys.valueOf" + value + "));\n";
                } catch (Exception e) {
                    logger.error("Exception occurred in Pressky Function"+ "\n" + e.getMessage());
                }
                break;
            }

            case "name": {
                logger.info("Excecuting case name");
                try {
                    driver.findElement(By.name(locator)).sendKeys(Keys.valueOf(value));
                    Command = "driver.findElement(By.name(" + locator + ")).sendKeys(Keys.valueOf" + value + "));\n";
                } catch (Exception e) {
                    logger.error("Exception occurred in Pressky Function"+ "\n" + e.getMessage());
                }
                break;
            }

            case "xpath": {
                logger.info("Executing case xpath");
                try {
                    driver.findElement(By.xpath(locator)).sendKeys(Keys.valueOf(value));
                    Command = "driver.findElement(By.xpath(" + locator + ")).sendKeys(Keys.valueOf" + value + "));\n";
                } catch (Exception e) {
                    logger.error("Exception occurred in Pressky Function"+ "\n" + e.getMessage());
                }
                break;
            }
            case "css": {
                logger.info("Executing case cssSelector");
                try {
                    driver.findElement(By.cssSelector(locator)).sendKeys(Keys.valueOf(value));
                    Command = "driver.findElement(By.cssSelector(" + locator + ")).sendKeys(Keys.valueOf" + value + "));\n";
                } catch (Exception e) {
                    logger.error("Exception occurred in Pressky Function"+ "\n" + e.getMessage());
                }
                break;
            }
        }
        logger.info("End of Execution of PressKeyTag");

        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(Command);
            } catch (IOException ex) {
                System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }
    }

}
