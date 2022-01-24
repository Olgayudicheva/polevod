package polevod.page_object;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import polevod.Global;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class Seasons {
     final Logger LOGGER = LogManager.getLogger(Seasons.class);
     final WebDriverWait wait;
    //By byClickSeasons = new By.ByXPath("(//*[contains(@class, \"ant-layout-sider-children\")]//*[contains(.,\"Обороты-сезоны\")])[last()]/../../..");
    By byClickSeasons = Global.findLeftMenuItemByText("Обороты-сезоны");
    By byFieldTable=new By.ByXPath("//*[contains(@class,'ant-table-tbody')]");
    private final WebDriver driver;

    public Seasons(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,10);
    }
    public void openSeasonsPage(){
        wait.until(ExpectedConditions.presenceOfElementLocated(byClickSeasons));
        driver.findElement(byClickSeasons).click();
    }
    //получить с/х поля
    public List<Field> getFieldList(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(byFieldTable));
        ArrayList<Field> fields = new ArrayList<>();
        List<Integer> years = getYearsSeasons();
        driver.findElement(byFieldTable).findElements(new By.ByXPath("tr")).forEach(new Consumer<WebElement>() {
            @Override
            public void accept(WebElement webElement) {
                fields.add(new Field(webElement, years));
            }
        });
        return fields;
    }
    public class Field {
        public final WebElement webElement;
        final By byName = new By.ByXPath(".//a[contains(@class, 'TerritoryLink')]");
        final By byYearsRows = new By.ByXPath(".//td[contains(@class,\"3hG14\")]/div");
        public ArrayList<Year> yearList = new ArrayList<>();

        public Field(WebElement webElement, List<Integer> years) {
            this.webElement=webElement;
            List<WebElement> rows = webElement.findElements(byYearsRows);
            for (int yi=0;yi<years.size();yi++) {
                ArrayList<WebElement> yearsItem = new ArrayList<>();
                for (int i=0;i<rows.size()-1;i++) {
                    yearsItem.add(rows.get(i).findElements(new By.ByXPath("./div")).get(yi));
                }
                yearList.add(new Year(yearsItem, years.get(yi)));
            }
        }

        public String getName() {
            return webElement.findElement(byName).getText();
        }

        public class Year {
            public final List<WebElement> items;
            final Integer year;
            final boolean isEmpty;
            public Year(List<WebElement> items, Integer year) {
                this.items=items;
                this.year = year;

                isEmpty = seasonCount()==0;
            }

            int seasonCount() {
                int count = 0;
                for (WebElement item : items) {
                    if (!item.findElements(new By.ByXPath("./*")).isEmpty()) {
                        count++;
                    }
                }
                return count;
            }

            public void addSeason(String cultural, String startDate, String endDate, String result) {
                //Кликаем по кнопке "добавить сезон"
                webElement.findElement(new By.ByXPath(".//span[contains(@role,\"button\")]")).click();
                //Ищем и ждем пока появится диалоговое окно
                By modalXpath = new By.ByXPath("//*[contains(text(),'Создание нового сезона')]/../..");
                wait.until(ExpectedConditions.presenceOfElementLocated(modalXpath));
                WebElement modal = driver.findElement(modalXpath);
                //Нажимаем на выбор культуры
                modal.findElement(new By.ByXPath(".//label[contains(text(), \"Культура\")]/../../div[2]//div[contains(@class, 'select')]")).click();//.sendKeys(cultural);
                //Вводим культуру
                By inputCulturalXpath = new By.ByXPath("//*[contains(text(),'Создание нового сезона')]/../..//label[contains(text(), \"Культура\")]/../..//input");
                wait.until(ExpectedConditions.presenceOfElementLocated(inputCulturalXpath));
                driver.findElement(inputCulturalXpath).sendKeys(cultural);
                driver.findElement(inputCulturalXpath).sendKeys(Keys.RETURN);
                //Вводим дату сева (startDate)
                By inputStartDateXpath = new By.ByXPath("//input[@placeholder=\"Выберите дату сева\"]");
                wait.until(ExpectedConditions.presenceOfElementLocated(inputStartDateXpath));
                driver.findElement(inputStartDateXpath).sendKeys(startDate);
                // Вводим дату уборки (endDate)
                By inputendDateXpath = new By.ByXPath("//input[@placeholder=\"Выберите дату факт. уборки\"]");
                wait.until(ExpectedConditions.presenceOfElementLocated(inputendDateXpath));
                driver.findElement(inputendDateXpath).sendKeys(endDate);

                //Нажимаем сохранить
                By buttonSaveNewSeason = new By.ByXPath("//button[@class=\"ant-btn ant-btn-primary ant-btn-lg\"]");
                wait.until(ExpectedConditions.presenceOfElementLocated(buttonSaveNewSeason));
                driver.findElement(buttonSaveNewSeason).click();


            }

            @Override
            public String toString() {
                return "Year{" +
                        " year=" + year +
                        " isEmpty=" + isEmpty +
                        " count=" + seasonCount() +
                        '}';
            }
        }
    }
    //получение списка годов
    public List<Integer> getYearsSeasons(){
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        //wait.until(ExpectedConditions.presenceOfElementLocated(byGetYearsSeasons));
        ArrayList<Integer> years = new ArrayList<>();
        driver.findElement(new By.ByXPath("//*[@id=\"app\"]/section/section/main/section/div[4]/div/div/div/div/div/table/thead/tr/th[2]/div"))
                .findElements(new By.ByXPath("./div")).forEach(new Consumer<WebElement>() {
            @Override
            public void accept(WebElement webElement) {
                years.add(Integer.parseInt(webElement.getText()));
                LOGGER.debug(webElement.getText());
            }
        });


        return years;
    }
}
