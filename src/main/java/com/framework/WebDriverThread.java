package com.framework;

import com.framework.drivers.Browsers;
import com.framework.drivers.Chrome;
import com.framework.drivers.Firefox;
import com.framework.enums.DriverType;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;
import java.util.logging.Logger;

public class WebDriverThread {

    private static Logger logger = Logger.getLogger(Class.class.getName());

    private RemoteWebDriver webDriver;
    private DriverType selectedDriverType;

    private Browsers browsers;

    private DriverType defaultDriverType = DriverType.CHROME;

    private final String selectedBrowser = System.getProperty("selectedBrowser", defaultDriverType.toString()).toUpperCase();
    private final String operatingSystem = System.getProperty("os.name");
    private final String systemArchitecture = System.getProperty("os.arch");
    private final String operatingSystemVersion = System.getProperty("os.version");

    public WebDriver getDriver() throws WebDriverException {
        if (null == webDriver) {
            selectedDriverType = determineEffectiveDriverType();
            instantiateWebDriver();
        }
        return webDriver;
    }

    private DriverType determineEffectiveDriverType() {
        DriverType driverType = defaultDriverType;

        try {
            driverType = DriverType.valueOf(selectedBrowser);
        } catch (IllegalArgumentException ignored) {
            logger.severe("Unknown driver sepcified, defaulting to '" + driverType + "'...");
        } catch (NullPointerException ignored) {
            logger.severe("No driver specified, defaulting to '" + driverType + "'...");
        }

        return driverType;
    }

    private void instantiateWebDriver() {
        DesiredCapabilities capabilities;
        MutableCapabilities driverOptions;

        logger.info("Current Operating System : " + operatingSystem);
        logger.info("Current Operating System Version : " + operatingSystemVersion);
        logger.info("Current Architecture : " + systemArchitecture);
        logger.info("Current Browser Selection : " + selectedDriverType);

        try {
            switch (selectedDriverType) {
                case CHROME:
                    browsers = new Chrome();
                    capabilities = browsers.getDesiredCapabilities();
                    driverOptions = (ChromeOptions) browsers.browserOptions(capabilities);
                    webDriver = browsers.getWebDriverObject(driverOptions);
                    break;
                case FIREFOX:
                    browsers = new Firefox();
                    capabilities = browsers.getDesiredCapabilities();
                    driverOptions = (FirefoxOptions) browsers.browserOptions(capabilities);
                    webDriver = browsers.getWebDriverObject(driverOptions);
                    break;
                default:
                    throw new WebDriverException("WebDriver selected has missing binary or is not found");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void takeScreenshot() throws IOException {
        File src = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);

        // now copy the  screenshot to desired location using copyFile //method
        File screenShotName = new File(System.getProperty("user.dir") + "/build/screenshot/screenShot-" + UUID.randomUUID() + ".png");
        FileUtils.copyFile(src, screenShotName);

        //Log Into TestNG
        Reporter.log("<a href='"+ screenShotName.getAbsolutePath() + "'> <img src='"+ screenShotName.getAbsolutePath() + "' height='100' width='100'/> </a>");
    }

    public void quitDriver() {
        if(null != webDriver) {
            webDriver.quit();
            webDriver = null;
        }
    }

    public void stopDriverServices() {
        browsers.stopDriverService();
    }

}
