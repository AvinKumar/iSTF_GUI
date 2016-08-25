package com.hp.test.framework.jelly;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class LoginTag extends SeleniumTagSupport {
    
    static Logger logger = Logger.getLogger(LoginTag.class);
    private String userName;
    private String password;
    private String confirmPassword;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void doTag(XMLOutput output) throws MissingAttributeException, JellyTagException {
        WebDriver selenium = getSelenium();
        logger.info("Started Execution of LoginTag");
        if (getUserName() != null) {
            selenium.findElement(By.id("userName")).sendKeys(userName);
            selenium.findElement(By.id("password")).sendKeys(password);

            selenium.findElement(By.id("actualLoginButton")).click();
         logger.info("Completed Execution of LoginTag");
        }
    }

}
