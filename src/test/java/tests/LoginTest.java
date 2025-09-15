package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // ✅ Selenium Manager will auto-manage the right ChromeDriver version
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testValidLogin() {
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        WebElement message = driver.findElement(By.id("flash"));
        Assert.assertTrue(message.getText().contains("You logged into a secure area!"));
    }

    @Test
    public void testInvalidLogin() {
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys("wrongusername");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        WebElement message = driver.findElement(By.id("flash"));
        Assert.assertTrue(message.getText().contains("Your username is invalid!"));
    }

    @Test
    public void testEmptyFields() {
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        WebElement message = driver.findElement(By.id("flash"));
        Assert.assertTrue(message.getText().contains("Your username is invalid!"));
    }

    @AfterMethod
    public void tearDown() {
        // ✅ The browser is closed after EACH test
        if (driver != null) {
            driver.quit();
        }
    }
}