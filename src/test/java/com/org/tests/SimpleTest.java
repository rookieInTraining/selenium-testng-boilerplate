package com.org.tests;

import com.framework.factory.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class SimpleTest extends DriverFactory {

    @Test(testName = "First Test")
    public void simpleTest() {
        WebDriver driver = getDriver();

        driver.get("https://www.google.com");

        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getTitle());
//        System.out.println(driver.getPageSource());
    }

}






