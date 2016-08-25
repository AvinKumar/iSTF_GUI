package com.hp.test.framework.jelly;

import org.apache.commons.jelly.TagSupport;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * The base tag for all tags that need selenium support
 *
 * @author sayedmo
 */
public abstract class SeleniumTagSupport extends TagSupport {

    public static final String KEY_SELENIUM = "selenium";

    public void setSelenium(WebDriver selenium) {
        context.setVariable(KEY_SELENIUM, selenium);
    }

    public WebDriver getSelenium() {
        return (WebDriver) context.getVariable(KEY_SELENIUM);
    }

    public void type(String id, String description) {
        if (description != null) {
            getSelenium().findElement(By.id(id)).sendKeys(description);
        }
    }

}
