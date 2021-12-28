package polevod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import polevod.page_object.AuthPage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Test2 {

    static final Logger LOGGER = LogManager.getLogger(Test2.class);
    WebDriver driver;
    @BeforeAll
    static void before() {
        //System.setProperty("webdriver.chrome.driver", "chromedriver_mac");
        WebDriverManager.chromedriver().setup();
    }

    @AfterEach
    void after() throws InterruptedException {
        if (driver!=null) {
            Thread.sleep(3000);
            driver.quit();
        }
    }

    @Test
    void testFirst() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--start-fullscreen");
        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        AuthPage authPage = new AuthPage(driver);
        authPage.auth("9612884689", "passpass1");
    }
}
