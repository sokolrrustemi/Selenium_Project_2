package testCase;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.BaseDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class AllTestCase extends BaseDriver {

    Actions action = new Actions(driver);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @Test
    public void TC_0101RegisterTest() {

        driver.get("https://demowebshop.tricentis.com/");
        driver.findElement(By.cssSelector(".ico-register")).click();

        action.click(driver.findElement(By.cssSelector("#gender-male")));
        action.sendKeys(driver.findElement(By.cssSelector("#FirstName")), "Ahmet");
        action.sendKeys(driver.findElement(By.cssSelector("#LastName")), "Qa");
        action.sendKeys(driver.findElement(By.cssSelector("#Email")), "qahmet16@gmail.com");
        action.sendKeys(driver.findElement(By.cssSelector("#Password")), "cyprus1");
        action.sendKeys(driver.findElement(By.cssSelector("#ConfirmPassword")), "cyprus1");
        action.click(driver.findElement(By.cssSelector("#register-button")));
        action.perform();

        try {
            String result = driver.findElement(By.cssSelector(".page-body > .result")).getText();
            Assert.assertEquals("Your registration completed", result);
            action.click(driver.findElement(By.cssSelector(".header-links > ul > li + li > a.ico-logout")));
        } catch (Exception e) {
            System.out.println("Üyelik daha önce yapıldığından dolayı bu adımı geçebiliriz.");
        }
    }


    @Test
    public void TC_0201RegisterExistingUserTest() {

        driver.get("https://demowebshop.tricentis.com/");

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

    }

    @Test
    public void TC_0301LoginTest() {

        driver.get("https://demowebshop.tricentis.com/");

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

    }

    @Test
    public void TC_0401LoginWithInvalidCredentialsTest() {

        driver.get("https://demowebshop.tricentis.com/");

        action.click(driver.findElement(By.cssSelector(".ico-login"))).perform();
        action.sendKeys(driver.findElement(By.cssSelector("#Email")), "qahmet1@gmail.com");
        action.sendKeys(driver.findElement(By.cssSelector("#Password")), "cyprus1");
        action.click(driver.findElement(By.xpath("//input[@type='submit'][@value='Log in']")));
        action.perform();

        String result = driver.findElement(By.xpath("(//div[@class='header-links']//a)[1]")).getText();
        Assert.assertEquals("qahmet1@gmail.com", result);

    }

    @Test
    public void TC_0501LoginWithInvalidCredentialsTest2() {

        driver.get("https://demowebshop.tricentis.com/");

        String[] emailArray = {"", "qahmet1@gmail.com", "", "ozgur6@gmail.com"};
        String[] passwordArray = {"", "", "cyprus1", "Ankara02"};
        try {
            action.click(driver.findElement(By.cssSelector(".ico-logout"))).perform();
        } catch (Exception e) {
            System.out.println("Login değil ise bu adımı yapma");
        }

        for (int i = 0; i < emailArray.length; i++) {
            action.click(driver.findElement(By.cssSelector(".ico-login"))).perform();
            action.sendKeys(driver.findElement(By.cssSelector("#Email")), emailArray[i]);
            action.sendKeys(driver.findElement(By.cssSelector("#Password")), passwordArray[i]);
            action.click(driver.findElement(By.xpath("//input[@type='submit'][@value='Log in']")));
            action.perform();
            String result = driver.findElement(By.cssSelector(".validation-summary-errors > span")).getText();
            Assert.assertEquals("Login was unsuccessful. Please correct the errors and try again.", result);
        }
    }

    @Test
    public void TC_0601OrderTest() {

        driver.get("https://demowebshop.tricentis.com/");

        action.click(driver.findElement(By.cssSelector(".ico-login"))).perform();
        action.sendKeys(driver.findElement(By.cssSelector("#Email")), "qahmet@gmail.com");
        action.sendKeys(driver.findElement(By.cssSelector("#Password")), "cyprus1");
        action.click(driver.findElement(By.xpath("//input[@type='submit'][@value='Log in']")));
        action.perform();

        List<WebElement> itemTitlesList = driver.findElements(By.xpath("//h2[@class=\"product-title\"]"));
        for (WebElement webElement : itemTitlesList) {
            if (webElement.getText().contains("14.1-inch Laptop")) {
                webElement.click();
                break;
            }
        }

        WebElement addToCartBtn =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@type='button'][@value='Add to cart'])[1]")));
        addToCartBtn.click();

        WebElement successMsjAdToCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='content']")));
        Assert.assertEquals(successMsjAdToCard.getText(), "The product has been added to your shopping cart");

        WebElement shoppingCart = driver.findElement(By.xpath("//li[@id='topcartlink']/a"));
        wait.until(ExpectedConditions.visibilityOf(shoppingCart));
        shoppingCart.click();

        WebElement country = driver.findElement(By.id("CountryId"));
        Select selectCountry = new Select(country);
        selectCountry.selectByValue("1");

        WebElement state = driver.findElement(By.id("StateProvinceId"));
        Select selectState = new Select(state);
        selectState.selectByValue("9");

        WebElement zipCode = driver.findElement(By.id("ZipPostalCode"));
        zipCode.clear();
        zipCode.sendKeys("44100");

        WebElement terms = driver.findElement(By.id("termsofservice"));
        terms.click();

        WebElement checkOut = driver.findElement(By.id("checkout"));
        checkOut.click();

        WebElement newAddress = driver.findElement(By.xpath("//*[text()='New Address']"));
        newAddress.click();

        WebElement country1 = driver.findElement(By.id("BillingNewAddress_CountryId"));

        Select selectCountry1 = new Select(country1);
        selectCountry1.selectByValue("1");

        WebElement city = driver.findElement(By.id("BillingNewAddress_City"));
        city.sendKeys("NewYork");

        WebElement address = driver.findElement(By.id("BillingNewAddress_Address1"));
        address.sendKeys("NewYork 2");

        WebElement zipPostalCode = driver.findElement(By.id("BillingNewAddress_ZipPostalCode"));
        zipPostalCode.sendKeys("44100");

        WebElement phone = driver.findElement(By.id("BillingNewAddress_PhoneNumber"));
        phone.sendKeys("1856745963");

        WebElement continue1 =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"Billing.save()\"]")));
        continue1.click();

        WebElement continue2 =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"Shipping.save()\"]")));
        continue2.click();

        WebElement continue3 =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"ShippingMethod.save()\"]")));
        continue3.click();

        WebElement continue4 =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"PaymentMethod.save()\"]")));
        continue4.click();

        WebElement continue5 =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"PaymentInfo.save()\"]")));
        continue5.click();

        WebElement confirmBtn =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"ConfirmOrder.save()\"]")));
        confirmBtn.click();

        WebElement successMessage = driver.findElement(By.xpath("//*[text()='Your order has been successfully processed!']"));
        Assert.assertTrue(successMessage.getText().contains("successfully"));

        WebElement orderDetails =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Click here for order details.']")));
        orderDetails.click();

        WebElement orderText =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".order-number")));

        String orderNumber = orderText.getText().substring(7);

        Assert.assertTrue(driver.getCurrentUrl().contains(orderNumber));

        WebElement logOutBtn = driver.findElement(By.className("ico-logout"));
        logOutBtn.click();

    }

    @Test
    public void TC_0701PollTest() {

        driver.get("https://demowebshop.tricentis.com/");

        action.click(driver.findElement(By.id("pollanswers-1"))).perform();
        action.click(driver.findElement(By.id("vote-poll-1"))).perform();

        WebElement warningMessage =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Only registered users can vote.']")));

        Assert.assertTrue(warningMessage.getText().contains("Only registered users can vote"));

        action.click(driver.findElement(By.cssSelector(".ico-login"))).perform();
        action.sendKeys(driver.findElement(By.cssSelector("#Email")), "qahmet@gmail.com");
        action.sendKeys(driver.findElement(By.cssSelector("#Password")), "cyprus1");
        action.click(driver.findElement(By.xpath("//input[@type='submit'][@value='Log in']")));
        action.perform();

        try {
            action.click(driver.findElement(By.id("pollanswers-1"))).perform();
            action.click(driver.findElement(By.id("vote-poll-1"))).perform();

        } catch (Exception e) {
            System.out.println("Vote daha önceden verildi");
        }

        WebElement voteText = driver.findElement(By.cssSelector(".poll-total-votes"));
        Assert.assertTrue(voteText.getText().contains("vote"));
    }


    @Test
    public void TC_0801OrderTestNegative() {

        driver.get("https://demowebshop.tricentis.com/");

        try {
            action.click(driver.findElement(By.cssSelector(".ico-logout"))).perform();

        } catch (Exception e) {
            System.out.println("Logout ise bu adımı yapma");
        }

        action.click(driver.findElement(By.cssSelector(".ico-login"))).perform();
        action.sendKeys(driver.findElement(By.cssSelector("#Email")), "qahmet@gmail.com");
        action.sendKeys(driver.findElement(By.cssSelector("#Password")), "cyprus1");
        action.click(driver.findElement(By.xpath("//input[@type='submit'][@value='Log in']")));
        action.perform();

        List<WebElement> itemTitlesList = driver.findElements(By.xpath("//h2[@class=\"product-title\"]"));
        for (WebElement webElement : itemTitlesList) {
            if (webElement.getText().contains("14.1-inch Laptop")) {
                webElement.click();
                break;
            }
        }

        WebElement addToCartBtn =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@type='button'][@value='Add to cart'])[1]")));
        addToCartBtn.click();

        WebElement successMsjAdToCard =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='content']")));

        Assert.assertEquals(successMsjAdToCard.getText(), "The product has been added to your shopping cart");

        WebElement shoppingCart = driver.findElement(By.xpath("//li[@id='topcartlink']/a"));
        wait.until(ExpectedConditions.visibilityOf(shoppingCart));
        shoppingCart.click();

        WebElement applyCoupon = driver.findElement(By.cssSelector("[class='button-2 apply-discount-coupon-code-button'"));
        wait.until(ExpectedConditions.visibilityOf(applyCoupon));
        applyCoupon.click();

        WebElement applyCouponMessage = driver.findElement(By.cssSelector("[class='message']"));
        Assert.assertEquals("The coupon code you entered couldn't be applied to your order", applyCouponMessage.getText());

        WebElement addGiftCard = driver.findElement(By.cssSelector("[class='button-2 apply-gift-card-coupon-code-button'"));
        wait.until(ExpectedConditions.visibilityOf(addGiftCard));
        addGiftCard.click();

        WebElement addGiftCardMessage = driver.findElement(By.cssSelector("[class='message']"));
        Assert.assertEquals("The coupon code you entered couldn't be applied to your order", addGiftCardMessage.getText());

        WebElement country = driver.findElement(By.id("CountryId"));
        Select selectCountry = new Select(country);
        selectCountry.selectByValue("1");

        WebElement state = driver.findElement(By.id("StateProvinceId"));
        Select selectState = new Select(state);
        selectState.selectByValue("9");

        WebElement zipCode = driver.findElement(By.id("ZipPostalCode"));
        zipCode.clear();
        zipCode.sendKeys("44100");

        WebElement terms = driver.findElement(By.id("termsofservice"));
        terms.click();

        WebElement checkOut = driver.findElement(By.id("checkout"));
        checkOut.click();

        WebElement newAddress = driver.findElement(By.xpath("//*[text()='New Address']"));
        newAddress.click();

        WebElement country1 = driver.findElement(By.id("BillingNewAddress_CountryId"));

        Select selectCountry1 = new Select(country1);
        selectCountry1.selectByValue("1");

        WebElement city = driver.findElement(By.id("BillingNewAddress_City"));
        city.sendKeys("NewYork");

        WebElement address = driver.findElement(By.id("BillingNewAddress_Address1"));
        address.sendKeys("NewYork 2");

        WebElement zipPostalCode = driver.findElement(By.id("BillingNewAddress_ZipPostalCode"));
        zipPostalCode.sendKeys("44100");

        WebElement phone = driver.findElement(By.id("BillingNewAddress_PhoneNumber"));
        phone.sendKeys("1856745963");

        WebElement continue1 =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"Billing.save()\"]")));
        continue1.click();

        WebElement continue2 =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"Shipping.save()\"]")));
        continue2.click();

        WebElement continue3 =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"ShippingMethod.save()\"]")));
        continue3.click();

        WebElement continue4 =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"PaymentMethod.save()\"]")));
        continue4.click();

        WebElement continue5 =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"PaymentInfo.save()\"]")));
        continue5.click();

        WebElement confirmBtn =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[onclick=\"ConfirmOrder.save()\"]")));
        confirmBtn.click();

        WebElement successMessage = driver.findElement(By.xpath("//*[text()='Your order has been successfully processed!']"));
        Assert.assertTrue(successMessage.getText().contains("successfully"));

        WebElement logOutBtn = driver.findElement(By.className("ico-logout"));
        logOutBtn.click();

    }

    @Test
    public void invoiceDisplay() {

        driver.get("https://demowebshop.tricentis.com/");

        try {
            action.click(driver.findElement(By.cssSelector(".ico-logout"))).perform();

        } catch (Exception e) {
            System.out.println("Logout ise bu adımı yapma");
        }

        action.click(driver.findElement(By.cssSelector(".ico-login"))).perform();
        action.sendKeys(driver.findElement(By.cssSelector("#Email")), "qahmet@gmail.com");
        action.sendKeys(driver.findElement(By.cssSelector("#Password")), "cyprus1");
        action.click(driver.findElement(By.xpath("//input[@type='submit'][@value='Log in']")));
        action.perform();
        action.click(driver.findElement(By.cssSelector(".account"))).perform();
        action.click(driver.findElement(By.xpath("(//a[text()='Orders'])[1]"))).perform();
        action.click(driver.findElement(By.xpath("(//input[@type='button'])[2]"))).perform();
        action.click(driver.findElement(By.cssSelector("[class='button-2 pdf-order-button']"))).perform();

        WebElement orderText =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".order-number")));
        String orderNumber = orderText.getText().substring(7);

        // Tab sayısı değişiklik gösterebilir indirilen pdf ye gelene kadar tabları sayın.
        Actions actions = new Actions(driver);

        Robot robot = null;

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 26; i++) {

            actions.sendKeys(Keys.TAB).perform();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            if (i == 25) {
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_CONTROL);
            }
        }

        Set<String> windowHandles = driver.getWindowHandles();
        String[] handles = windowHandles.toArray(new String[0]);
        driver.switchTo().window(handles[1]);
        String secondTabTitle = driver.getCurrentUrl();
        Assert.assertTrue(secondTabTitle.contains(orderNumber));

        delayQuit();

    }
}
