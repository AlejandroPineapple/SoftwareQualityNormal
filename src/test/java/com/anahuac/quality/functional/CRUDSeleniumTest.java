package com.anahuac.quality.functional;

import java.util.regex.Pattern;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.time.Duration;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class CRUDSeleniumTest {
	
	private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();
	  JavascriptExecutor js;
	  
	  @Before
	  public void setUp() throws Exception {
	    WebDriverManager.chromedriver().setup();
	    driver = new ChromeDriver();
	    baseUrl = "https://mern-crud-mpfr.onrender.com/";
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
	    js = (JavascriptExecutor) driver;
	  }
	  
	  // MODIFICAR DELETE Y ARREGLAR UPDATEAGE

	  @Test
	  public void a_createUser_test() throws Exception {
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click(); //boton add
	    driver.findElement(By.name("name")).click();
	    driver.findElement(By.name("name")).clear();
	    driver.findElement(By.name("name")).sendKeys("Majo");
	    driver.findElement(By.name("email")).click();
	    driver.findElement(By.name("email")).clear();
	    driver.findElement(By.name("email")).sendKeys("majo@email.com");
	    driver.findElement(By.name("age")).click();
	    driver.findElement(By.name("age")).clear();
	    driver.findElement(By.name("age")).sendKeys("18");
	    pause(2000);
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click(); // display flechita
	    pause(2000);
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[2]")).click(); // female
	    driver.findElement(By.xpath("//html/body/div[3]/div/div[2]/form/button")).click(); //boton de add
	    pause(3000);
	    
	    String actualResult = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[4]/div/p")).getText();
        assertThat(actualResult, is("Successfully added!"));
        pause(2000);
	    
	  }
	  
	 @Test
	  public void b_createDuplicateEmail_test() throws Exception {
		driver.get("https://mern-crud-mpfr.onrender.com/");
		driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
	    driver.findElement(By.name("name")).click();
	    driver.findElement(By.name("name")).clear();
	    driver.findElement(By.name("name")).sendKeys("Majo1");
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Add User'])[1]/following::form[1]")).click();
	    driver.findElement(By.name("email")).click();
	    driver.findElement(By.name("email")).clear();
	    driver.findElement(By.name("email")).sendKeys("majo@email.com");
	    driver.findElement(By.name("age")).click();
	    driver.findElement(By.name("age")).clear();
	    driver.findElement(By.name("age")).sendKeys("19");
        pause(2000);
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click(); // flechita display
        pause(2000);
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[2]")).click(); // female
	    driver.findElement(By.xpath("//html/body/div[3]/div/div[2]/form/button")).click(); //boton de add
	    pause(3000);
	    
	    String actualResult = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[5]/div/p")).getText();
        assertThat(actualResult, is("That email is already taken."));
        pause(2000);
	  }
	 
	 @Test
	 public void c_updateAge_test() throws Exception {
		driver.get("https://mern-crud-mpfr.onrender.com/");
		
		driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click(); //boton add
	    driver.findElement(By.name("name")).click();
	    driver.findElement(By.name("name")).clear();
	    driver.findElement(By.name("name")).sendKeys("MajoUpdate");
	    driver.findElement(By.name("email")).click();
	    driver.findElement(By.name("email")).clear();
	    driver.findElement(By.name("email")).sendKeys("majoupdate@email.com");
	    driver.findElement(By.name("age")).click();
	    driver.findElement(By.name("age")).clear();
	    driver.findElement(By.name("age")).sendKeys("24");
	    pause(2000);
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click(); // display flechita
	    pause(2000);
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[2]")).click(); // female
	    driver.findElement(By.xpath("//html/body/div[3]/div/div[2]/form/button")).click(); //boton de add
	    pause(3000);
		driver.findElement(By.xpath("/html/body/div[3]/div/i")).click(); // boton cerrar
		pause(4000);
		
		driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[5]/button[1]")).click(); //boton Edit
		driver.findElement(By.name("age")).click();
		pause(4000);
	    driver.findElement(By.name("age")).clear();
	    pause(4000);
	    driver.findElement(By.name("age")).sendKeys("25"); // modificar edad a 25
	    pause(4000);
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click(); // Boton Save
	    pause(4000);
	    		 
	    String actualResult = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[4]/div/p")).getText();
        assertThat(actualResult, is("Successfully updated!"));	
        pause(4000);
       
	 }
	 
	 @Test
	 public void d_deleteUser_test() {
	     driver.get("https://mern-crud-mpfr.onrender.com/");
	     
	     driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click(); //boton add
		 driver.findElement(By.name("name")).click();
		 driver.findElement(By.name("name")).clear();
		 driver.findElement(By.name("name")).sendKeys("Majodelete");
		 driver.findElement(By.name("email")).click();
		 driver.findElement(By.name("email")).clear();
		 driver.findElement(By.name("email")).sendKeys("majodelete@email.com");
		 driver.findElement(By.name("age")).click();
		 driver.findElement(By.name("age")).clear();
		 driver.findElement(By.name("age")).sendKeys("30");
		 pause(2000);
		driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click(); // display flechita
		pause(2000);
		driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[2]")).click(); // female
		driver.findElement(By.xpath("//html/body/div[3]/div/div[2]/form/button")).click(); //boton de add
		pause(3000);
		driver.findElement(By.xpath("/html/body/div[3]/div/i")).click(); // boton cerrar
			pause(4000);

	     // email de majodelete@email.com
	     String userRow = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[2]")).getText();
	     
	     driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[5]/button[2]")).click(); // boton eliminar
	     pause(3000);
	     driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/button[1]")).click(); // boton "Yes"
	     pause(3000); 
	     
	     WebElement table = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table")); //toda la tabla del MERN
	     String tableText = table.getText();
	     
	     // verificar que email ya no este en la tabla
	     assertThat(tableText, not(containsString(userRow)));
	 }
	 
	 @Test
	 public void e_findRecord_test() {
		 driver.get(baseUrl);

		 driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click(); //boton add
		 driver.findElement(By.name("name")).click();
		 driver.findElement(By.name("name")).clear();
		 driver.findElement(By.name("name")).sendKeys("Majofind");
		 driver.findElement(By.name("email")).click();
		 driver.findElement(By.name("email")).clear();
		 driver.findElement(By.name("email")).sendKeys("majofind@email.com");
		 driver.findElement(By.name("age")).click();
		 driver.findElement(By.name("age")).clear();
		 driver.findElement(By.name("age")).sendKeys("18");
		 pause(4000);
		 driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click(); // display flechita
		 pause(4000);
		 driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[2]")).click(); // female
		 driver.findElement(By.xpath("//html/body/div[3]/div/div[2]/form/button")).click(); //boton de add
		 pause(3000);
		 driver.findElement(By.xpath("/html/body/div[3]/div/i")).click(); // boton cerrar
		 pause(4000);
		 
		 WebElement table = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));
		 String tableText = table.getText();
		 
		 assertThat(tableText, containsString("Majofind"));
	 }
	 
	 @Test
	 public void f_findAllRecords_test() {
		 driver.get(baseUrl);
		 //Agregar primer usuario MajoFind1
		 driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click(); //boton add
		 driver.findElement(By.name("name")).click();
		 driver.findElement(By.name("name")).clear();
		 driver.findElement(By.name("name")).sendKeys("MajoFind1");
		 driver.findElement(By.name("email")).click();
		 driver.findElement(By.name("email")).clear();
		 driver.findElement(By.name("email")).sendKeys("majofind1@email.com");
		 driver.findElement(By.name("age")).click();
		 driver.findElement(By.name("age")).clear();
		 driver.findElement(By.name("age")).sendKeys("19");
		 pause(4000);
		 driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click(); // display flechita
		 pause(4000);
		 driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[2]")).click(); // female
		 driver.findElement(By.xpath("//html/body/div[3]/div/div[2]/form/button")).click(); //boton de add
		 pause(3000);
		 //Agregar segundo usuario MajoFind2
		 //driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click(); //boton add
		 driver.findElement(By.name("name")).click();
		 driver.findElement(By.name("name")).clear();
		 driver.findElement(By.name("name")).sendKeys("MajoFind2");
		 driver.findElement(By.name("email")).click();
		 driver.findElement(By.name("email")).clear();
		 driver.findElement(By.name("email")).sendKeys("majofind2@email.com");
		 driver.findElement(By.name("age")).click();
		 driver.findElement(By.name("age")).clear();
		 driver.findElement(By.name("age")).sendKeys("20");
		 pause(4000);
		 driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click(); // display flechita
		 pause(4000);
		 driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[2]")).click(); // female
		 driver.findElement(By.xpath("//html/body/div[3]/div/div[2]/form/button")).click(); //boton de add
		 pause(3000);
		 //Agregar tercer usuario MajoFind3
		 //driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click(); //boton add
		 driver.findElement(By.name("name")).click();
		 driver.findElement(By.name("name")).clear();
		 driver.findElement(By.name("name")).sendKeys("MajoFind3");
		 driver.findElement(By.name("email")).click();
		 driver.findElement(By.name("email")).clear();
		 driver.findElement(By.name("email")).sendKeys("majofind3@email.com");
		 driver.findElement(By.name("age")).click();
		 driver.findElement(By.name("age")).clear();
		 pause(4000);
		 driver.findElement(By.name("age")).sendKeys("21");
		 pause(4000);
		 driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click(); // display flechita
		 pause(4000);
		 driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[2]")).click(); // female
		 driver.findElement(By.xpath("//html/body/div[3]/div/div[2]/form/button")).click(); //boton de add
		 pause(3000);	
		 
		 //Cerrar ventana de agregar
		 driver.findElement(By.xpath("/html/body/div[3]/div/i")).click(); // boton cerrar
		 pause(4000);
		 
		 WebElement table = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));
		 String tableText = table.getText();
		 
		 assertThat(tableText, containsString("majofind1@email.com"));
		 assertThat(tableText, containsString("majofind2@email.com"));
		 assertThat(tableText, containsString("majofind3@email.com"));

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
	  
	  private void pause(long mils) {
	  	  try {
	  		  Thread.sleep(mils);
	  	  }
	  	  catch(Exception e) {
	  		  e.printStackTrace();
	  	  }
	    }
	}
