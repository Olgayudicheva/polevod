package polevod;

import org.openqa.selenium.By;

abstract public class Global {
    public static By findLeftMenuItemByText(String text) {
        return new By.ByXPath("(//*[contains(@class, \"ant-layout-sider-children\")]//*[contains(.,\""+text+"\")])[last()]/../../..");
    }

    public static By findElementByContainsText(String text) {
        return new By.ByXPath("(//*[contains(.,'"+text+"')])[last()]");
    }
}
