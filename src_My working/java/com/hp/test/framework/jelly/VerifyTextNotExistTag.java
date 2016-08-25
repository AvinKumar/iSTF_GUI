package com.hp.test.framework.jelly;

import java.util.List;

import atu.testng.reports.ATUReports;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

import com.angular.ngwebdriver.ByAngular;
import com.angular.ngwebdriver.ByAngularExactBinding;

/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class VerifyTextNotExistTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(VerifyTextNotExistTag.class);
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
        logger.info("Started Execution of VerifyTextNotExistTag function");
        final WebDriverException exception = new WebDriverException();
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);

        switch (bylocator.toLowerCase()) {
            case "id": {
                logger.info("Executing case id");
                //driver.findElement(By.id(locator)).click();
                try {
                    String act = driver.findElement(By.id(locator)).getText();
                    if (!act.contains(expected)) {
                        System.out.println(expected + " text is NOT in page");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verify Text not exist", expected, act, false);
                    } else {
                        System.out.println(expected + " text is on this page");
                        //throw new WebDriverException(exception.getMessage());
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        //String act = driver.findElement(By.id(locator)).getText();
                        ATUReports.add("Verify Text not exist", expected, act, false);
                        //try {
                        org.testng.Assert.fail("Verify Text not exist" + act + "Expected" + expected);

                    }
                } catch (WebDriverException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }

            case "name": {
                logger.info("Executing case name");
                //driver.findElement(By.id(locator)).click();
                try {
                    String act = driver.findElement(By.name(locator)).getText();
                    if (!act.contains(expected)) {
                        System.out.println(expected + " text is NOT in page");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verify Text not exist", expected, act, false);
                    } else {
                        System.out.println(expected + " text is on this page");
                        //throw new WebDriverException(exception.getMessage());
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        //String act = driver.findElement(By.id(locator)).getText();
                        ATUReports.add("Verify Text not exist", expected, act, false);
                        //try {
                        org.testng.Assert.fail("Verify Text not exist" + act + "Expected" + expected);

                    }
                } catch (WebDriverException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }

            case "xpath": {
                logger.info("Executing case xpath");
                //driver.findElement(By.id(locator)).click();
                try {
                    String act = driver.findElement(By.xpath(locator)).getText();
                    if (!act.contains(expected)) {
                        System.out.println(expected + "text is NOT in page");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verify Text not exist", expected, act, false);
                    } else {
                        System.out.println(expected + " text is on this page");
                        //throw new WebDriverException(exception.getMessage());
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        //String act = driver.findElement(By.id(locator)).getText();
                        ATUReports.add("Verify Text not exist", expected, act, false);
                        //try {
                        org.testng.Assert.fail("Verify Text not exist" + act + "Expected" + expected);

                    }
                } catch (WebDriverException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }

            case "css": {
                logger.info("Executing case css");
                //driver.findElement(By.id(locator)).click();
                try {
                    String act = driver.findElement(By.cssSelector(locator)).getText();
                    if (!act.contains(expected)) {
                        System.out.println(expected + "text is NOT in page");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verify Text not exist", expected, act, false);
                    } else {
                        System.out.println(expected + " text is on this page");
                        //throw new WebDriverException(exception.getMessage());
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        //String act = driver.findElement(By.id(locator)).getText();
                        ATUReports.add("Verify Text not exist", expected, act, false);
                        //try {
                        org.testng.Assert.fail("Verify Text not exist" + act + "Expected" + expected);

                    }
                } catch (WebDriverException e) {
                    System.out.println("Element not found" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }
            
            // Added by: mana, patra
            // Extracting text using angular property exactbinding 
            case "exactbinding": {
                logger.info("Executing case ng-exactbinding");
                //command = "String actual=driver.findElement(By.cssSelector(" + locator + ")).getText()\n";
                String status = "false";
                String act = "";
                
                	try {
                		//logger.info("String not present: " + expected);
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                		
                			List<WebElement> we = driver.findElements(ByAngular.exactBinding(locator));
                			logger.info(we.size());
                			logger.info("Verifying text should not be present: " + expected);
                			for (int i = 0; i < we.size(); i++) {
                				if (!(we.get(i).getText().equals(expected))) {
                					status = "true";
                					act=we.get(i).getText();
                					logger.info("Expected not to be present: " + expected + ",Actual: " + act);
                					ATUReports.add("Text should not be: ", expected, act, false);
                					
                				} else {
                					act=we.get(i).getText();
                					status = "false";
                					logger.info("Expected not to be present: " + expected + ",Actual: " + act);
                					ATUReports.add("Verify Text not exist", expected, act, false);
                					org.testng.Assert.fail("Actual Text: " + act + ", Expected: " + expected + "Should not be present");
                					break;
                				}
                			}
                		//}else{
                			//act=expected;
                			//status="true";
                			//ATUReports.add("Search Results", expected, act, false);
                		//}
                	/*if(status.equals("true")) {
                		logger.info("String not present: " + expected);
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Search Results", "Text Should not be present: " + expected, "Text present: " + act, false);
                	}else{
                		logger.info("String is present (verifytextnotexist failed): " + expected);
                		 ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Search Results", "Text Should not be present: " + expected, "Text present: "+ act, false);
                        org.testng.Assert.fail("Search Results" + act + "Expected" + expected);
                	}*/
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
        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {

        }
        logger.info("Completed Execution of VerifyTextNotFound function");

    }

}
