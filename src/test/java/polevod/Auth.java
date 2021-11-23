package polevod;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Auth {
    @BeforeAll
    static void before() {
        //System.setProperty("webdriver.chrome.driver", "chromedriver_mac");
        WebDriverManager.chromedriver().setup();
    }

    By.ByXPath phoneXpath = new By.ByXPath("//input[@type=\"text\"]");

    @Test //простой тест авторизации
    void testAuthSimple() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
       // options.addArguments();
        WebDriver driver = new ChromeDriver(options);
        //driver.manage().window().maximize();
        try {
            driver.get("https://polevod.direct.farm/");
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until((ExpectedConditions.visibilityOfElementLocated(phoneXpath)));
            WebElement phone = driver.findElement(phoneXpath);
            WebElement pass = driver.findElement (new By.ByXPath("//input[@type=\"password\"]"));
            WebElement loginbutton = driver.findElement((new By.ByXPath("//button[@type=\"submit\"]")));
            phone.click();
            phone.sendKeys("9612884689");
            pass.sendKeys("passpass1");
            loginbutton.click();

        } finally {
            Thread.sleep(10000);
            driver.quit();
                    }


    }
}
