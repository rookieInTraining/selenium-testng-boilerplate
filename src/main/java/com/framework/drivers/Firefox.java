package com.framework.drivers;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class Firefox implements Browsers {

    private GeckoDriverService firefoxDriverService;

    @Override
    public RemoteWebDriver getWebDriverObject(MutableCapabilities desiredCapabilities) throws MalformedURLException {
        System.setProperty("webdriver.gecko.driver","src/main/resources/geckodriver");
        startDriverService();
        if (runOnGrid()) {
            return new RemoteWebDriver(new URL(HUB_URL), desiredCapabilities);
        } else {
            return new RemoteWebDriver(firefoxDriverService.getUrl(), desiredCapabilities);
        }
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        return DesiredCapabilities.firefox();
    }

    @Override
    public Object browserOptions(DesiredCapabilities desiredCapabilities) {
        final FirefoxOptions firefoxOptions = new FirefoxOptions();

        firefoxOptions.setHeadless(Boolean.valueOf(System.getProperty("headless")));
        firefoxOptions.addArguments("window-size=1080x720");
        firefoxOptions.merge(desiredCapabilities);

        return firefoxOptions;
    }

    @Override
    public void startDriverService() {
        if (Objects.isNull(firefoxDriverService)) {
            try {
                firefoxDriverService = new GeckoDriverService.Builder()
                        .usingDriverExecutable(new File("src/main/resources/geckodriver"))
                        .usingAnyFreePort()
                        .build();
                firefoxDriverService.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stopDriverService() {
        if (Objects.isNull(firefoxDriverService)) {
            throw new NullPointerException("No Driver Service running at the moment");
        }
        firefoxDriverService.stop();
    }
}
