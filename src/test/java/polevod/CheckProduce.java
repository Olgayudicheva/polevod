package polevod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.sql.Array;
import java.util.*;


public class CheckProduce {

    static final Logger LOGGER = LogManager.getLogger(CheckProduce.class);

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

    By threatCard = new By.ByXPath("//div[contains(@class,\"ExpandedRowItem_Wrapper\")]");

    By byCards = new By.ByXPath(".//div[contains(@class,\"ProtectionWrapper\")]/div");

    By byLoadNext = new By.ByXPath(".//span[contains(text(), \"Загрузить еще\")]/..");

    By getXpathByText(String text) {
        return new By.ByXPath("(//*[contains(.,'" + text + "')])[last()]");
    }

    @Test
        //простой тест авторизации
    void testFirst() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        //options.addArguments("--headless");
        //options.addArguments("--start-maximised");
        //options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        // options.addArguments();
        WebDriver driver = new ChromeDriver(options);
        HashSet<String> errorDeveloper = new HashSet<>();
        HashMap<String, ErrorUtl> errorNotFound = new HashMap<>();
        //driver.manage().window().maximize();
        try {
            driver.get("https://polevod.direct.farm/");
            WebDriverWait wait = new WebDriverWait(driver, 20);
            Actions actions = new Actions(driver);
            wait.until((ExpectedConditions.visibilityOfElementLocated(phoneXpath)));
            WebElement phone = driver.findElement(phoneXpath);
            WebElement pass = driver.findElement(new By.ByXPath("//input[@type=\"password\"]"));
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


            wait.until(ExpectedConditions.visibilityOfElementLocated(threatCard));
            List<WebElement> threadCardList = driver.findElements(threatCard);


            LOGGER.info("Кол угроз: "+threadCardList.size());
            for (int threatCardIndex = 0; threatCardIndex < threadCardList.size(); threatCardIndex++) {
                WebElement threadCard = threadCardList.get(threatCardIndex);
                if (threadCard.findElements(new By.ByXPath(".//div[contains(@class,\"ProtectionWrapper\")]/div[@class=\"ExpandedRowItem_ProtectionsPlaceholder_nUvki\"]")).size()>0) {
                    continue;
                }

                boolean end = false;
                while (end == false) {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(new By.ByXPath(".//div[contains(@class,\"ProtectionWrapper\")]")));
                    List<WebElement> load = threadCard.findElements(byLoadNext);
                    if (load.isEmpty()) {
                        end = true;
                        break;
                    }
                    wait.until(ExpectedConditions.elementToBeClickable(load.get(0))).click();
                }

                List<WebElement> cardsList = threadCard.findElements(byCards);
                LOGGER.info(cardsList.size());



                for (int i = 0; i < cardsList.size(); i++) {
                    WebElement element = cardsList.get(i);

                    WebElement nameElement = element.findElement(new By.ByXPath(".//div[@class=\"ProtectionItem_ProtectionItemName_1o_qj\"]/a"));
                    String name = nameElement.getText();
                    String href = nameElement.getAttribute("href");
                    String developer = element.findElement(new By.ByXPath(".//div[@class=\"ProtectionItem_ProtectionItemRegistrant_RK9gY\"]")).getText();
                    //LOGGER.info(developer);
                    if (developer.isBlank()) {
                        errorDeveloper.add(name);
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
                    driver.switchTo().window(handles[handles.length - 1]);
                    wait.until(ExpectedConditions.presenceOfElementLocated(new By.ByTagName("header")));
                    List<WebElement> notFound404 = driver.findElements(new By.ByXPath("*[contains(.,\"Ошибка 404\")]"));
                    if (!notFound404.isEmpty()) {
                        String okUrl = getUrlByName(name);
                        errorNotFound.put(name, new ErrorUtl(okUrl, href));
                    }
                    driver.close();
                    driver.switchTo().window(handles[0]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Gson gson = new Gson().newBuilder().create();
            LOGGER.info("Пустые производители у: " + gson.toJson(errorDeveloper));
            LOGGER.info("Error url: " + gson.toJson(errorNotFound));
            Thread.sleep(5000);
            driver.quit();
        }


    }

    class ErrorNotFound {
        Set<String> errorDeveloper;

    }

    String getUrlByName(String name) throws IOException {
        Gson gson = new Gson().newBuilder().create();
        OkHttpClient okHttpClient = new OkHttpClient();


        HttpUrl.Builder httpBuilder = HttpUrl.parse("https://direct.farm/api/resource/v2/knowledge/plant/search").newBuilder();
        httpBuilder.addQueryParameter("query",name);
        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .build();

        ResponseBody response = okHttpClient.newCall(request).execute().body();
        JsonObject jsonObject = gson.fromJson(response.string(), JsonObject.class);
        JsonObject first = jsonObject.getAsJsonArray("groups")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("articles")
                .getAsJsonArray("objectList")
                .get(0).getAsJsonObject();

        JsonObject promotion = first.get("promotion").getAsJsonObject().get("path").getAsJsonObject();
        int id = promotion.get("articleId").getAsInt();
        String rootName = promotion.get("root").getAsString();
        String group = promotion.get("group").getAsString();

        String url = "https://direct.farm/knowledge/"+rootName+"/"+group+"/"+id;
        LOGGER.info(url);
        return url;
    }

    class ErrorUtl {
        String ok;
        String error;

        public ErrorUtl(String ok, String error) {
            this.ok = ok;
            this.error = error;
        }
    }
}