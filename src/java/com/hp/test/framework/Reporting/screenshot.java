/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.Reporting;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author yanamalp
 */
public class screenshot {
static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(screenshot.class.getName());
    public static void generatescreenshot(String html_path,String Screenshot_file_path) throws MalformedURLException, FileNotFoundException, IOException {
        try{
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get("file:///"+html_path);//+C:/Users/yanamalp/Desktop/DS_Latest/DSIntegrationTest/ATU%20Reports/Results/Run_3/CurrentRun.html");
        new Select(driver.findElement(By.id("tcFilter"))).selectByVisibleText("Skipped Test Cases"); 
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
// Now you can do whatever you need to do with it, for example copy somewhere
        FileUtils.copyFile(scrFile, new File(Screenshot_file_path));
        driver.quit();
        }
        catch(Exception e)
        {
            log.error("Error in getting Screen shot for the Last run"+e.getMessage());
        }
    }

}
