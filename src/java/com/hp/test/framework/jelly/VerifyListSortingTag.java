package com.hp.test.framework.jelly;

import atu.testng.reports.ATUReports;


import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.angular.ngwebdriver.ByAngular;
import com.angular.ngwebdriver.NgWebDriver;

/**
 * TODO: Class Description
 *
 * @author mana, patra
 * Usage: <sel:verifylistsorting id="exactbinding=binding_name" expected="values in sorting order separated by "|" />
 */

public class VerifyListSortingTag extends SeleniumTagSupport {
	
	static Logger logger = Logger.getLogger(VerifyListSortingTag.class);
    private SoftAssert softAssert = new SoftAssert();

    private String id, expected;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getexpected() {
        return expected;
    }

    public void setexpected(String expected) {
        this.expected = expected;
    }
    
    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of VerifyListSortingTag function");
        final WebDriverException exception = new WebDriverException();
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");
        String actual = "";
        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);

        switch (bylocator.toLowerCase()) {
            case "exactbinding": {
            	logger.info("Executing case binding/exactbinding");
                String status = "false";
                String act = "";
                ArrayList aList= new ArrayList(Arrays.asList(expected.split("\\|")));
                //System.out.println("Array of Names: " + aList);
                
                	try {
                		
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                		//new NgWebDriver((JavascriptExecutor) driver).waitForAngularRequestsToFinish();
                		List<WebElement> we = driver.findElements(ByAngular.exactBinding(locator));
                		logger.info(we.size());
                		logger.info("Verifying list: " + expected);
                	for (int i = 0; i < we.size(); i++) {
                			if (we.get(i).getText().equals(aList.get(i))) {
                				act = we.get(i).getText();
                                ATUReports.add("Search Results at index: " + i, aList.get(i).toString(), act, false);
                				status = "true";
                				logger.info("Index: " + i + ", Expected Value: " + aList.get(i) + " Actual Value: " + act);
                				
                			} else {
                				act = we.get(i).getText();
                				logger.info("Index: " + i + ", Expected Value: " + aList.get(i) + "Actual Value: " + act);
                				ATUReports.add("Search Results at index: " + i, aList.get(i).toString(), act, false);
                                org.testng.Assert.fail("Search Results at index "  + i + " Actual: " + act + ", Expected: " + aList.get(i).toString());
                				//arr.add(we.get(i).getText());
                			}
                	}
                	}catch (WebDriverException e) {
                		//throw new WebDriverException(e.getMessage());
                        System.out.println("Element not found" + e.getMessage());
                        ATUReports.add(id + "Not found", true);
                        //org.testng.Assert.fail("Exception Occured hence failed");
                        throw new WebDriverException(e.getMessage());
                	}
                break;
            }

}
}
}
