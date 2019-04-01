package com.framework.factory;

import com.framework.WebDriverThread;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class DriverFactory {

    private static Logger logger = Logger.getLogger(Class.class.getName());
    private static List<WebDriverThread> webDriverThreadPool = Collections.synchronizedList(new ArrayList<>());
    private static ThreadLocal<WebDriverThread> driverThread;

    @BeforeMethod
    public static void instantiateDriverObject() {
        logger.info("Initialization done by thread : " + Thread.currentThread().getName());
        driverThread = new ThreadLocal<WebDriverThread>() {
            @Override
            protected WebDriverThread initialValue() {
                WebDriverThread webDriverThread = new WebDriverThread();
                webDriverThreadPool.add(webDriverThread);
                Reporter.log("Added webdriver : " + webDriverThreadPool.toString(),true);
                return webDriverThread;
            }
        };
    }

    public static WebDriver getDriver() throws WebDriverException {
        logger.info("Fetching driver");
        return driverThread.get().getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public static void clearCookies(ITestResult result) throws WebDriverException {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.info("Test Failed, taking a screenshot");
            try {
                driverThread.get().takeScreenshot();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getDriver().manage().deleteAllCookies();
        getDriver().quit();
    }

    @AfterSuite
    public static void closeDriverObjects() {
        logger.info("Closing driver objects for pool : " + webDriverThreadPool);
        for (WebDriverThread webDriverThread : webDriverThreadPool) {
            webDriverThread.quitDriver();
            webDriverThread.stopDriverServices();
        }
    }
}
