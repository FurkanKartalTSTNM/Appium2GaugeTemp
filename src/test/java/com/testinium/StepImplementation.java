package com.testinium;

import com.thoughtworks.gauge.Step;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class StepImplementation extends HookImp {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Step({"<seconds> saniye bekle", "Wait <second> seconds"})
    public void waitBySecond(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
            logger.info("{} saniye beklendi",seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step(" XPath ile elementi bul ve tıkla <xpath>")
    public void clickElementByXpathIOS(String xpath) {
        WebElement element = driver.findElement(AppiumBy.xpath(xpath));
        element.click();
    }
    @Step("ID ile elementi bul ve tıkla <id>")
    public void clickElementByIdIOS(String id) {
        WebElement element = driver.findElement(AppiumBy.id(id));
        element.click();
    }


}
