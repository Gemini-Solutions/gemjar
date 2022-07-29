package com.gemini.generic;

import com.gemini.quartzReporting.GemTestReporter;
import com.gemini.quartzReporting.STATUS;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

    public static void setWebDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }

    public static WebDriver getWebDriver() {
        return driver.get();
    }

    public static void closeDriver() {
        try {
            GemTestReporter.addTestStep("Close Driver", "Driver Close Successful", STATUS.PASS);
            driver.get().close();
        } catch (Exception e) {
            GemTestReporter.addTestStep("Close Driver", "Driver Close Failed", STATUS.FAIL);
            e.printStackTrace();
        }
    }

    public static void initializeBrowser() {
        initializeBrowser("Chrome");
    }

    public static void initializeBrowser(String browserName) {
        try {
            if (browserName == null) {
                WebDriverManager.chromedriver().setup();
                setWebDriver(new ChromeDriver());
            } else {
                String browser = browserName.trim().toLowerCase();
                switch (browser) {
                    case "chrome":
                        initializeChrome();
                        break;
                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        setWebDriver(new FirefoxDriver());

                        break;
                    default:
                        WebDriverManager.chromedriver().setup();
                        setWebDriver(new ChromeDriver());
                        break;
                }
            }
        } catch (Exception e) {
//            GemTestReporter.addTestStep("initialize Browser", "Browser initialization Failed ", STATUS.FAIL);
            e.printStackTrace();
        }
    }

    public static void initializeChrome(ChromeOptions chromeOptions) {
        setWebDriver(new ChromeDriver(chromeOptions));
    }

    public static void initializeChrome() {
        WebDriverManager.chromedriver().setup();
        setWebDriver(new ChromeDriver());
    }

}
