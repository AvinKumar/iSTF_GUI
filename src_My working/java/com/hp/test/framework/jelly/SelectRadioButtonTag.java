
package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * TODO: Class Description
 * @author dkalra
 */
public class SelectRadioButtonTag extends SeleniumTagSupport {
	private String id; 
	private String value;
        static Logger logger = Logger.getLogger(SelectRadioButtonTag.class);
    
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
	logger.info("Started Execution of SelectRadioButtonTag");
        WebDriver driver=getSelenium();
        String command="";
	List<WebElement> select = driver.findElements(By.id(id));
        for (WebElement element : select) {
            if (element.getAttribute("value").equalsIgnoreCase(value)) {
                element.click();
            }
        }
        command="List<WebElement> select = driver.findElements(By.id("+id+"));\n";
        command="for (WebElement element : select) {\n if (element.getAttribute(\"value\").equalsIgnoreCase("+value+")) {\n";
        command="element.click();}}\n";
                
        logger.info("Completed Execution of SelectRadioButtonTag");
         if(GenerateperformanceTests)
        {
          try {
            StartBrowserTag.JunitTestcase_fw.write(command);
        } catch (IOException ex) {
            System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
        }
        }
    }

}
