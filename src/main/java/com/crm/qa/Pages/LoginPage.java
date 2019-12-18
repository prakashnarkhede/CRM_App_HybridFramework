package com.crm.qa.Pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.crm.qa.BaseClass.TestBase;

public class LoginPage extends TestBase 
{
	//Page Factory or Object Repository Where we store all WebElements.
	//@FindBy will automatically do => driver.findElement(By.name(""));
	@FindBy(name="username")
	WebElement username;
	
	@FindBy(name="password")
	WebElement password;
	
	@FindBy(xpath="//input[@class='btn btn-small' and @type='submit']") 
	WebElement loginButton;
	
	@FindBy(xpath="//button[contains(text(),'Sign Up')]")
	WebElement signUpButton;
	
	@FindBy(xpath="//img[contains(@class,'img-responsive')]")
	WebElement crmLogo;
	
	//Initializing [Page Objects] all Object Repository Elements with help of Page Factory in Constructor.
	//We initialize Page Factory Using initElements(driver, this) //This refers to Current Class Object.
	public LoginPage()
	{
		PageFactory.initElements(driver, this);
	}
	
	public String validateLoginPageTitle()
	{
		return driver.getTitle();
	}
	
	public boolean validateCRMImage()
	{
		return crmLogo.isDisplayed(); //It will return True Or False if the Logo is Present Or No.
	}
	
	public HomePage login(String uname, String pword)
	{
		username.sendKeys(uname);
		password.sendKeys(pword);
		
		//LoginButton.click();
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", loginButton);
				
		return new HomePage(); //Since Login Page is Landing on HomePage
	}
}

