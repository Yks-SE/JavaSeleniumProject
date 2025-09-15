package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class LoginTest {

    // ThreadLocal drivers/waits to support parallel TestNG execution
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> tlWait = new ThreadLocal<>();

    private WebDriver getDriver() {
        return tlDriver.get();
    }

    private WebDriverWait getWait() {
        return tlWait.get();
    }

    // helper: attempt to find visible element with retries to avoid StaleElementReference
    private WebElement safeFindVisible(By locator, int attempts) {
        WebDriverWait wait = getWait();
        for (int i = 0; i < attempts; i++) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (StaleElementReferenceException e) {
                try { Thread.sleep(150); } catch (InterruptedException ignored) {}
            } catch (TimeoutException e) {
                throw e;
            }
        }
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // safe click with retries
    private void safeClick(By locator) {
        WebDriverWait wait = getWait();
        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                el.click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                try { Thread.sleep(150); } catch (InterruptedException ignored) {}
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    // safe sendKeys with retries
    private void safeSendKeys(By locator, CharSequence... keys) {
        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = safeFindVisible(locator, 1);
                el.clear();
                el.sendKeys(keys);
                return;
            } catch (StaleElementReferenceException e) {
                try { Thread.sleep(150); } catch (InterruptedException ignored) {}
            }
        }
        WebElement el = safeFindVisible(locator, 1);
        el.clear();
        el.sendKeys(keys);
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        // Detect OS
        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("win");

        // Headless selection
        if (isWindows) {
            options.addArguments("--headless");
        } else {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");

        // unique user-data-dir per-thread (normalize backslashes)
        String tmpDir = System.getProperty("java.io.tmpdir");
        Path profilePath = Paths.get(tmpDir, "chrome-user-data-" + Thread.currentThread().getId() + "-" + System.currentTimeMillis()).toAbsolutePath();
        String profilePathStr = profilePath.toString().replace("\\", "/");
        options.addArguments("--user-data-dir=" + profilePathStr);

        // Ensure chromedriver matches installed Chrome (safe to call concurrently)
        WebDriverManager.chromedriver().setup();

        WebDriver driver;
        try {
            driver = new ChromeDriver(options);
        } catch (WebDriverException firstEx) {
            System.err.println("Primary Chrome start failed: " + firstEx.getMessage());
            ChromeOptions fallback = new ChromeOptions();
            if (isWindows) {
                fallback.addArguments("--headless");
            } else {
                fallback.addArguments("--headless=new");
                fallback.addArguments("--no-sandbox");
                fallback.addArguments("--disable-dev-shm-usage");
            }
            fallback.addArguments("--disable-gpu", "--disable-extensions", "--remote-allow-origins=*");
            driver = new ChromeDriver(fallback);
        }

        try {
            driver.manage().window().maximize();
        } catch (Exception ignored) {}

        // store in ThreadLocal
        tlDriver.set(driver);
        tlWait.set(new WebDriverWait(driver, Duration.ofSeconds(12))); // slightly longer wait
    }

    @Test
    public void testValidLogin() {
        getDriver().get("https://the-internet.herokuapp.com/login");

        safeSendKeys(By.id("username"), "tomsmith");
        safeSendKeys(By.id("password"), "SuperSecretPassword!");
        safeClick(By.cssSelector("button[type='submit']"));

        WebElement message = safeFindVisible(By.id("flash"), 3);
        Assert.assertTrue(message.getText().contains("You logged into a secure area!"));
    }

    @Test
    public void testInvalidLogin() {
        getDriver().get("https://the-internet.herokuapp.com/login");

        safeSendKeys(By.id("username"), "wrongusername");
        safeSendKeys(By.id("password"), "wrongpassword");
        safeClick(By.cssSelector("button[type='submit']"));

        WebElement message = safeFindVisible(By.id("flash"), 3);
        Assert.assertTrue(message.getText().contains("Your username is invalid!"));
    }

    @Test
    public void testEmptyFields() {
        getDriver().get("https://the-internet.herokuapp.com/login");

        safeClick(By.cssSelector("button[type='submit']"));

        WebElement message = safeFindVisible(By.id("flash"), 3);
        Assert.assertTrue(message.getText().contains("Your username is invalid!"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();

        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            try {
                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File screenshotsDir = new File("screenshots");
                if (!screenshotsDir.exists()) screenshotsDir.mkdir();
                String fileName = "screenshots/" + result.getName() + "_" + Thread.currentThread().getId() + "_" + System.currentTimeMillis() + ".png";
                FileUtils.copyFile(src, new File(fileName));
                System.out.println("Screenshot saved: " + fileName);
            } catch (WebDriverException e) {
                System.err.println("Could not capture screenshot (session may be closed): " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // quit & remove thread-local driver
        if (driver != null) {
            try {
                driver.quit();
            } catch (WebDriverException ignored) {}
        }
        tlDriver.remove();
        tlWait.remove();
    }
}