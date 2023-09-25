package testCase;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import utility.BaseDriver;

public class TC_0301 extends BaseDriver {
    @Test
    public void loginTest() {

        driver.get("https://demowebshop.tricentis.com/");

        Actions action = new Actions(driver);
        action.click(driver.findElement(By.cssSelector(".ico-login"))).perform();
        action.sendKeys(driver.findElement(By.cssSelector("#Email")), "qahmet1@gmail.com");
        action.sendKeys(driver.findElement(By.cssSelector("#Password")), "cyprus1");
        action.click(driver.findElement(By.xpath("//input[@type='submit'][@value='Log in']")));
        action.perform();

        String result = driver.findElement(By.xpath("(//div[@class='header-links']//a)[1]")).getText();
        Assert.assertEquals("qahmet1@gmail.com", result);

        action.click(driver.findElement(By.cssSelector(".ico-logout"))).perform();

        String logIn = driver.findElement(By.cssSelector(".ico-login")).getText();
        Assert.assertEquals("Log in", logIn);

        delayQuit();

    }
}
