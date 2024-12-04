package com.anahuac.quality.functional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.time.Duration;

public class FacebookTest {
	
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;
  
  @BeforeEach
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
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      //fail(verificationErrorString);
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

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
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