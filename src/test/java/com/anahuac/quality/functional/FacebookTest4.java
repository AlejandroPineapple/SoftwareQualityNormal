package com.anahuac.quality.functional;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class FacebookTest4 {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    JavascriptExecutor js;

    @Before
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        baseUrl = "https://www.facebook.com/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testUntitledTestCase() throws Exception {
        driver.get(baseUrl);
        pause(5000);
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("puppies");
        driver.findElement(By.id("pass")).clear();
        driver.findElement(By.id("pass")).sendKeys("puppies");
        driver.findElement(By.name("login")).click();
        pause(5000);

        String actualResult = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/div[2]/form/div/div[1]/div[2]")).getText();
        assertThat(actualResult, is("The email or mobile number you entered isn’t connected to an account. Find your account and log in."));
        pause(3000);

    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
    
    private boolean isElementPresent(By by) {
        try {
          driver.findElement(by);
          return true;
        } catch (NoSuchElementException e) {
          return false;
        }
      }
    
    private void pause(long mils) {
  	  try {
  		  Thread.sleep(mils);
  	  }
  	  catch(Exception e) {
  		  e.printStackTrace();
  	  }
    }
    
    
}
