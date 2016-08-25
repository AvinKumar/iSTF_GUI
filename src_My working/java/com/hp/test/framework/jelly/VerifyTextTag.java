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
 * @author sayedmo
 * Usage: <sel:verifytext id="css=cssname" expected="value to be verified" />
 */
public class VerifyTextTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(VerifyTextTag.class);
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
        logger.info("Started Execution of VerifyTextTag function");
        final WebDriverException exception = new WebDriverException();
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");
        String command = "";
        String actual = "";
        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);

        switch (bylocator.toLowerCase()) {
            case "id": {
                logger.info("Executing case id");
                command = "String actual=driver.findElement(By.id(" + locator + ")).getText()\n";

                try {
                    if (driver.findElement(By.id(locator)).getText().contains(expected)) {
                        System.out.println(expected + " text is on this page");
                        ATUReports.setAuthorInfo("Supervisor Automation Team", "DATE()", "1.0");
                        String act = driver.findElement(By.id(locator)).getText();
                        ATUReports.add("Verifying the Text :\" "+driver.findElement(By.id(locator)).getText()+"\"", expected, act, false);
                    } else {
                        System.out.println(expected + " text is NOT on this page");
                        ATUReports.setAuthorInfo("Supervisor Automation Team", "DATE()", "1.0");
                        String act = driver.findElement(By.id(locator)).getText();
                        ATUReports.add("Verifying whether the Text :" +"\""+ expected +"\""+"is present? but Actual text is" +"\""+act+"\"",false);
                        //try {
                        org.testng.Assert.fail("Verifying whether the Text :" +"\""+ expected +"\""+"is present? but Actual text is" +"\""+act+"\"");

                    }
                } catch (WebDriverException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }

            case "name": {
                logger.info("Executing case name");
                command = "String actual=driver.findElement(By.name(" + locator + ")).getText()\n";
                try {
                    if (driver.findElement(By.name(locator)).getText().contains(expected)) {
                        System.out.println(expected + " text is on this page");
                        ATUReports.setAuthorInfo("Supervisor Automation Team", "DATE()", "1.0");
                        String act = driver.findElement(By.name(locator)).getText();
                        ATUReports.add("Verifying the Text :\" "+driver.findElement(By.name(locator)).getText()+"\"", expected, act, false);
                    } else {
                        System.out.println(expected + " text is NOT on this page");
                        ATUReports.setAuthorInfo("Supervisor Automation Team", "DATE()", "1.0");
                        String act = driver.findElement(By.name(locator)).getText();
                        ATUReports.add("Verifying whether the Text :" +"\""+ expected +"\""+"is present? but Actual text is" +"\""+act+"\"",false);
                        org.testng.Assert.fail("Verifying whether the Text :" +"\""+ expected +"\""+"is present? but Actual text is" +"\""+act+"\"");

                    }
                } catch (WebDriverException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                    //org.testng.Assert.fail("Exception Occured hence failed");
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }

            case "xpath": {
                logger.info("Executing case xpath");
                command = "String actual=driver.findElement(By.xpath(" + locator + ")).getText()\n";
                     logger.info("XPATH text: " + driver.findElement(By.xpath(locator)).getText());
                try {
                    if (driver.findElement(By.xpath(locator)).getText().contains(expected)) {
                        ATUReports.setAuthorInfo("Supervisor Automation Team", "DATE()", "1.0");
                        String act = driver.findElement(By.xpath(locator)).getText();
                        ATUReports.add("Verifying the Text :\" "+driver.findElement(By.xpath(locator)).getText()+"\"", expected, act, false);
                    } else {
                        ATUReports.setAuthorInfo("Supervisor Automation Team", "DATE()", "1.0");
                        String act = driver.findElement(By.xpath(locator)).getText();
                        ATUReports.add("Verifying whether the Text :" +"\""+ expected +"\""+"is present? but Actual text is" +"\""+act+"\"",false);
                        org.testng.Assert.fail("Verifying whether the Text :" +"\""+ expected +"\""+"is present? but Actual text is" +"\""+act+"\"");
                    }
                } catch (WebDriverException e) {
                    //throw new WebDriverException(e.getMessage());
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                    ATUReports.add(id + "Not found", true);
                    //org.testng.Assert.fail("Exception Occured hence failed");
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }

            case "css": {
                logger.info("Executing case css");
                command = "String actual=driver.findElement(By.cssSelector(" + locator + ")).getText()\n";
                logger.info("CSS text: " + driver.findElement(By.cssSelector(locator)).getText());
                try {
                    if (driver.findElement(By.cssSelector(locator)).getText().contains(expected)) {
                        ATUReports.setAuthorInfo("Supervisor Automation Team", "DATE()", "1.0");
                        String act = driver.findElement(By.cssSelector(locator)).getText();
                        ATUReports.add("Verifying the Text :\" "+driver.findElement(By.cssSelector(locator)).getText()+"\"", expected, act, false);
                    } else {
                        ATUReports.setAuthorInfo("Supervisor Automation Team", "DATE()", "1.0");
                        String act = driver.findElement(By.cssSelector(locator)).getText();
                        ATUReports.add("Verifying whether the Text :" +"\""+ expected +"\""+"is present? but Actual text is" +"\""+act+"\"",false);
                        org.testng.Assert.fail("Verifying whether the Text :" +"\""+ expected +"\""+"is present? but Actual text is" +"\""+act+"\"");
                    }
                } catch (WebDriverException e) {
                    //throw new WebDriverException(e.getMessage());
                    System.out.println("Element not found" + e.getMessage());
                    ATUReports.add(id + "Not found", true);
                    //org.testng.Assert.fail("Exception Occured hence failed");
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }
            
            // Added by: mana,patra
            // Verifying text based on its repeater values
            
            case "repeater": {
                logger.info("Executing case ng-repeater");
                //command = "String actual=driver.findElement(By.cssSelector(" + locator + ")).getText()\n";
                String status = "false";
                String act = "";
                ArrayList arr = new ArrayList();
                
                	try {
                		
                		List<WebElement> we = driver.findElements(ByAngular.repeater(locator));
                        System.out.println("Repeater Size: " + we.size());
                        System.out.println("Verifying text: " + expected);
                        for (int i = 0; i <= we.size(); i++) {
                        	System.out.println(we.get(i).getText());
                			if (we.get(i).getText().contains(expected)) {
                				act = we.get(i).getText();
                                ATUReports.add("Verify Text", expected, act, false);
                				status = "true";
                				//act=we.get(i).getText();
                				break;
                			} else {
                				arr.add(we.get(i).getText());
                			}
                	}
                	if(status == "false") {
                		logger.info("String does not Matching: " + expected);
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verify Text", expected, String.join(",", arr), false);
                        org.testng.Assert.fail("Verify Text Actual: " + act + ", Expected: " + expected);
                	}/*else{
                		 ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Search Results", "Should match: " + expected, "Matching: " + act, false);
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
            
             // Added by: mana,patra
            // Verifying text based on its exact-binding values
            
            case "binding":
            case "exactbinding": {
                logger.info("Executing case ng-exactbinding");
                
                //String[] expectedOptions = expected.split("|");
                //command = "String actual=driver.findElement(By.cssSelector(" + locator + ")).getText()\n";
                String status = "false";
                String act = "";
                ArrayList arr = new ArrayList();
                
                	try {
                		logger.info("String Matching: " + expected);
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                		//new NgWebDriver((JavascriptExecutor) driver).waitForAngularRequestsToFinish();
                		List<WebElement> we = driver.findElements(ByAngular.exactBinding(locator));
                		logger.info("Binding Size: " + we.size());
                		logger.info("Verifying text: " + expected);
                	for (int i = 0; i <= we.size(); i++) {
                			if (we.get(i).getText().contains(expected)) {
                				act = we.get(i).getText();
                                ATUReports.add("Verify Text", expected, act, false);
                				status = "true";
                				//act=we.get(i).getText();
                				break;
                			} else {
                				arr.add(we.get(i).getText());
                			}
                	}
                	if(status == "false") {
                		logger.info("String does not Matching: " + expected);
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verify Text", expected, String.join(",", arr), false);
                        org.testng.Assert.fail("Verify Text Actual: " + act + ", Expected: " + expected);
                	}/*else{
                		 ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Search Results", "Should match: " + expected, "Matching: " + act, false);
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

        command = command + "org.junit.Assert.assertEquals(" + expected + ",actual);\n";
        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(command);
            } catch (IOException ex) {
                System.out.println("Exception in Writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }

        logger.info("Completed Execution of VerifyTextTag function");
    }

}
