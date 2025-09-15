package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.CSVReaderUtil;

import java.util.List;

public class LoginDataDrivenTest extends BaseTest {

    @Test
    public void testLoginWithCSVData() {
        String filePath = "src/test/resources/data/credentials.csv"; // absolute path inside project
        List<String[]> testData = CSVReaderUtil.readCSV(filePath);

        for (String[] data : testData) {
            String username = data[0];
            String password = data[1];
            String expectedMessage = data[2];

            driver.get("https://the-internet.herokuapp.com/login");
            driver.findElement(By.id("username")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            WebElement message = driver.findElement(By.id("flash"));
            Assert.assertTrue(
                    message.getText().contains(expectedMessage),
                    "Expected: " + expectedMessage + " but got: " + message.getText()
            );
        }
    }
}