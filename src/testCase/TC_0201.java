package testCase;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import utility.BaseDriver;
import org.junit.Assert;

public class TC_0201 extends BaseDriver {
    @Test
    public void registerExistingUserTest() {

        driver.get("https://demowebshop.tricentis.com/");

        Actions action = new Actions(driver);
        action.click(driver.findElement(By.cssSelector(".ico-register"))).perform();
        action.click(driver.findElement(By.cssSelector("#gender-male")));
        action.sendKeys(driver.findElement(By.cssSelector("#FirstName")), "Ahmet");
        action.sendKeys(driver.findElement(By.cssSelector("#LastName")), "Qa");
        action.sendKeys(driver.findElement(By.cssSelector("#Email")), "qa1@gmail.com");
        action.sendKeys(driver.findElement(By.cssSelector("#Password")), "cyprus");
        action.sendKeys(driver.findElement(By.cssSelector("#ConfirmPassword")), "cyprus");
        action.click(driver.findElement(By.cssSelector("#register-button")));
        action.perform();

        String result = driver.findElement(By.cssSelector(".validation-summary-errors > ul > li")).getText();
        Assert.assertEquals("The specified email already exists", result);

        delayQuit();
    }
}
