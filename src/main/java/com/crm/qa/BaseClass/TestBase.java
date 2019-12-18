package com.crm.qa.BaseClass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.crm.qa.Utilities.TestUtility;
import com.crm.qa.Utilities.WebEventListener;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestBase
{
	public static WebDriver driver; 
	public static Properties property; //Making public So that we can use in all Child Classes.
	public static EventFiringWebDriver e_driver;
	public static WebEventListener eventListener;
	public static Logger Log;
	public static ExtentReports extent;
	public static ExtentTest extentTest;
		
	//Using Base Class we achieving Inheritance Concept from Java.
	public TestBase() //Constructor to read data from property file.
	{
		Log = Logger.getLogger(this.getClass()); //Logger Implementation.
		try 
		{
			property = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "/src/main/java/com/crm/qa/Configuration/Configuration.properties");
			property.load(ip);
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@BeforeTest
	public void setExtent()
	{
		TestUtility.setDateForLog4j();
		//Telling System Where Exactly Extent Report has to be Generated under Project.
		extent = new ExtentReports(System.getProperty("user.dir") + "/CRMExtentResults/CRMExtentReport" + TestUtility.getSystemDate() + ".html");
		extent.addSystemInfo("Host Name", "Windows 10 - Lenovo");
		extent.addSystemInfo("User Name", "Test User");
		extent.addSystemInfo("Environment", "SIT Region");
	}
	
	public static void initialization(String Browser)
	{
		if(Browser.equals("chrome"))
		{
			System.setProperty("webdriver.chrome.driver","./Drivers/chromedriver.exe");
			driver = new ChromeDriver();
		}
		else if(Browser.equals("IE"))
		{
			System.setProperty("webdriver.ie.driver", "./Drivers/IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}
		else if(Browser.equals("firefox"))
		{
			System.setProperty("webdriver.gecko.driver","./Drivers/geckodriver.exe");
			driver = new FirefoxDriver();
		}
		else
		{
			System.out.println("Path of Driver Executable is not Set for any Browser");
		}
		
		e_driver = new EventFiringWebDriver(driver);
		//Now create object of EventListerHandler to register it with EventFiringWebDriver.
		eventListener = new WebEventListener();
		e_driver.register(eventListener);
		driver = e_driver;
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(TestUtility.Page_Load_TimeOut, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TestUtility.Implicit_Wait, TimeUnit.SECONDS);
		
		driver.get(property.getProperty("Url"));
	}
	
	@AfterTest
	public void endReport()
	{
		extent.flush();
		extent.close();
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) throws IOException
	{
		if(result.getStatus()==ITestResult.FAILURE)
		{
			extentTest.log(LogStatus.FAIL, "Test Case Failed is "+result.getName()); //To Add Name in Extent Report.
			extentTest.log(LogStatus.FAIL, "Test Case Failed is "+result.getThrowable()); //To Add Errors and Exceptions in Extent Report.
		
			String screenshotPath = TestUtility.getScreenshot(driver, result.getName());
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath)); //To Add Screenshot in Extent Report.
		}
		else if(result.getStatus()==ITestResult.SKIP)
		{
			extentTest.log(LogStatus.SKIP, "Test Case Skipped is " + result.getName());
		}
		else if(result.getStatus()==ITestResult.SUCCESS)
		{
			extentTest.log(LogStatus.PASS, "Test Case Passed is " + result.getName());
		}
		extent.endTest(extentTest); //Ending Test and Ends the Current Test and Prepare to Create HTML Report.
		driver.quit();
		Log.info("Browser Terminated");
		Log.info("-----------------------------------------------");
	}
}
