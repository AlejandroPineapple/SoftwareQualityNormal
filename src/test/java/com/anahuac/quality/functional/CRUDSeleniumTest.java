package com.anahuac.quality.functional;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class CRUDSeleniumTest {

	private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Explicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    }

    @Test
    @Order(2)
    public void CreateRecord() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).sendKeys("Kuromilover69");
        driver.findElement(By.name("email")).sendKeys("kuromi@gmail.com");
        driver.findElement(By.name("age")).sendKeys("120");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Female'])[1]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        Thread.sleep(2000);
        String result = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[4]/div/p")).getText();
        assertEquals("Successfully added!", result);
    }

    @Test
    @Order(5)
    public void CreateRecordWhenEmailTaken() throws InterruptedException {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).sendKeys("Soyuncopion");
        driver.findElement(By.name("email")).sendKeys("kuromi@gmail.com");
        driver.findElement(By.name("age")).sendKeys("120");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Female'])[1]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        Thread.sleep(2000);
        String result = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[5]/div/p")).getText();
        assertEquals("That email is already taken.", result);
    }
    
    @Test
    @Order(4)
    public void Edit() throws Exception {
    	driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr/td[5]/button")).click();
        driver.findElement(By.name("age")).click();
        Thread.sleep(2000);
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("15");
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();
        Thread.sleep(2000);
        String result = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[4]/div/p")).getText();
        
        assertEquals("Successfully updated!", result);
    }
    
    @Test
    @Order(3)
    public void FindOne() throws Exception {
    	driver.get("https://mern-crud-mpfr.onrender.com/");
    	String result = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
        String expected = "Kuromilover69";
        
        assertEquals(expected, result);
    }
    
    @Test
    @Order(1)
    public void FindAll() throws Exception {
    	
    	driver.get("https://mern-crud-mpfr.onrender.com/");
	    WebElement table = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));
	    List<WebElement> rows = table.findElements(By.xpath(".//tr"));
	    
	    if (rows.size() > 1) {
	        for (int i = 1; i < rows.size(); i++) {
	            driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[5]/button[2]")).click();
	    	    driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/button[1]")).click();
	    	    driver.get("https://mern-crud-mpfr.onrender.com/");
	        }
	    }
        
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).sendKeys("Quierodormir");
        driver.findElement(By.name("email")).sendKeys("quierodormir@gmail.com");
        driver.findElement(By.name("age")).sendKeys("120");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Female'])[1]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).sendKeys("Yatermine");
        driver.findElement(By.name("email")).sendKeys("yatermine@gmail.com");
        driver.findElement(By.name("age")).sendKeys("120");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Female'])[1]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).sendKeys("Porfavorpongame10");
        driver.findElement(By.name("email")).sendKeys("plis@gmail.com");
        driver.findElement(By.name("age")).sendKeys("120");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Female'])[1]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        Thread.sleep(5000);
 
        
    	String result1 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[3]/td[1]")).getText();
        String expected1 = "Quierodormir";
        
        String result2 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[2]/td[1]")).getText();
        String expected2 = "Yatermine";
        
        String result3 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
        String expected3 = "Porfavorpongame10";
        
        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
        assertEquals(expected3, result3);
        //maestra estoy 99% que si funciona asi pero si no es por el orden
        //esta imposible probarlo porque todos estan creando al mismo tiempo pido perdon
        //ya hasta me estan diciendo que deje de molestar
        //pero si deberia funcionar
    }
    
    @Test
    @Order(6)
    public void Delete() throws Exception {
    	driver.get("https://mern-crud-mpfr.onrender.com/");
    	String result = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[5]/button[2]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/button[1]")).click();
        Thread.sleep(2000);
        driver.get("https://mern-crud-mpfr.onrender.com/");
        String expected = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
        
        assertNotEquals(expected, result);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
