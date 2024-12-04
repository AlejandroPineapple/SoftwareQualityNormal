package com.anahuac.quality.functional;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.time.Duration;

public class SeleniumTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;
  @Before
  public void setUp() throws Exception {
	WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    js = (JavascriptExecutor) driver;
  }

@Test
  public void testUntitledTestCase() throws Exception {
    driver.get(baseUrl + "chrome://newtab/");
    driver.get("https://www.google.com/search?q=puppies&oq=puppies&gs_lcrp=EgZjaHJvbWUqDggAEEUYJxg7GIAEGIoFMg4IABBFGCcYOxiABBiKBTIMCAEQABhDGIAEGIoFMgcIAhAAGIAEMgcIAxAAGIAEMgcIBBAAGIAEMgcIBRAAGIAEMgcIBhAAGIAEMgkIBxAAGAoYgAQyBwgIEAAYgAQyBwgJEAAYgATSAQgxNjQyajBqN6gCALACAA&sourceid=chrome&ie=UTF-8");
    driver.findElement(By.xpath("//div[@id='kp-wp-tab-overview']/div[2]/div/div/div/div/div/div/div/div/div/div/div/span[3]/a/span")).click();
    driver.get("https://en.wikipedia.org/wiki/Puppy");
    //Warning: assertTextPresent may require manual changes
    //assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*From Wikipedia, the free encyclopedia[\\s\\S]*$"));
    String actualResult = driver.findElement(By.xpath("/html/body/div[2]/div/div[3]/main/div[3]/div[3]/div[1]/figure[2]/figcaption/a")).getText();
    assertThat(actualResult, is("Basset Hound"));
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
}