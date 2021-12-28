package polevod.page_object;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class AuthPage {
    final Logger LOGGER = LogManager.getLogger(AuthPage.class);
    final WebDriverWait wait;
    By byPhoneInput = new By.ByXPath("//input[@type=\"text\"]");
    By byPasswordInput = new By.ByXPath("//input[@type=\"password\"]");
    By bySingInButton = new By.ByXPath("//button[@type=\"submit\"]");
    private final WebDriver driver;
    public AuthPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,10);
    }

    public void auth (String phone, String password){
        LOGGER.info("---Авторизация---");
        driver.get("https://polevod.direct.farm");
        wait.until(ExpectedConditions.presenceOfElementLocated(byPhoneInput));
        WebElement phoneElement = driver.findElement((byPhoneInput));
        phoneElement.click();
        phoneElement.sendKeys(phone);
        WebElement passwordElement = driver.findElement((byPasswordInput));
        passwordElement.click();
        passwordElement.sendKeys(password);


        driver.findElement(bySingInButton).click();

    }
}
