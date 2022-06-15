package com.gemini.generic;

import com.gemini.quartzReporting.GemTestReporter;
import com.gemini.quartzReporting.STATUS;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DriverAction {

    /**
     * lauch URL launches any url passed in parameter
     *
     * @param url
     * @param report
     */
    public static void launchUrl(String url, boolean report) {
        try {
            DriverManager.getWebDriver().get(url);
            if (report) {
                GemTestReporter.addTestStep("Launch Url", "Url ~" + url, STATUS.PASS, DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            if (report) {
                GemTestReporter.addTestStep("Launch Url", "Launch Url Failed <BR>Url ~" + url, STATUS.FAIL,
                        DriverAction.takeSnapShot());
            }
            e.printStackTrace();
        }
    }

    public static void launchUrl(String url) {
        launchUrl(url, false);
    }

    public static String getTitle(String url, Boolean report) {
        try {
            String title = DriverManager.getWebDriver().getTitle();
            if (report) {
                GemTestReporter.addTestStep("Get Title", "Title ~" + title, STATUS.PASS);
            }
            return title;
        } catch (Exception e) {
            GemTestReporter.addTestStep("Get Title", "Get Title Failed", STATUS.FAIL);
            e.printStackTrace();
            return null;
        }
    }

    public static String getTitle(String url) {
        return getTitle(url, false);
    }

    public static void maximizeBrowser(Boolean report) throws IOException {
        try {
            DriverManager.getWebDriver().manage().window().maximize();
            if (report) {
                GemTestReporter.addTestStep("Maximize Browser", "Browser Maximize Successful ", STATUS.PASS,
                        DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Maximize Browser", "Browser Maximize Failed", STATUS.FAIL,
                    DriverAction.takeSnapShot());
            e.printStackTrace();
        }
    }

    public static void maximizeBrowser() {
        try {
            maximizeBrowser(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void minimizeBrowser(Boolean report) throws IOException {
        try {
            DriverManager.getWebDriver().manage().window().minimize();
            if (report) {
                GemTestReporter.addTestStep("Minimize Browser", "Browser Minimize Successful", STATUS.PASS,
                        DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Minimize Browser", "Browser Minimize Failed", STATUS.FAIL,
                    DriverAction.takeSnapShot());
            e.printStackTrace();
        }
    }

    public static void minimizeBrowser() {
        try {
            minimizeBrowser(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Dimension getBrowserSize() {
        try {
            return DriverManager.getWebDriver().manage().window().getSize();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setBrowserSize(int width, int height, Boolean report) {
        try {
            Dimension dimension = new Dimension(width, height);
            DriverManager.getWebDriver().manage().window().setSize(dimension);
            if (report) {
                GemTestReporter.addTestStep("Set Browser Size",
                        "Browser Size Set To <BR> width ~ " + width + "<BR> height ~ " + height, STATUS.PASS);
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Set Browser Size", "Set Browser Size Failed", STATUS.FAIL);
            e.printStackTrace();
        }
    }

    public static void setBrowserSize(int width, int height) {
        try {
            setBrowserSize(width, height, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBrowserPosition(int x, int y, Boolean report) {
        try {
            Point point = new Point(x, y);
            DriverManager.getWebDriver().manage().window().setPosition(point);
            if (report) {
                GemTestReporter.addTestStep("Set Browser Position",
                        "Browser Position Set To <BR> x ~ " + x + "<BR> y ~ " + y, STATUS.PASS);
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Set Browser Position", "Set Browser Position Failed", STATUS.FAIL);
            e.printStackTrace();
        }
    }

    public static void setBrowserPosition(int x, int y) {
        try {
            setBrowserSize(x, y, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Point getBrowserLocation() {
        try {
            Point p = DriverManager.getWebDriver().manage().window().getPosition();
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    ////////// TimeOuts///////////

    public static void waitSec(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImplicitTimeOut(long seconds) {
        try {
            DriverManager.getWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setScriptTimeOut(long seconds) {
        try {
            DriverManager.getWebDriver().manage().timeouts().scriptTimeout(Duration.ofSeconds(seconds));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPageLoadTimeOut(long seconds) {
        try {
            DriverManager.getWebDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(seconds));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /// Navigation

    public static void navigateBack(Boolean report) throws IOException {
        try {
            DriverManager.getWebDriver().navigate().back();
            if (report) {
                GemTestReporter.addTestStep("Navigate Back", "Navigate Back Successful", STATUS.PASS,
                        DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Navigate Back", "Navigate Back Failed", STATUS.FAIL,
                    DriverAction.takeSnapShot());
            e.printStackTrace();
        }
    }

    public static void navigateBack() {
        try {
            navigateBack(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void navigateRefresh(boolean report) throws IOException {
        try {
            DriverManager.getWebDriver().navigate().refresh();
            if (report) {
                GemTestReporter.addTestStep("Refresh Page", "Page Refresh Successful", STATUS.PASS,
                        DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Refresh Page", "Page Refresh Failed", STATUS.FAIL,
                    DriverAction.takeSnapShot());
            e.printStackTrace();
        }
    }

    public static void navigateRefresh() {
        try {
            navigateRefresh(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void navigateForward(Boolean report) throws IOException {
        try {
            DriverManager.getWebDriver().navigate().forward();
            if (report) {
                GemTestReporter.addTestStep("Navigate Forward", "Forward Navigation Successful ", STATUS.PASS,
                        DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Navigate Forward", "Forward Navigation Failed ", STATUS.FAIL,
                    DriverAction.takeSnapShot());
            e.printStackTrace();
        }
    }

    public static void navigateForward() {
        try {
            navigateForward(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void navigateToUrl(String url, Boolean report) throws IOException {
        try {
            DriverManager.getWebDriver().navigate().to(url);
            if (report) {
                GemTestReporter.addTestStep("Navigate to Url", "Navigation to Url Successful<BR>URL ~ " + url,
                        STATUS.PASS, DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Navigate to Url", "Navigation to Url Failed<BR>URL ~ " + url, STATUS.FAIL,
                    DriverAction.takeSnapShot());
            e.printStackTrace();
        }
    }

    public static void navigateToUrl(String url) {
        try {
            navigateToUrl(url, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /// WebElement
    public static WebElement getElement(By locator) {
        try {
            WebElement element = DriverManager.getWebDriver().findElement(locator);
            return element;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // return set<String>
    public static String getWindowHandle() {
        try {
            return DriverManager.getWebDriver().getWindowHandle();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<String> getWindowHandles() {
        try {
            return DriverManager.getWebDriver().getWindowHandles();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentURL() {
        try {
            return DriverManager.getWebDriver().getCurrentUrl();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPageSource() {
        try {
            return DriverManager.getWebDriver().getPageSource();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /////////// Switch Operations/////////////////////
    public static void switchToWindow(String nameOfHandle) {
        try {
            DriverManager.getWebDriver().switchTo().window(nameOfHandle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchToFrame(int index) {
        try {
            DriverManager.getWebDriver().switchTo().frame(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchToFrame(String nameOrId) {
        try {
            DriverManager.getWebDriver().switchTo().frame(nameOrId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchToFrame(WebElement frameElement) {
        try {
            DriverManager.getWebDriver().switchTo().frame(frameElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchToParentFrame() {
        try {
            DriverManager.getWebDriver().switchTo().parentFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchToDefaultContent() {
        try {
            DriverManager.getWebDriver().switchTo().defaultContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WebElement switchToActiveElement() {
        try {
            return DriverManager.getWebDriver().switchTo().activeElement();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void switchToAlert(Boolean report) throws IOException {
        try {
            DriverManager.getWebDriver().switchTo().alert();
            if (report) {
                GemTestReporter.addTestStep("Switch To Alert", "Switch To Alert Successful", STATUS.PASS,
                        DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Switch To Alert", "Switch To Alert Failed", STATUS.FAIL,
                    DriverAction.takeSnapShot());

            e.printStackTrace();
        }
    }

    public static void switchToAlert() throws IOException {
        switchToAlert(false);
    }

    public static void AcceptAlert(Boolean report) throws IOException {
        try {
            DriverManager.getWebDriver().switchTo().alert().accept();
            if (report) {
                GemTestReporter.addTestStep("Accept Alert", "Alert Accepted Successful", STATUS.PASS,
                        DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Accept Alert", "Alert Accepted Failed", STATUS.FAIL,
                    DriverAction.takeSnapShot());
            e.printStackTrace();
        }
    }

    public static void AcceptAlert() throws IOException {
        AcceptAlert(false);
    }

    public static void DismissAlert(Boolean report) throws IOException {
        try {
            DriverManager.getWebDriver().switchTo().alert().dismiss();
            if (report) {
                GemTestReporter.addTestStep("Dismiss Alert", "Alert Dismissed Successful", STATUS.PASS,
                        DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Dismiss Alert", "Alert Dismissed Failed", STATUS.FAIL,
                    DriverAction.takeSnapShot());
            e.printStackTrace();
        }
    }

    public static void DismissAlert() throws IOException {
        DismissAlert(false);
    }

    public static void AlertInput(String input, Boolean report) throws IOException {
        try {
            String s = DriverAction.takeSnapShot();
            DriverManager.getWebDriver().switchTo().alert().sendKeys(input);
            if (report) {
                GemTestReporter.addTestStep("SendKeys To Alert", "SendKeys To Alert Successful <BR> input ~ " + input,
                        STATUS.PASS, s);
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("SendKeys To Alert", "SendKeys To Alert Failed <BR> input ~ " + input,
                    STATUS.FAIL, DriverAction.takeSnapShot());
            e.printStackTrace();
        }
    }

    public static void AlertInput(String input) {
        AlertInput(input);
    }

    ////////////// Web Elements///////////////////

    public static List<WebElement> getElements(By locator) {
        try {
            List<WebElement> elements = DriverManager.getWebDriver().findElements(locator);
            return elements;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getElementText(By locator) {
        try {
            WebElement element = getElement(locator);
            String elementText = element.getText();
            return elementText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getElementText(WebElement element) {
        try {
            String elementText = element.getText();
            return elementText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getElementsText(By locator) {
        try {
            List<WebElement> elements = getElements(locator);
            List<String> elementsText = new ArrayList<String>();
            for (WebElement element : elements) {
                elementsText.add(getElementText(element));
            }
            return elementsText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    ////////////////// Click Operation/////////////////

    public static void click(By locator, String elementLabel) throws IOException {
        try {
            WebElement element = getElement(locator);
            element.click();
            GemTestReporter.addTestStep("Click on ", "Click Successful on " + elementLabel, STATUS.PASS,
                    DriverAction.takeSnapShot());

        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Click on ", "Click Failed on " + elementLabel, STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void click(By locator) throws IOException {
        try {
            WebElement element = getElement(locator);
            element.click();
        } catch (Exception e) {
            GemTestReporter.addTestStep("Some error occur while Click", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
            e.printStackTrace();
        }
    }

    public static void click(By locator, String steps, String description) throws IOException {
        try {
            WebElement element = getElement(locator);
            element.click();
            GemTestReporter.addTestStep(steps, description, STATUS.PASS, DriverAction.takeSnapShot());

        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep(steps, description, STATUS.FAIL, DriverAction.takeSnapShot());
        }
    }

    public static void click(WebElement webElement, String steps, String description) throws IOException {
        try {
            webElement.click();
            GemTestReporter.addTestStep(steps, description, STATUS.PASS, DriverAction.takeSnapShot());

        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep(steps, description, STATUS.FAIL, DriverAction.takeSnapShot());
        }
    }

    public static void click(WebElement webElement) throws IOException {
        try {
            webElement.click();
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur while Click", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void click(WebElement webElement, String elementLabel) throws IOException {
        try {
            webElement.click();
            GemTestReporter.addTestStep("Click on ", "Click Successful on " + elementLabel, STATUS.PASS,
                    DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Click on ", "Click Failed on " + elementLabel, STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    ///////////////// Type Operation//////////////////////

    public static void typeText(By locator, String textToEnter, String steps, String description) throws IOException {
        try {
            WebElement element = getElement(locator);
            element.clear();
            element.sendKeys(textToEnter);
            GemTestReporter.addTestStep(steps, description, STATUS.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep(steps, description, STATUS.FAIL, DriverAction.takeSnapShot());
        }
    }

    public static void typeText(By locator, String textToEnter) throws IOException {
        try {
            WebElement element = getElement(locator);
            element.clear();
            element.sendKeys(textToEnter);
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur while Type text", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());

        }
    }

    public static void typeText(By locator, String textToEnter, String elementLabel) throws IOException {
        try {
            WebElement element = getElement(locator);
            element.clear();
            element.sendKeys(textToEnter);
            GemTestReporter.addTestStep("Type on " + elementLabel, "Type Text Successful<BR>Type Text ~" + textToEnter,
                    STATUS.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Type on " + elementLabel, "Type Text Failed<BR>Type Text ~" + textToEnter,
                    STATUS.FAIL, DriverAction.takeSnapShot());
        }
    }

    public static void typeText(WebElement element, String textToEnter, String steps, String description)
            throws IOException {
        try {
            element.clear();
            element.sendKeys(textToEnter);

            GemTestReporter.addTestStep(steps, description, STATUS.PASS, DriverAction.takeSnapShot());

        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep(steps, description, STATUS.FAIL, DriverAction.takeSnapShot());
        }
    }

    public static void typeText(WebElement element, String textToEnter) {
        element.clear();
        element.sendKeys(textToEnter);
    }

    public static void typeText(WebElement element, String textToEnter, String elementLabel) throws IOException {
        try {
            element.clear();
            element.sendKeys(textToEnter);
            GemTestReporter.addTestStep("Type on " + elementLabel, "Type Text Successful<BR>Type Text ~" + textToEnter,
                    STATUS.PASS, DriverAction.takeSnapShot());

        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Type on " + elementLabel, "Type Text Failed<BR>Type Text ~" + textToEnter,
                    STATUS.FAIL, DriverAction.takeSnapShot());
        }
    }


    public static String getAttributeName(WebElement webElement, String name) throws IOException {
        try {
            return webElement.getAttribute(name);
        } catch (Exception e) {
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL, DriverAction.takeSnapShot());

            e.printStackTrace();
            return null;
        }
    }

    public static String getCSSValue(By locator, String propertyName) throws IOException {
        try {
            WebElement element = getElement(locator);
            return element.getCssValue(propertyName);
        } catch (Exception e) {
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL, DriverAction.takeSnapShot());

            e.printStackTrace();
            return null;
        }
    }

    public static String getCSSValue(WebElement element, String propertyName) throws IOException {
        try {
            return element.getCssValue(propertyName);
        } catch (Exception e) {
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL, DriverAction.takeSnapShot());

            e.printStackTrace();
            return null;
        }
    }

    /////////////////////////////////////////////

    public static String takeSnapShot() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String fileWithPath = QuanticGlobalVar.reportLocation + "/SS/SS" + timestamp.getTime() + ".png";
        WebDriver webdriver = DriverManager.getWebDriver();
        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile = new File(fileWithPath);
        try {
            FileUtils.copyFile(SrcFile, DestFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "SS/SS" + timestamp.getTime() + ".png";

    }

    public static String takeSnapShotBase64() {
        try {
            TakesScreenshot scrShot = ((TakesScreenshot) DriverManager.getWebDriver());
            String SrcFile = scrShot.getScreenshotAs(OutputType.BASE64);
            return SrcFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // close Driver or Current tab

    public static void closeCurrentTab() {
        try {
            DriverManager.closeDriver();
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Close Current Tab", "Some error occured", STATUS.PASS);
        }
    }

    //Double click mouse event

    public static void doubleClick(WebElement element, String elementLabel) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            act.doubleClick(element).perform();
            GemTestReporter.addTestStep("Double Click on ", "Double Click Successful on " + elementLabel, STATUS.PASS,
                    DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Double Click on ", "Double Click Failed on " + elementLabel, STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void doubleClick(By locator, String elementLabel) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            WebElement element = getElement(locator);
            act.doubleClick(element).perform();
            GemTestReporter.addTestStep("Double Click on ", "Double Click Successful on " + elementLabel, STATUS.PASS,
                    DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Double Click on ", "Double Click Failed on " + elementLabel, STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }
    public static void doubleClick(By locator) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            WebElement element = getElement(locator);
            act.doubleClick(element).perform();
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur while Double Click", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }
    public static void doubleClick(By locator, String steps, String description) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            WebElement element = getElement(locator);
            act.doubleClick(element).perform();
            GemTestReporter.addTestStep(steps, description, STATUS.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep(steps, description, STATUS.FAIL, DriverAction.takeSnapShot());
        }
    }
    public static void doubleClick(WebElement element, String steps, String description) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            act.doubleClick(element).perform();
            GemTestReporter.addTestStep(steps, description, STATUS.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep(steps, description, STATUS.FAIL, DriverAction.takeSnapShot());
        }
    }
    public static void doubleClick(WebElement element) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            act.doubleClick(element).perform();
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur while Double Click", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    // Right Click or Context click

    public static void rightClick(WebElement element, String elementLabel) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            act.contextClick(element).perform();
            GemTestReporter.addTestStep("Right Click on ", "Right Click Successful on " + elementLabel, STATUS.PASS,
                    DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Right Click on ", "Right Click Failed on " + elementLabel, STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }
    public static void rightClick(By locator, String elementLabel) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            WebElement element = getElement(locator);
            act.contextClick(element).perform();
            GemTestReporter.addTestStep("Right Click on ", "Right Click Successful on " + elementLabel, STATUS.PASS,
                    DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Right Click on ", "Right Click Failed on " + elementLabel, STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }
    public static void rightClick(By locator) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            WebElement element = getElement(locator);
            act.contextClick(element).perform();
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur while Right Click", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }
    public static void rightClick(By locator, String steps, String description) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            WebElement element = getElement(locator);
            act.contextClick(element).perform();
            GemTestReporter.addTestStep(steps, description, STATUS.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep(steps, description, STATUS.FAIL, DriverAction.takeSnapShot());
        }
    }
    public static void rightClick(WebElement element, String steps, String description) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            act.contextClick(element).perform();
            GemTestReporter.addTestStep(steps, description, STATUS.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep(steps, description, STATUS.FAIL, DriverAction.takeSnapShot());
        }
    }
    public static void rightClick(WebElement element) throws IOException {
        try {
            Actions act = new Actions(DriverManager.getWebDriver());
            act.contextClick(element).perform();
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur while Right Click", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    // drop down


    // upload file
    public static void fileUpload(WebElement element, String path) throws IOException {
        try {
            DriverAction.getElement((By) element).sendKeys(path);
            GemTestReporter.addTestStep("File Upload", "Successfully", STATUS.PASS, takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void fileUpload(By element, String path) throws IOException {
        try {
            DriverAction.getElement(element).sendKeys(path);
            GemTestReporter.addTestStep("File Upload", "Successfully", STATUS.PASS, takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }
//report function
    //drag and drop



    // drop down

    public static void dropDown(By element, String name, boolean report) throws IOException {
        try {
            Select drp = new Select(DriverAction.getElement(element));
            drp.selectByVisibleText(name);
            if (report) {
                GemTestReporter.addTestStep("DropDown", name + " Selected", STATUS.PASS, takeSnapShot());
            }
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void dropDown(WebElement element, String name, boolean report) throws IOException {
        try {
            Select drp = new Select(DriverAction.getElement((By) element));
            drp.selectByVisibleText(name);
            if (report) {
                GemTestReporter.addTestStep("DropDown", name + " Selected", STATUS.PASS, takeSnapShot());
            }
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void dropDown(By element, int index, boolean report) throws IOException {
        try {
            Select drp = new Select(DriverAction.getElement(element));
            drp.selectByIndex(index);
            if (report) {
                GemTestReporter.addTestStep("DropDown", index + " Selected", STATUS.PASS, takeSnapShot());
            }
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void dropDown(WebElement element, int index, boolean report) throws IOException {
        try {
            Select drp = new Select(DriverAction.getElement((By) element));
            drp.selectByIndex(index);
            if (report) {
                GemTestReporter.addTestStep("DropDown", index + " Selected", STATUS.PASS, takeSnapShot());
            }
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    // upload file

    public static void fileUpload(WebElement element, String path, boolean report) throws IOException {
        try {
            DriverAction.getElement((By) element).sendKeys(path);
            if (report) {
                GemTestReporter.addTestStep("File Upload", "Successfully", STATUS.PASS, takeSnapShot());
            }
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void fileUpload(By element, String path, boolean report) throws IOException {
        try {
            DriverAction.getElement(element).sendKeys(path);
            if (report) {
                GemTestReporter.addTestStep("File Upload", "Successfully", STATUS.PASS, takeSnapShot());
            }
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    // scroll
    public static void pageScroll(int X, int Y, boolean report) throws IOException {
        try {
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getWebDriver();
            js.executeScript("window.scrollBy(+" + X + "," + Y + ")");
            if (report) {
                GemTestReporter.addTestStep("Scrolling", "Successful", STATUS.PASS, takeSnapShot());
            }
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }


    // drag and drop

    public static void dragAndDrop(WebElement from, WebElement To, boolean report) throws IOException {
        try {
            WebElement from1 = DriverManager.getWebDriver().findElement((By) from);
            WebElement To1 = DriverManager.getWebDriver().findElement((By) To);
            Actions act = new Actions(DriverManager.getWebDriver());
            act.dragAndDrop(from1, To1).build().perform();
            if (report) {
                GemTestReporter.addTestStep("Drag and Drop", "Success", STATUS.PASS, DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur while Drag and drop", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void dragAndDrop(By from, By To, boolean report) throws IOException {
        try {
            WebElement from1 = DriverManager.getWebDriver().findElement(from);
            WebElement To1 = DriverManager.getWebDriver().findElement(To);
            Actions act = new Actions(DriverManager.getWebDriver());
            act.dragAndDrop(from1, To1).build().perform();
            if (report) {
                GemTestReporter.addTestStep("Drag and Drop", "Success", STATUS.PASS, DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur while Drag and drop", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void dragAndDrop(By from, String fromElementLabel, By To, String toElementLabel) throws IOException {
        try {
            WebElement from1 = DriverManager.getWebDriver().findElement(from);
            WebElement To1 = DriverManager.getWebDriver().findElement(To);
            Actions act = new Actions(DriverManager.getWebDriver());
            act.dragAndDrop(from1, To1).build().perform();
            GemTestReporter.addTestStep("Drag and Drop", "Successfully moved from " + fromElementLabel + " to " + toElementLabel, STATUS.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur while Drag and drop", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void dragAndDrop(WebElement from, String fromElementLabel, WebElement To, String toElementLabel) throws IOException {
        try {
            WebElement from1 = DriverManager.getWebDriver().findElement((By) from);
            WebElement To1 = DriverManager.getWebDriver().findElement((By) To);
            Actions act = new Actions(DriverManager.getWebDriver());
            act.dragAndDrop(from1, To1).build().perform();
            GemTestReporter.addTestStep("Drag and Drop", "Successfully moved from " + fromElementLabel + " to " + toElementLabel, STATUS.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur while Drag and drop", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }
    //Select

    // drop down
    public static void dropDown(By element, String name) throws IOException {
        try {
            Select drp = new Select(DriverAction.getElement(element));
            drp.selectByVisibleText(name);
            GemTestReporter.addTestStep("DropDown", name + " Selected", STATUS.PASS, takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void dropDown(WebElement element, String name) throws IOException {
        try {
            Select drp = new Select(DriverAction.getElement((By) element));
            drp.selectByVisibleText(name);
            GemTestReporter.addTestStep("DropDown", name + " Selected", STATUS.PASS, takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void dropDown(By element, int index) throws IOException {
        try {
            Select drp = new Select(DriverAction.getElement(element));
            drp.selectByIndex(index);
            GemTestReporter.addTestStep("DropDown", index + " Selected", STATUS.PASS, takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

    public static void dropDown(WebElement element, int index) throws IOException {
        try {
            Select drp = new Select(DriverAction.getElement((By) element));
            drp.selectByIndex(index);
            GemTestReporter.addTestStep("DropDown", index + " Selected", STATUS.PASS, takeSnapShot());
        } catch (Exception e) {
            e.printStackTrace();
            GemTestReporter.addTestStep("Some error occur", "Error Occur", STATUS.FAIL,
                    DriverAction.takeSnapShot());
        }
    }

}
