package com.hp.test.framework.jelly;

import com.angular.ngwebdriver.NgWebDriver;
import com.hp.test.framework.ReadProps.ReadJmeterConfigProps;
import com.hp.test.framework.generatejellytess.ModelProperties;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * TODO: Class Description
 *
 * @author purush
 */
public class StartBrowserTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(StartBrowserTag.class);
    ModelProperties mp = new ModelProperties();
    public static String MainBrowserWindow;
    Random randomGenerator = new Random();
    public static int randomnum;
    public static File JunitTestcase;
    public static FileWriter JunitTestcase_fw;
    public static boolean GenerateperformanceTests;

    public StartBrowserTag() {

        ReadJmeterConfigProps readjmeterproperties = new ReadJmeterConfigProps();
        String locationofwebdriverscript = readjmeterproperties.getProperty("location.java.files");
        GenerateperformanceTests = (readjmeterproperties.getProperty("genrate.performace.test").toLowerCase().contains("yes")) ? true : false;
        if (GenerateperformanceTests) {
            try {
                randomnum = randomGenerator.nextInt(10000);
                JunitTestcase = new File(locationofwebdriverscript + "/AutoGenarateTest" + randomnum + ".java");
                JunitTestcase.createNewFile();
                JunitTestcase_fw = new FileWriter(JunitTestcase);
                JunitTestcase_fw.write(" import org.openqa.selenium.WebDriver;\n");
                JunitTestcase_fw.write("import org.openqa.selenium.firefox.FirefoxDriver;\n ");
                JunitTestcase_fw.write("import java.util.concurrent.TimeUnit;\n ");
                JunitTestcase_fw.write("import org.openqa.selenium.By;\n ");
                JunitTestcase_fw.write("import org.openqa.selenium.support.ui.Select;\n ");
                JunitTestcase_fw.write("import org.junit.Test;\n ");
                JunitTestcase_fw.write("import junit.framework.TestCase;\n ");
                 JunitTestcase_fw.write("import org.openqa.selenium.NoSuchElementException;\n ");
                 JunitTestcase_fw.write("import org.openqa.selenium.WebElement;\n ");
                  JunitTestcase_fw.write("import java.util.List;\n ");
                 
                
                


            } catch (IOException ex) {
                System.out.println("Exception writing header to junit testcase"+ "\n" + ex.getMessage());
            }
        }

    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {

        WebDriver driver = null;

        try {
            if (mp.getProperty("BROWSER").equalsIgnoreCase("Firefox")) {
                driver = new FirefoxDriver();
                NgWebDriver ngWebDriver = new NgWebDriver((JavascriptExecutor) driver); // By: mana, patra
            } else if (mp.getProperty("BROWSER").equalsIgnoreCase("chrome")) {
                System.setProperty("webdriver.chrome.driver", "conf/chromedriver.exe");
                driver = new ChromeDriver();
                NgWebDriver ngWebDriver = new NgWebDriver((JavascriptExecutor) driver); // By: mana, patra

            } else if (mp.getProperty("BROWSER").equalsIgnoreCase("IE")) {
                System.setProperty("webdriver.ie.driver", "conf/IEDriverServer.exe");
                DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
                caps.setCapability("ignoreZoomSetting", true);
                driver = new InternetExplorerDriver(caps);
                //driver = new InternetExplorerDriver();

            }
            MainBrowserWindow = driver.getWindowHandle().toString();
            driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(90,TimeUnit.SECONDS);
            driver.manage().window().maximize();
            if (url != null) {
                driver.get(url);
            } else {
                driver.get(mp.getProperty("URL"));
            }
            setSelenium((WebDriver) driver);

            if (GenerateperformanceTests) {
                JunitTestcase_fw.write("public class AutoGenarateTest" + randomnum + " extends TestCase {");
                JunitTestcase_fw.write("\n");
                JunitTestcase_fw.write(" WebDriver driver;\n\n");
                JunitTestcase_fw.write("@Test\n");
                JunitTestcase_fw.write(" public void test(){\n");
                JunitTestcase_fw.write("\n");
                JunitTestcase_fw.write("try {");
                JunitTestcase_fw.write("\n");
                JunitTestcase_fw.write(" driver = new FirefoxDriver(); ");
                JunitTestcase_fw.write("\n");
                JunitTestcase_fw.write(" driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);");
                JunitTestcase_fw.write("\n");
                JunitTestcase_fw.write(" driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);");
                JunitTestcase_fw.write("\n");
                JunitTestcase_fw.write("driver.manage().window().maximize();");
                JunitTestcase_fw.write("\n");
                JunitTestcase_fw.write("driver.get(\"" + mp.getProperty("URL") + "\");");
                JunitTestcase_fw.write("\n");

                JunitTestcase_fw.write("\n");

            }

        } catch (WebDriverException e) {
            System.out.println(e.getMessage());
            logger.error("Expection occurred in StartBrowser"+ "\n" + e.getMessage());

        } catch (IOException e) {
            logger.error("Exception in writing data to java file"+ "\n" + e.getMessage());
        }

    }

}
