package com.jd.selenium;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;


import java.text.DecimalFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class seleniumTest {

    private static  WebDriver driver;

    @BeforeAll
    private static void setup() {
        //All tests are ran in Chrome using Chrome WebDriver
        driver = new ChromeDriver();
        System.out.println("Testing");
    }


    @Test
    public void subscribeAndSaveAddEligibleItemtoCartTest() throws InterruptedException {
        //Lauches amazon iste
        driver.get ("https://www.amazon.com");
        Thread.sleep(2000);

        //Selects Subscribe and Save filter from search filter dropdown
        WebElement selectFilter = driver.findElement(By.id("searchDropdownBox"));
        Select filterSelector = new Select(selectFilter);
        filterSelector.selectByVisibleText("Subscribe & Save");

        //Enter an item in the search box
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("tylenol extra strength");
        Thread.sleep(2000);

        //Retrieve the list returned from search and click on an item
        List<WebElement> matches = driver.findElements(By.xpath("//div[contains(text(), 'tylenol extra strength')]"));
        System.out.println("Size of matched elements:"+matches.size());
        WebElement targetElement = matches.get(0);
        System.out.println("Element:"+targetElement.getTagName());
        targetElement.click();

        //Click on an item from result set
        driver.findElement(By.xpath("//a[(@class='a-link-normal s-link-style a-text-normal')]/span[text()='Tylenol Extra Strength Caplets with 500 mg Acetaminophen Pain Reliever Fever Reducer, 100 Count']")).click();

        //Verify the default selected purchase plan is Subscribe and Save
        WebElement defaultSnSradio = driver.findElement(By.xpath("//i[@class='a-icon a-accordion-radio a-icon-radio-active']/following-sibling::h5//span[contains(text(),' Subscribe & Save: ')]"));
        assertNotNull(defaultSnSradio);

        //Change the default subscription qty
        WebElement selectQty= driver.findElement(By.id("rcxsubsQuan"));
        Select quantitySelector = new Select(selectQty);
        quantitySelector.selectByIndex(4);

        //Change the default subscription delivery
        WebElement selectDelivery = driver.findElement(By.id("rcxOrdFreqOnml"));
        Select deliverySelector = new Select(selectDelivery);
        deliverySelector.selectByVisibleText(" 2 months ");

        //Find the original price without Subscription and Save discount
        WebElement Price = driver.findElement(By.xpath("//div[@id='corePrice_desktop']//table//tr//td//span[contains(@class,'a-price a-text-price a-size-medium apexPriceToPay')]"));
        System.out.println("OrgPrice:"+Price.getText().replace("$","").trim());
        float subsPrice = Float.parseFloat(Price.getText().replace("$","").trim());

        //Define Subscribe and save percent as 5% and validate onscreen subscricption price matches 5% of its original
        String percent = "0.05";
        float discount = Float.parseFloat(percent);
        float DP = (discount * subsPrice);
        System.out.println("DiscountedPrice:"+DP);
        float discountPrice = (subsPrice - DP);
        DecimalFormat df = new DecimalFormat("#####.##");
        String formattedDiscountPrice = df.format(discountPrice);
        System.out.println("DiscountedPrice:" + formattedDiscountPrice);

        String sns = driver.findElement(By.xpath("//span[@id='sns-base-price'][1]")).getText().replace("$","").trim();
        String[] parts = sns.split(" ");
        String part1 = parts[0];
        System.out.println("FinalP:"+part1);
        Assertions.assertEquals(part1,formattedDiscountPrice);
        driver.findElement(By.xpath("//button[@id='rcx-subscribe-submit-button-announce']")).click();

        //Click on Cart to verify item added is in the Cart
        driver.findElement(By.xpath("//a[@id='nav-cart']")).click();
        WebElement Subssavecartitem = driver.findElement(By.xpath("//span[text()='Tylenol Extra Strength Caplets with 500 mg Acetaminophen Pain Reliever Fever Reducer, 100 Count']"));
        assertNotNull(Subssavecartitem);


    }



    @Test
    public void subscribeAndsaveDefaultOptionTest() throws InterruptedException {

        //launches amazon site
        driver.get ("https://www.amazon.com");
        Thread.sleep(2000);

        //Enter an item in the search box
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("tylenol extra strength");
        Thread.sleep(2000);

        //Retrieve the list returned from search and click on an item
        List<WebElement> matches = driver.findElements(By.xpath("//div[contains(text(), 'tylenol extra strength')]"));
        System.out.println("Size of matched elements:"+matches.size());
        WebElement targetElement = matches.get(0);
        System.out.println("Element:"+targetElement.getTagName());
        targetElement.click();

        //Select Subscribe and Save filter option from side navigation bar
        driver.findElement(By.xpath("//div[(@class='a-section')]/span[text()='Subscribe & Save Eligible']")).click();
        Thread.sleep(2000);

        //Click on an item from result set
        driver.findElement(By.xpath("//a[(@class='a-link-normal s-link-style a-text-normal')]/span[text()='Tylenol Extra Strength Caplets with 500 mg Acetaminophen Pain Reliever Fever Reducer, 100 Count']")).click();

        //Verify the default selected purchase plan is Subscribe and Save
        WebElement defaultSnSradio = driver.findElement(By.xpath("//i[@class='a-icon a-accordion-radio a-icon-radio-active']/following-sibling::h5//span[contains(text(),' Subscribe & Save: ')]"));
        assertNotNull(defaultSnSradio);

    }

    @Test
    public void subscribeAndSaveOptionNOTavailableForNotEligibleItemsTest() throws InterruptedException {
        //Launches amazon site
        driver.get ("https://www.amazon.com");
        Thread.sleep(2000);

        //Enter an item in the search box
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("aspirin 81mg 500 count");
        Thread.sleep(2000);
        List<WebElement> matches = driver.findElements(By.xpath("//div[contains(text(), 'aspirin 81mg 500 count')]"));  // and ancestor::div[@id='nav-flyout-searchAjax']")).click()
        System.out.println("Size of matched elements:"+matches.size());
        WebElement targetElement = matches.get(0);
        System.out.println("Element:"+targetElement.getTagName());
        targetElement.click();

        //Asserts Subscribe and save option NOT available for not eligible items
        driver.findElement(By.xpath("//a[(@class='a-link-normal s-link-style a-text-normal')]/span[text()='GenCare - Aspirin Pain Reliever (NSAID) 81 mg (500 Coated Tablets) Adult Low Dose | Safe Pain Relief Enteric Coated Aspirin Pills | Muscle Pain & Menstrual Pain Relief | Generic Bayer']")).click();
        NoSuchElementException thrownException = assertThrows(NoSuchElementException.class, () -> {
            WebElement defaultSnSradio = driver.findElement(By.xpath("//i[@class='a-icon a-accordion-radio a-icon-radio-active']/following-sibling::h5//span[contains(text(),' Subscribe & Save: ')]"));
        });

    }

    @Test
    public void subscribeAndSaveSwitchPurchasePlanTest() throws InterruptedException {
        //Lauches amazon site
        driver.get ("https://www.amazon.com");
        Thread.sleep(2000);

        //Enter an item in the search box
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("tylenol extra strength");
        Thread.sleep(2000);

        //Retrieve the list returned from search and click on an item
        List<WebElement> matches = driver.findElements(By.xpath("//div[contains(text(), 'tylenol extra strength')]"));  // and ancestor::div[@id='nav-flyout-searchAjax']")).click()
        System.out.println("Size of matched elements:"+matches.size());
        WebElement targetElement = matches.get(0);
        System.out.println("Element:"+targetElement.getTagName());
        targetElement.click();

        //Verify Subscribe and Save plan not selected
        driver.findElement(By.xpath("//a[(@class='a-link-normal s-link-style a-text-normal')]/span[text()='Tylenol Extra Strength Acetaminophen Rapid Release Gels, Pain Reliever & Fever Reducer, 100 ct']")).click();
        NoSuchElementException thrownException = assertThrows(NoSuchElementException.class, () -> {
            WebElement defaultSnSradio = driver.findElement(By.xpath("//i[@class='a-icon a-accordion-radio a-icon-radio-active']/following-sibling::h5//span[contains(text(),' Subscribe & Save: ')]"));

        });
        //Select Subscribe and Save radio option
       WebElement Subsave = driver.findElement(By.xpath("//div[@id='snsAccordionRowMiddle']"));
        //WebElement Subsave = driver.findElement(By.xpath("//i[@class='a-icon a-accordion-radio a-icon-radio-inactive']"));
        System.out.println("Element:"+Subsave.getTagName());
        Subsave.click();
        Thread.sleep(2000);

        //Subscribe and save radio option is selected
        WebElement subsnsaveradio = driver.findElement(By.xpath("//i[@class='a-icon a-accordion-radio a-icon-radio-active']/following-sibling::h5//span[contains(text(),' Subscribe & Save: ')]"));
            assertNotNull(subsnsaveradio);

    }

}
