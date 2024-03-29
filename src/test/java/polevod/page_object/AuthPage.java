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
    //поиск элемента input, у которого type="text"
    By byPhoneInput = new By.ByXPath("//input[@type=\"text\"]");
     //поиск элемента input, у которого type="password"
    By byPasswordInput = new By.ByXPath("//input[@type=\"password\"]");
     //поиск элемента button, у которого type="submit"
    By bySingInButton = new By.ByXPath("//button[@type=\"submit\"]");
     //поиск любого элемента, у которого в атрибуте class содержится "AppLayout_Veil_1z-ge"
    By byHZ = new By.ByXPath("//*[contains(@class, 'AppLayout_Veil_1z-ge')]");

    private final WebDriver driver;
    public AuthPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,10);
    }

    public void auth (String phone, String password){
        LOGGER.info("---Авторизация---");
        driver.get("https://polevod.direct.farm");
        wait.until(ExpectedConditions.visibilityOfElementLocated(byPhoneInput));
        if (!driver.findElements(byHZ).isEmpty()) {
            driver.findElement(byHZ).click();
        }
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
