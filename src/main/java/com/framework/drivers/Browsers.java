package com.framework.drivers;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.util.Objects;

public interface Browsers {

    String HUB_URL = "http://localhost:4444/wd/hub";

    RemoteWebDriver getWebDriverObject(MutableCapabilities desiredCapabilities) throws MalformedURLException;

    DesiredCapabilities getDesiredCapabilities();

    Object browserOptions(DesiredCapabilities desiredCapabilities);

    void startDriverService();

    void stopDriverService();

    default boolean runOnGrid() {
        if (Objects.nonNull(System.getProperty("runOnGrid")) && System.getProperty("runOnGrid").equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

}