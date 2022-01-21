package polevod.templates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import polevod.Global;
import polevod.page_object.AuthPage;

public class PageObjectTemplate {
        final Logger LOGGER = LogManager.getLogger(PageObjectTemplate.class);
        final WebDriverWait wait;
        private final WebDriver driver;

        public PageObjectTemplate(WebDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver,10);
        }
}
