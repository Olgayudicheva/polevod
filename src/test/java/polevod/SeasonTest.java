package polevod;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import polevod.page_object.AuthPage;
import polevod.page_object.Seasons;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;


public class SeasonTest {

    static final Logger LOGGER = LogManager.getLogger(SeasonTest.class);
    WebDriver driver;
    @BeforeAll
    static void before() {
        //System.setProperty("webdriver.chrome.driver", "chromedriver_mac");
        WebDriverManager.chromedriver().setup();
    }

    @AfterEach
    void after() throws InterruptedException {
        if (driver!=null) {
            Thread.sleep(5000);
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

         //открыть страницу Обороты-сезоны
        Seasons seasons=new Seasons(driver);
        seasons.openSeasonsPage();
        //получить список полей
        List<Seasons.Field> fieldList = seasons.getFieldList();
        fieldList.forEach(new Consumer<Seasons.Field>() {
            @Override
            public void accept(Seasons.Field field) {
                LOGGER.info(field.getName());
                LOGGER.info(field.yearList);
            }
        });
        fieldList.get(0).yearList.get(0).addSeason("Рис","01.01.2019","20.01.2019","");
        //получаем и выводим года массивом чисел
        LOGGER.info(seasons.getYearsSeasons());
    }
}
