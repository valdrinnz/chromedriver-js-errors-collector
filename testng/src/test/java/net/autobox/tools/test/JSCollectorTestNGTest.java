package net.autobox.tools.test;

import java.lang.reflect.Method;
import net.autobox.tools.JSErrorsCollectorListener;
import net.autobox.tools.JSErrorsCollectorTestNG;
import net.autobox.tools.testng.drivers.JSErrorsDriverHolder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

/** Class that contains TestNG unit tests for Chromedriver JS errors collector. */
@Listeners(JSErrorsCollectorListener.class)
public class JSCollectorTestNGTest {

    private WebDriver driver;
    private final int BROWSER_WAIT_MILLISECONDS = 4000;

    /** Static method that sets WebDriver system property. */
    @BeforeClass
    static void setChromeDriver() {
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + "/src/main/java/drivers/webdriver/chromedriver");
    }

    @BeforeMethod
    public void setDriverForListener(Method method) {
        driver = new ChromeDriver();
        JSErrorsDriverHolder.setDriverForTest(method.getName(), driver);
    }

    /** Test method.
     * It should receive JS reference error and expect related exception. */
    @Test
    @JSErrorsCollectorTestNG
    void referenceErrorTest() throws InterruptedException {
        driver.get("http://testjs.site88.net");
        driver.findElement(By.name("testClickButton")).click();
        waitBeforeClosingBrowser();
    }

    /** Test method.
     * It should not receive any JS error and end without exception. */
    @Test
    @JSErrorsCollectorTestNG
    void noJSErrorsTest() throws InterruptedException {
        driver.get("http://this-page-intentionally-left-blank.org/");
        waitBeforeClosingBrowser();
    }

    /** Test method.
     * It should receive JS error but not fail the test due boolean flag false value. */
    @Test
    @JSErrorsCollectorTestNG(assertJSErrors = false)
    void assertJSErrorsFalseTest() throws InterruptedException {
        driver.get("http://testjs.site88.net");
        driver.findElement(By.name("testClickButton")).click();
        waitBeforeClosingBrowser();
    }
    @AfterMethod
    void closeDriver() {
        driver.quit();
    }

    private void waitBeforeClosingBrowser() throws InterruptedException {
        Thread.sleep(BROWSER_WAIT_MILLISECONDS);
    }
}
