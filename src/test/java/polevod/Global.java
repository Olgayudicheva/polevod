package polevod;

import org.openqa.selenium.By;

abstract public class Global {
    //поиск элементов в левом меню по тексту
    public static By findLeftMenuItemByText(String text) {
        return new By.ByXPath("(//*[contains(@class, \"ant-layout-sider-children\")]//*[contains(.,\""+text+"\")])[last()]/../../..");
    }
    //поиск элементов по содержащимуся в нем тексту
    public static By findElementByContainsText(String text) {
        return new By.ByXPath("(//*[contains(.,'"+text+"')])[last()]");
    }
}
