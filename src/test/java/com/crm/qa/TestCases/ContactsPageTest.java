package com.crm.qa.TestCases;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.crm.qa.BaseClass.TestBase;
import com.crm.qa.Pages.ContactsPage;
import com.crm.qa.Pages.DealsPage;
import com.crm.qa.Pages.HomePage;
import com.crm.qa.Pages.LoginPage;
import com.crm.qa.Utilities.TestUtility;

public class ContactsPageTest extends TestBase
{
	LoginPage loginPage;
	HomePage homePage;
	TestUtility testUtil;
	ContactsPage contactsPage;
	DealsPage dealsPage;
	
	String sheetName = "Contacts"; //Passing Excel Sheet Name
	
	public ContactsPageTest()
	{
		super();
	}
	
	@Parameters("Browser")
	@BeforeMethod
	public void setUp(String Browser)
	{
		initialization(Browser);
		testUtil = new TestUtility();
		Log.info("Application Launched Successfully");
		
		loginPage = new LoginPage();
		contactsPage = new ContactsPage();
		dealsPage = new DealsPage();
		homePage = loginPage.login(property.getProperty("Username"),property.getProperty("Password"));
		testUtil.switchToFrame();
		contactsPage = homePage.clickOnContactsLink();
	}
	
	@Test(priority=1)
	public void verifyContactsPageLabelTest(Method method)
	{
		extentTest = extent.startTest(method.getName());
		Assert.assertTrue(contactsPage.verifyContactsLabel(), "Contacts Label is Missing in the Page");
		Log.info("Verified Contacts Page Label");
	}
	
	@Test(priority=2)
	public void selectSingleContactsTest(Method method)
	{
		extentTest = extent.startTest(method.getName());
		contactsPage.selectContactByName("Sai Baba");
		Log.info("Verified Single Contacts");
	}
	
	@Test(priority=3)
	public void selectMultipleContactsTest(Method method)
	{
		extentTest = extent.startTest(method.getName());
		contactsPage.selectContactByName("Sai Baba");
		contactsPage.selectContactByName("Pavan KrishnanReddy");
		Log.info("Verified Multiple Contacts");
	}
	
	//We are using Data Provider here to Access Data from Excel Sheet
	@DataProvider
	public Object[][] getCRMContactsTestData() //To Access Sheet from Test Data Sheet
	{
		Object data [][] = TestUtility.getTestData(sheetName);
		return data;
	}
	
	@Test(priority=4, dataProvider="getCRMContactsTestData")
	public void validateCreateNewContactTest(Method method, String Title, String FirstName, String LastName, String Company)
	{
		extentTest = extent.startTest(method.getName());
		homePage.clickOnNewContactLink();
		contactsPage.createNewContact(Title, FirstName, LastName, Company);
		Log.info("New Contacts Created Successfully");
	}
}
