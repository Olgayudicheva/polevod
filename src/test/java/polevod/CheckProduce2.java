package polevod;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import java.util.ArrayList;
import java.util.List;


public class CheckProduce2 {

    static final Logger LOGGER = LogManager.getLogger(CheckProduce2.class);

    @BeforeAll
    static void before() {
        //System.setProperty("webdriver.chrome.driver", "chromedriver_mac");
        WebDriverManager.chromedriver().setup();
    }

    By.ByXPath phoneXpath = new By.ByXPath("//input[@type=\"text\"]");
    By byArhive = new By.ByXPath("(//*[contains(.,\"Угрозы по культурам\")])[last()]/../../..");
    By bySelectKhoz = new By.ByXPath("//*[@id=\"app\"]/section/aside/div/ul/li[1]/div");
    By bySelectTestovoeKhoz = new By.ByXPath("(//*[@class=\"ant-menu-item-group\"]//*[contains(.,'Тестовое хозяйство')])[last()]/../..");
    By byClickPlus = new By.ByXPath("//*[@id=\"app\"]/section/section/main/section/div[3]/div/div/div/div/div/table/tbody/tr[1]/td[1]/span");

    By byCards = new By.ByXPath("(//div[contains(.,\"Подмаренник цепкий\")])[last()]/../../..//div[@class=\"ExpandedRowItem_ProtectionWrapper_210X_\"]/div");

    By byLoadNext = new By.ByXPath("(//div[contains(.,\"Подмаренник цепкий\")])[last()]/../../..//span[contains(text(), \"Загрузить еще\")]/..");

    By getXpathByText(String text) {
        return new By.ByXPath("(//*[contains(.,'"+text+"')])[last()]");
    }

    @Test //простой тест авторизации
    void testFirst() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        //options.addArguments("--headless");
        //options.addArguments("--start-maximised");
        //options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        // options.addArguments();
        WebDriver driver = new ChromeDriver(options);
        //driver.manage().window().maximize();
        try {
            driver.get("https://polevod.direct.farm/");
            WebDriverWait wait = new WebDriverWait(driver, 10);
            Actions actions = new Actions(driver);
            wait.until((ExpectedConditions.visibilityOfElementLocated(phoneXpath)));
            WebElement phone = driver.findElement(phoneXpath);
            WebElement pass = driver.findElement (new By.ByXPath("//input[@type=\"password\"]"));
            WebElement loginbutton = driver.findElement((new By.ByXPath("//button[@type=\"submit\"]")));
            phone.click();
            phone.sendKeys("9612884689");
            pass.sendKeys("passpass1");
            loginbutton.click();
            //Поиске и клик по выборку хозяйства
            wait.until(ExpectedConditions.presenceOfElementLocated(bySelectKhoz));
            wait.until(ExpectedConditions.elementToBeClickable(bySelectKhoz)).click();
            //Поиск и клик по Тестовому хозяйству
            wait.until(ExpectedConditions.presenceOfElementLocated(bySelectTestovoeKhoz));
            wait.until(ExpectedConditions.elementToBeClickable(bySelectTestovoeKhoz)).click();
            //Поиск и клик по архиву
            Thread.sleep(1000);
            wait.until(ExpectedConditions.presenceOfElementLocated(byArhive));
            wait.until(ExpectedConditions.elementToBeClickable(byArhive)).click();
            //нажимаем плюс
            wait.until(ExpectedConditions.presenceOfElementLocated(byClickPlus));
            wait.until(ExpectedConditions.elementToBeClickable(byClickPlus)).click();
            //



            boolean end = false;
            while (end==false) {
                try {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(byLoadNext));
                    wait.until(ExpectedConditions.elementToBeClickable(byLoadNext)).click();
                } catch (Throwable t) {
                    end = true;
                }
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(byCards));
            List<WebElement> cardsList = driver.findElements(byCards);
            ArrayList<String> errorDeveloper = new ArrayList<>();
            ArrayList<String> errorNotFound = new ArrayList<>();
            LOGGER.info(cardsList.size());

            for (int i=0;i<cardsList.size();i++) {
                WebElement element = cardsList.get(i);
                String name = element.findElement(new By.ByXPath(".//div[@class=\"ProtectionItem_ProtectionItemName_1o_qj\"]/a")).getText();
                String developer = element.findElement(new By.ByXPath(".//div[@class=\"ProtectionItem_ProtectionItemRegistrant_RK9gY\"]")).getText();
                //LOGGER.info(developer);
                if (developer.isBlank()) {
                    errorDeveloper.add("\""+name+"\"");
                }

                By byDetails = new By.ByXPath(".//div[@class=\"ProtectionItem_ProtectionItemName_1o_qj\"]/a");
                List<WebElement> detailsList = element.findElements(byDetails);
                if (detailsList.isEmpty()) continue;
                WebElement details = detailsList.get(0);
                actions.moveToElement(details).perform();

                wait.until(ExpectedConditions.visibilityOf(details));
                wait.until(ExpectedConditions.elementToBeClickable(details)).click();
                wait.until(ExpectedConditions.numberOfWindowsToBe(2));
                String[] handles = driver.getWindowHandles().toArray(new String[2]);
                driver.switchTo().window(handles[handles.length-1]);
                wait.until(ExpectedConditions.presenceOfElementLocated(new By.ByTagName("header")));
                List<WebElement> notFound404 = driver.findElements(new By.ByXPath("*[contains(.,\"404\")]"));
                if (!notFound404.isEmpty()) {
                    errorNotFound.add("\""+name+"\"");
                }
                driver.close();
                driver.switchTo().window(handles[0]);
            }

            LOGGER.info("Пустые производители у "+errorDeveloper);
            LOGGER.info("404: "+errorNotFound);



            //actions = new Actions(driver);
            //actions.moveToElement(driver.findElement(bySelectTestovoeKhoz)).perform();
            //Thread.sleep(1000);
            /*
            wait.until((ExpectedConditions.visibilityOfElementLocated(bySelectKhoz)));
            actions.moveToElement(driver.findElement(bySelectKhoz)).perform();
            wait.until(ExpectedConditions.visibilityOfElementLocated(bySelectTestovoeKhoz));
            actions.moveToElement(driver.findElement(bySelectTestovoeKhoz)).perform();
            driver.findElement(bySelectTestovoeKhoz).click();*/

            //открываем страницу "угрозы по культурам"
            /*wait.until((ExpectedConditions.visibilityOfElementLocated(byArhive)));
            driver.findElement(byArhive).click();*/



        } finally {
            Thread.sleep(10000);
            driver.quit();
                    }


    }
}
