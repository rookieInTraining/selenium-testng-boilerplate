package com.framework.drivers;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class Chrome implements Browsers {

    private ChromeDriverService chromeDriverService;

    @Override
    public RemoteWebDriver getWebDriverObject(MutableCapabilities desiredCapabilities) throws MalformedURLException {
        System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver");
        startDriverService();
        if (runOnGrid()) {
            return new RemoteWebDriver(new URL(HUB_URL), desiredCapabilities);
        } else {
            return new RemoteWebDriver(chromeDriverService.getUrl(), desiredCapabilities);
        }
    }

    public DesiredCapabilities getDesiredCapabilities() {
        HashMap<String,String> chromePreferences = new HashMap<>();
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        chromePreferences.put("profile.password_manager_enabled", "false");

        capabilities.setCapability("chrome.switches", Collections.singletonList("--no-default=browser-check"));
        capabilities.setCapability("chrome.prefs",chromePreferences);

        return capabilities;
    }

    public ChromeOptions browserOptions(DesiredCapabilities desiredCapabilities) {
        final ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.setHeadless(Boolean.valueOf(System.getProperty("headless")));
        chromeOptions.addArguments("window-size=1080x720");
        chromeOptions.merge(desiredCapabilities);

        return chromeOptions;
    }

    @Override
    public void startDriverService() {
        if (Objects.isNull(chromeDriverService)) {
            try {
                chromeDriverService = new ChromeDriverService.Builder()
                        .usingDriverExecutable(new File("src/main/resources/chromedriver"))
                        .usingAnyFreePort()
                        .build();
                chromeDriverService.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stopDriverService() {
        if (Objects.isNull(chromeDriverService)) {
            throw new NullPointerException("No Driver Service running at the moment");
        }
        chromeDriverService.stop();
    }
}
