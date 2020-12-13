/**  
   FunctionLibrary is a java class which contains basic webdriver methods e.g. 
    1) Dealing with check-boxes - Check/UnCheck Check-box etc.
    2) Dealing with Input-boxes - Clear Input-box, Entering value into Input-box etc.
    3) Dealing with links - Click link
    4) Verify texts/conditions appear on the webpage - Verify text, verify partial text, verify number, verify page source etc.
    5) Wait methods - Wait for an element to load, wait for page to load, wait for new window to appear etc. 

 */

package scripts;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import java.util.regex.Pattern;


import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Utility.TestUtil;




public class FunctionLibrary extends DriverScript {

	/**
	 * Waits for check-box to appear on the page. Once appeared, highlight the
	 * element and uncheck it if not checked already.
	 * <p>
	 * This method <code>returns</code>:
	 * <ul>
	 * <li>Pass if already unchecked or successfully unchecked</li>
	 * <li>Fail if any exception occurs in between</li>
	 * </ul>
	 * 
	 * @param locator
	 *            Element locator
	 * @param elemName
	 *            Element name
	 * 
	 * @return Pass/Fail
	 */

	public static String uncheckCheckBox(By locator, String elemName) {

		APPLICATION_LOGS.debug("Unchecking the checkbox : " + elemName);

		try {

			// Highlight check-box
			FunctionLibrary.highlightElement(driver, locator);

			// Wait for check-box to appear on the page
			waitForElementToLoad(locator);

			// UnCheck check-box if already checked
			if (driver.findElement(locator).isSelected()) {
				driver.findElement(locator).click();
			}

			// Log the result
			APPLICATION_LOGS.debug("Unchecked '" + elemName + "'");

			return "Pass : Unchecked '" + elemName + "'";

		}

		catch (Throwable uncheckCheckBoxException) {

			// Log the exception
			APPLICATION_LOGS
			.debug("Error came while unchecking '" + elemName + "' : " + uncheckCheckBoxException.getMessage());

			return "Fail : Error came while unchecking '" + elemName + "' : " + uncheckCheckBoxException.getMessage();

		}

	}


	/**
	 *
	 * Scrolls to the text  mentioned in the parameter
	 * Returns pass or fail depending on whether scroll was
	 * successful or not
	 * 
	 * @param text
	 * 
	 * @return Pass/Fail
	 */    
	public static String javaScriptScroll(String text)
	{

		APPLICATION_LOGS.debug("Scrolling to the text: "+text);	

		String locator = null;
		int InScroll=0;
		int ScrollTo=500;

		int flagForFailSafe = 0;

		locator="//*[contains(text(),'"+text+"')]";

		try
		{
			//scroll down
			while(!(FunctionLibrary.isElementDisplayed(By.xpath(locator), text))){

				if(flagForFailSafe<5)
				{
					System.out.println("Scrolling...");
					((JavascriptExecutor)driver).executeScript("scroll("+InScroll+","+ScrollTo+")");
					InScroll=ScrollTo;
					ScrollTo=ScrollTo+200;
					++flagForFailSafe;
					Thread.sleep(1000l);
				}
				else
				{
					APPLICATION_LOGS.debug("Fail: Element: "+text+" not found after scrolling 5 times");
					System.out.println("Element: "+text+" not found after scrolling 5 times");
					return "Fail: Element: "+text+" not found after scrolling 5 times";
				}

			}
		}
		catch (Exception exception)
		{
			// report error
			APPLICATION_LOGS
			.debug("Error while scrolling to the text: " + exception.getMessage());
			APPLICATION_LOGS.debug("Please check if the element is displayed or not");

			return "Fail : scrolling to the text: " + exception.getMessage();
		}

		((JavascriptExecutor)driver).executeScript("scroll("+InScroll+","+ScrollTo+")");

		return "Pass: Successfully scrolled to the text: "+text;
	}





	/**
	 * Scrolls to the given element
	 * 
	 * @param locator, name
	 * 
	 * @param locator
	 * @param name
	 * @return
	 */
	public static String javaScriptScrollToElement(By locator, String name)
	{

		APPLICATION_LOGS.debug("Scrolling to the element: "+name);    

		int InScroll=0;
		int ScrollTo=500;

		int flagForFailSafe = 0;

		try
		{
			//scroll down
			while(!(FunctionLibrary.isElementDisplayed(locator, name))){

				if(flagForFailSafe<5)
				{
					System.out.println("Scrolling...");
					((JavascriptExecutor)driver).executeScript("scroll("+InScroll+","+ScrollTo+")");
					InScroll=ScrollTo;
					ScrollTo=ScrollTo+200;
					++flagForFailSafe;
					Thread.sleep(1000l);
				}
				else
				{
					APPLICATION_LOGS.debug("Fail: Element: "+name+" not found after scrolling 5 times");
					System.out.println("Element: "+name+" not found after scrolling 5 times");
					return "Fail: Element: "+name+" not found after scrolling 5 times";
				}

			}
		}
		catch (Exception exception)
		{
			// report error
			APPLICATION_LOGS
			.debug("Error while scrolling to the text: " + exception.getMessage());
			APPLICATION_LOGS.debug("Please check if the element is displayed or not");

			return "Fail : scrolling to the text: " + exception.getMessage();
		}

		((JavascriptExecutor)driver).executeScript("scroll("+InScroll+","+ScrollTo+")");

		return "Pass: Successfully scrolled to the text: "+name;
	}


	/**
	 * Asserts whether expected text is present on the page or not. Returns Pass
	 * if present on the page. Returns Fail if not present on the page.
	 * 
	 * @param expText
	 *            Expected text
	 * 
	 * @return Pass/Fail
	 */

	public static String verifyTextPresent(String expText) {

		APPLICATION_LOGS.debug("Verifying Text : '" + expText + "' " + "present in the Page Source");

		try {

			// Verify page source contains expected text
			Assert.assertTrue(driver.getPageSource().contains(expText));

			// Log result
			APPLICATION_LOGS.debug("'" + expText + "' present in the Page Source");

			return "Pass : '" + expText + "' present in the Page Source";

		}

		catch (Throwable verifyTextPresentError) {

			// report error
			APPLICATION_LOGS
			.debug("Error while Verifying Text from Page Source : " + verifyTextPresentError.getMessage());

			return "Fail : Error while Verifying Text from Page Source : " + verifyTextPresentError.getMessage();

		}

	}

	/**
	 * Waits for element to appear on the page. Once appeared, highlight the
	 * element and clicks on it. Returns Pass if able to click on the element.
	 * Returns Fail if any exception occurs in between.
	 * 
	 * @param locator
	 *            Element locator
	 * @param elemName
	 *            Element name
	 * 
	 * @return Pass/Fail
	 */

	public static String clickLink(By locator, String elemName) {

		APPLICATION_LOGS.debug("Clicking on : " + elemName);

		try {

			// Wait for link to appear on the page
			waitForElementToLoad(locator);

			// Highlight link
			FunctionLibrary.highlightElement(driver, locator);

			// Click on the link
			driver.findElement(locator).click();

			// Log result
			//System.out.println("Clicked on : " + elemName);
			APPLICATION_LOGS.debug("Clicked on : " + elemName);

			return "Pass : Clicked on : " + elemName;

		}

		catch (Throwable clickLinkException) {

			// Log error
			APPLICATION_LOGS.debug("Error while clicking on - " + elemName + " : " + clickLinkException.getMessage());
			TestUtil.takeScreenShot(screenshotPath+elemName+"_Clickerror"+ ".jpg");
			return "Fail : Error while clicking on - " + elemName + " : " + clickLinkException.getMessage();

		}

	}


	/**
	 * Waits for element to appear on the page. Once appeared, highlight the
	 * element and clicks on it. Returns Pass if able to click on the element.
	 * Returns Fail if any exception occurs in between.
	 * 
	 * @param parLoc
	 *            Parent Element locator
	 * @param childLoc
	 *            Child Element locator
	 * @param elemName
	 *            Element name
	 * 
	 * @return Pass/Fail
	 */

	public static String clickLink(By parentLoc, By childLoc, String elemName) {

		APPLICATION_LOGS.debug("Clicking on : " + elemName);

		try {

			// Wait for parent element to load
			waitForElementToLoad(parentLoc);

			// Highlight element
			FunctionLibrary.highlightElement(driver, childLoc);

			// Click on the child element which is under parent element
			driver.findElement(parentLoc).findElement(childLoc).click();

			// Log result
			APPLICATION_LOGS.debug("Clicked on : " + elemName);

			return "Pass : Clicked on : " + elemName;

		}

		catch (Throwable clickLinkException) {

			// Log error
			APPLICATION_LOGS.debug("Error while clicking on - " + elemName + " : " + clickLinkException.getMessage());

			return "Fail : Error while clicking on - " + elemName + " : " + clickLinkException.getMessage();

		}

	}

	/**
	 * Waits for input box to appear on the page. Once appeared, highlight and
	 * clears the box. Returns Pass if Input box got cleared successfully.
	 * Returns Fail if input box didn't clear or any exception occurs in
	 * between.
	 * 
	 * @param locator
	 *            Element locator
	 * @param elemName
	 *            Element name
	 * 
	 * @return Pass/Fail
	 */

	public static String clearField(By locator, String elemName) {

		APPLICATION_LOGS.debug("Clearing field : " + elemName);

		try {

			// Wait for the input-box to load on the page
			waitForElementToLoad(locator);

			// Highlight the input-box
			FunctionLibrary.highlightElement(driver, locator);

			// Clear the input-box
			driver.findElement(locator).clear();

			// Check whether input-box has been cleared or not
			if (!driver.findElement(locator).getAttribute("value").isEmpty())
				driver.findElement(locator).clear();

			// Log result
			APPLICATION_LOGS.debug("Cleared : " + elemName);

			return "Pass : Cleared : " + elemName;

		}

		catch (Throwable clearFieldException) {

			// Log error
			APPLICATION_LOGS.debug("Error while clearing - " + elemName + " : " + clearFieldException.getMessage());

			return "Fail : Error while clearing - " + elemName + " : " + clearFieldException.getMessage();

		}

	}


	public static String input(By locator, String elemName, String Value) {

		APPLICATION_LOGS.debug("Sending Values in : " + elemName);

		try {

			// Wait for the input box to appear on the page
			waitForElementToLoad(locator);

			// Highlight the input box
			FunctionLibrary.highlightElement(driver, locator);

			// Send values to the input box
			driver.findElement(locator).sendKeys(Value);

			// Log result
			APPLICATION_LOGS.debug("Inputted '" + Value + "' text into : '" + elemName + "'");

			return "Pass : Inputted '" + Value + "' text into : '" + elemName + "'";

		}

		catch (Throwable inputException) {

			// Log error
			APPLICATION_LOGS.debug("Error while inputting into - '" + elemName + "' : " + inputException.getMessage());

			return "Fail : Error while inputting into - '" + elemName + "' : " + inputException.getMessage();

		}

	}


	public static String clearAndInput(By locator, String elemName, String Value) {

		try {

			// Wait for the input box to appear on the page
			waitForElementToLoad(locator);

			// Highlight the input box
			FunctionLibrary.highlightElement(driver, locator);

			// Clear the input field before sending values
			FunctionLibrary.clearField(locator, elemName);

			// Send values to the input box
			APPLICATION_LOGS.debug("Sending Values in : " + elemName);
			driver.findElement(locator).sendKeys(Value);

			// Log result
			APPLICATION_LOGS.debug("Inputted '" + Value + "' text into : '" + elemName + "'");
			//System.out.println("Inputted '" + Value + "' text into : '" + elemName + "'");

			return "Pass : Inputted '" + Value + "' text into : '" + elemName + "'";

		}

		catch (Throwable inputException) {

			// Log error
			APPLICATION_LOGS.debug("Error while inputting into - '" + elemName + "' : " + inputException.getMessage());
			System.out.println("Error while inputting into - '" + elemName + "' : " + inputException.getMessage());
			TestUtil.takeScreenShot(screenshotPath+elemName+"_Clickerror"+ ".jpg");

			return "Fail : Error while inputting into - '" + elemName + "' : " + inputException.getMessage();

		}

	}


	/**
	 * public static String assertText(String elemName,String actValue, String
	 * expValue) method specification :-
	 * 
	 * 1) Verifies and returns TRUE if expected and actual text match 2)
	 * elemName -> the name/type of text we intend to compare 3) actValue -> the
	 * actual string value which is shown in the application 4) expValue -> the
	 * expected string value which should be shown in the application 5)
	 * Assert.assertEquals(expValue.trim(), actValue.trim())) -> trims and
	 * compares the actual and expected string value
	 * 
	 * @param :
	 *            Name of the web element, Actual text and expected text
	 * 
	 * @return : Result of execution - Pass or fail (with cause)
	 */

	public static String assertText(String elemName, String actValue, String expValue) {

		APPLICATION_LOGS.debug("Asserting text for : '" + elemName + "' where Expected text is '" + expValue
				+ "' and Actual text is '" + actValue + "'");
		System.out.println("Asserting text for : '" + elemName + "' where Expected text is '" + expValue
				+ "' and Actual text is '" + actValue + "'");

		try {

			// Assert that expected value matches with actual value
			Assert.assertEquals(expValue.trim(), actValue.trim());

			// Log result
			APPLICATION_LOGS.debug("Successfully asserted text for : '" + elemName + "' where Expected text is '"
					+ expValue + "' and Actual text is '" + actValue + "'");

			System.out.println("Successfully asserted text for : '" + elemName + "' where Expected text is '"
					+ expValue + "' and Actual text is '" + actValue + "'");


			return "Pass : Expected text matches with actual text";

		}

		catch (Throwable assertTextException) {

			// Log error
			APPLICATION_LOGS
			.debug("Error while Asserting Text for - '" + elemName + "' : " + assertTextException.getMessage());

			return "Fail : Error while Asserting Text for - '" + elemName + "' : " + assertTextException.getMessage();

		}

	}


	public static String assertText(String elemName, int actValue, int expValue) {

		APPLICATION_LOGS
		.debug("Asserting  Text  where : ExpectedText = " + expValue + "  and ActualText = " + actValue);

		try {

			// Assert that expected value matches with actual value
			Assert.assertEquals(expValue, actValue);

			// Log result
			APPLICATION_LOGS.debug("Successfully asserted text for : '" + elemName + "' where Expected text is '"
					+ expValue + "' and Actual text is '" + actValue + "'");

			return "Pass : Expected text matches with actual text";

		}

		catch (Throwable assertTextException) {

			// Log error
			APPLICATION_LOGS
			.debug("Error while Asserting Text for - '" + elemName + "' : " + assertTextException.getMessage());

			return "Fail : Error while Asserting Text for - '" + elemName + "' : " + assertTextException.getMessage();

		}

	}

	public static String selectValueByVisibleText(By Locator, String Option, String elemName) {

		APPLICATION_LOGS.debug("Selecting '" + Option + "' from : " + elemName);

		try {

			// Wait for drop-down element to load on the page
			waitForElementToLoad(Locator);

			// Highlight the drop-down
			FunctionLibrary.highlightElement(driver, Locator);

			// Locate drop-down field
			Select select = new Select(driver.findElement(Locator));

			// Select value from drop-down
			select.selectByVisibleText(Option);

			// Log result
			APPLICATION_LOGS.debug("Selected '" + Option + "' from : " + elemName);

			return "Pass : Selected '" + Option + "' from : " + elemName;

		}

		catch (Throwable selectValueException) {

			// Log error
			APPLICATION_LOGS.debug(
					"Error while Selecting Value from - '" + elemName + "' : " + selectValueException.getMessage());

			return "Fail : Error while Selecting Value from - '" + elemName + "' : "
			+ selectValueException.getMessage();

		}

	}


	public static String selectValueByIndex(By Locator, int index, String elemName) {

		APPLICATION_LOGS.debug("Selecting value from : " + elemName);
		//System.out.println("Selecting value from : " + elemName);

		try {

			// Wait for drop-down element to load on the page
			waitForElementToLoad(Locator);

			// Highlight the drop-down
			FunctionLibrary.highlightElement(driver, Locator);

			// Locate drop-down field
			Select select = new Select(driver.findElement(Locator));

			// Select value from drop-down
			select.selectByIndex(index);

			// Log result
			APPLICATION_LOGS.debug("Selected value from : " + elemName);

			return "Pass : Selected value from : " + elemName;

		}

		catch (Throwable selectValueException) {

			// Log error
			APPLICATION_LOGS.debug(
					"Error while Selecting Value from - '" + elemName + "' : " + selectValueException.getMessage());
			System.out.println(
					"Error while Selecting Value from - '" + elemName + "' : " + selectValueException.getMessage());

			return "Fail : Error while Selecting Value from - '" + elemName + "' : "
			+ selectValueException.getMessage();

		}

	}


	public static String retrieveText(By locator, String elemName) {

		String retrievedText = null;

		APPLICATION_LOGS.debug("Retrieving Text from : " + elemName);

		try {

			// Wait for web element to load on the page
			waitForElementToLoad(locator);

			// Highlight the web element
			//FunctionLibrary.highlightElement(driver, locator);

			// Retrieve text from web element
			retrievedText = driver.findElement(locator).getText().trim();

			// Log result
			APPLICATION_LOGS.debug("Retrieved text : " + retrievedText);

		}

		catch (Throwable retrieveTextException) {

			// Log error
			APPLICATION_LOGS
			.debug("Error while Getting Text from '" + elemName + "' : " + retrieveTextException.getMessage());
			TestUtil.takeScreenShot(screenshotPath+elemName+"_Clickerror"+ ".jpg");

		}

		return retrievedText;

	}


	public static String retrieveAttributeValue(By locator, String value, String elemName) {

		String attributeValue = null;

		APPLICATION_LOGS.debug("Getting Attribute '" + value + "'  Value from - " + elemName);

		try {

			// Wait for web element to load
			waitForElementToLoad(locator);

			// Highlight the web element
			FunctionLibrary.highlightElement(driver, locator);

			// Get attribute value for the web element
			attributeValue = driver.findElement(locator).getAttribute(value);

			// Log result
			APPLICATION_LOGS.debug("Got Attribute '" + value + "'  Value from : " + elemName + " : " + attributeValue);

		}

		catch (Throwable retrieveAttributeValueException) {

			// report error
			APPLICATION_LOGS.debug("Error while Getting Attribute '" + value + "' value from '" + elemName + "' : "
					+ retrieveAttributeValueException.getMessage());

			return "Fail : Error while Getting Attribute '" + value + "' value from '" + elemName + "' : "
			+ retrieveAttributeValueException.getMessage();

		}

		return attributeValue;

	}


	public static void waitForPageToLoad() throws InterruptedException {

		try {

			// Waits for 60 seconds
			WebDriverWait wait = new WebDriverWait(driver, 60);
			// Wait until expected condition (All documents present on the page
			// get ready) met
			wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {

					if (!(d instanceof JavascriptExecutor))
						return true;

					Object result = ((JavascriptExecutor) d)
							.executeScript("return document['readyState'] ? 'complete' == document.readyState : true");

					if (result != null && result instanceof Boolean && (Boolean) result)
						return true;

					return false;

				}

			});

		}

		catch (Throwable waitForPageToLoadException) {
			APPLICATION_LOGS
			.debug("Error came while waiting for page to load : " + waitForPageToLoadException.getMessage());
		}

	}


	public static void waitForElementToLoad(final By locator) {

		APPLICATION_LOGS.debug("Waiting for web element to load on the page");

		try {

			// Waits for 60 seconds
			Wait<WebDriver> wait = new WebDriverWait(driver, 60);

			// Wait until the element is located on the page
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

			// Log result
			APPLICATION_LOGS.debug("Waiting ends ... Web element loaded on the page");

		}

		catch (Throwable waitForElementException) {

			// Log error
			APPLICATION_LOGS
			.debug("Error came while waiting for element to appear : " + waitForElementException.getMessage());

		}

	}


	public static ExpectedCondition<WebElement> visibilityOfElementLocated(final By locator) {

		return new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {

				// Highlight the web element
				FunctionLibrary.highlightElement(driver, locator);

				// Store the web element
				WebElement toReturn = driver.findElement(locator);

				// Check whether the web element is displayed on the page
				if (toReturn.isDisplayed())
					return toReturn;

				return null;

			}

		};

	}


	public static void waitForElementToDisappear(final By locator, String elemName) {

		APPLICATION_LOGS.debug("Waiting for " + elemName + " to disappear ...");

		try {

			// Waits for 60 seconds
			Wait<WebDriver> wait = new WebDriverWait(driver, 60);

			// Wait until the element get disappeared
			@SuppressWarnings("unused")
			WebElement element = wait.until(ElementLocatedToGetDisappear(locator));

			// Log result
			APPLICATION_LOGS.debug("Waiting ends ... " + elemName + " disappeared");

		}

		catch (Throwable waitForElementException) {

			// Log error
			APPLICATION_LOGS.debug(
					"Error came while waiting for element to disappear : " + waitForElementException.getMessage());

		}

	}


	public static ExpectedCondition<WebElement> ElementLocatedToGetDisappear(final By locator) {

		return new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {

				// Store the web element
				WebElement toReturn = driver.findElement(locator);

				// Check whether the web element is disappeared
				if (!toReturn.isDisplayed())
					return toReturn;

				return null;

			}

		};

	}


	public static String acceptAlert(String elemName) {

		APPLICATION_LOGS.debug("Accepting alert : " + elemName);

		try {

			// Create a new alert object
			Alert alert = driver.switchTo().alert();

			// Accept the alert
			alert.accept();

			// Log result
			APPLICATION_LOGS.debug("Accepted alert : " + elemName);

			return "Pass : Accepted the alert '" + elemName + "'";

		}

		catch (Throwable acceptAlertException) {

			// Log error
			APPLICATION_LOGS.debug("Error came while accepting alert : " + acceptAlertException.getMessage());

			return "Fail : Error came while accepting alert : " + acceptAlertException.getMessage();

		}

	}

	/*
	 * public static void clickAndWait(By locator,String elemName) method
	 * specification :-
	 * 
	 * 1) Click and wait for next page to load 2)
	 * driver.findElement(locator).click() -> Clicks on the web element targeted
	 * by locator
	 * 
	 * @param : Locator to locate the web element, Name of the web element
	 * 
	 * @return : Result of execution - Pass or fail (with cause)
	 */

	public static String clickAndWait(By locator, String elemName) {

		try {

			// Click on the web element targeted by locator
			clickLink(locator, elemName);

			// Wait for new page to load
			FunctionLibrary.waitForPageToLoad();

			// Log result
			APPLICATION_LOGS.debug(
					"Clicked on the element : " + elemName + " and new page loaded with title : " + driver.getTitle());

			return "Pass : Clicked on the element : " + elemName + " and new page loaded with title : "
			+ driver.getTitle();

		}

		catch (Throwable clickAndWaitException) {

			// Log error
			APPLICATION_LOGS.debug("Error while clicking on " + elemName + " and waiting for new page to load : "
					+ clickAndWaitException.getMessage());
			TestUtil.takeScreenShot(screenshotPath+elemName+"_Clickerror"+ ".jpg");

			return "Fail: Error while clicking on link " + elemName + " and waiting for new page to load : "
			+ clickAndWaitException.getMessage();

		}

	}

	/*
	 * public static String assertTitle(String expectedTitle) method
	 * specification :-
	 * 
	 * 1) Asserts page title 2) driver.getTitle() -> Retrieves page title 3)
	 * Assert.assertEquals() -> Asserts for equality
	 * 
	 * @param : Expected title to assert
	 * 
	 * @return : Result of execution - Pass or fail (with cause)
	 */

	public static String assertTitle(String expectedTitle) {

		String actualTitle = null;

		APPLICATION_LOGS.debug("Asserting  title  where : Expected title = " + expectedTitle);

		try {

			// Fetch actual title of the webpage
			actualTitle = driver.getTitle();

			// Asserts whether actual title matches with expected one
			Assert.assertEquals(expectedTitle.trim(), actualTitle.trim());

			// Log result
			APPLICATION_LOGS
			.debug("Actual title = " + actualTitle + " and matches with Expected title = " + expectedTitle);

			return "Pass : Actual title = " + actualTitle + " and matches with Expected title = " + expectedTitle;

		}

		catch (Throwable assertTitleException) {

			// Log error
			APPLICATION_LOGS.debug("Error while asserting title : " + assertTitleException.getMessage());

			return "Fail : Error while asserting title : " + assertTitleException.getMessage();

		}

	}

	/*
	 * public static String assertAlertAndAccept(String expectedAlertText)
	 * method specification :-
	 * 
	 * driver.switchTo().alert() -> Switch to the alert appeared on the page 3)
	 * Assert.assertEquals() -> Asserts for equality 4) alert.accept() ->
	 * Accepts the alert
	 * 
	 * @param : Expected alert text to assert
	 * 
	 * @return : Result of execution - Pass or fail (with cause)
	 */

	public static String assertAlertAndAccept(String expectedAlertText) {

		APPLICATION_LOGS.debug("Asserting alert text : " + expectedAlertText);

		String actualAlertText = null;
		Alert alert = null;

		try {

			// Switch control to alert
			alert = driver.switchTo().alert();

			// Get the actual alert message
			actualAlertText = alert.getText();

			// Assert alert message
			Assert.assertEquals(expectedAlertText.trim(), actualAlertText.trim());
			Thread.sleep(3000L);

			// Accept alert message
			alert.accept();
			Thread.sleep(3000L);

			// log result
			APPLICATION_LOGS.debug("Success : got the alert message saying : " + actualAlertText);

			return "Pass : got the alert message saying : '" + actualAlertText;

		}

		catch (Throwable alertExcpetion) {

			APPLICATION_LOGS.debug("Error came while asserting alert and accepting : " + alertExcpetion.getMessage());
			return "Fail : Error came while asserting alert and accepting : " + alertExcpetion.getMessage();

		}

	}

	// highlight the element on which action will be performed
	public static void highlightElement(WebDriver driver, By Locator) {

		try {

			for (int i = 0; i < 3; i++) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].setAttribute('style', arguments[1]);", driver.findElement(Locator),
						"color: red; border: 2px solid red;");

			}

		}

		catch (Throwable t) {
			APPLICATION_LOGS.debug("Error came : " + t.getMessage());
		}

	}





	/**
	 * public static String assertTextFromLocator(String expValue,String elemName,
	 * String actValue, By locator) method specification :-
	 * 
	 * 
	 * @param :
	 * Name of the web element, Actual text and expected text 
	 * 
	 * @return : Result of execution - Pass or fail (with cause)
	 */

	public static String assertTextFromLocator(String expectedText, String actualText, String elementName) {


		APPLICATION_LOGS.debug("Asserting whether expected text: "+expectedText+" is equal to actual text: "+actualText+" for element:"+elementName);

		try
		{
			if(expectedText.contains(actualText))

				APPLICATION_LOGS.debug("Pass: Expected text "+expectedText+" is equal to actual text: "+actualText+" for element: "+elementName);
			else
			{
				APPLICATION_LOGS.debug("Fail: Expected text "+expectedText+" did not match to actual text: "+actualText+" for element: "+elementName);
				System.out.println("Fail: Expected text "+expectedText+" did not match to actual text: "+actualText+" for element: "+elementName);
				return "Fail: Expected text "+expectedText+" did not match to actual text: "+actualText+" for element: "+elementName;	
			}
		}
		catch(Exception exception)
		{
			APPLICATION_LOGS.debug("Error while asserting Text for - '" + elementName + "' : " + exception.getMessage());
			return "Fail: Error while asserting Text for - '" + elementName + "' : " + exception.getMessage();
		}

		return "Pass: Expected text "+expectedText+" is equal to actual text: "+actualText+" for element: "+elementName;

	}

	public static String checkCheckBox(By locator, String elemName) {

		APPLICATION_LOGS.debug("Checking : " + elemName);

		try {

			// Wait for element to load
			waitForElementToLoad(locator);

			// Highlight the element
			FunctionLibrary.highlightElement(driver, locator);

			// Select the element if not selected already
			if (!driver.findElement(locator).isSelected()) {

				driver.findElement(locator).click();

				APPLICATION_LOGS.debug("Checked " + elemName);
				return "Pass : Checked " + elemName;

			}

			else {

				APPLICATION_LOGS.debug(elemName + " is already checked");

				return "Pass : " + elemName + " is already checked";

			}

		}

		catch (Throwable checkCheckBoxException) {

			APPLICATION_LOGS.debug("Error while checking -" + elemName + checkCheckBoxException.getMessage());
			return "Fail : Error while checking -" + elemName + checkCheckBoxException.getMessage();

		}

	}

	
	public static void inputInteger(By locator, String elemName, int Value) {

		APPLICATION_LOGS.debug("Sending Values in : " + elemName);

		try {

			waitForElementToLoad(locator);
			String Value1 = Integer.toString(Value);
			driver.findElement(locator).sendKeys(Value1);

		}

		catch (Throwable t) {

			APPLICATION_LOGS.debug("Error while Sending Values in:  -" + elemName + t.getMessage());
		}

	}

	/**
	 * 
	 * Click on an element using javascript
	 * 
	 * @param locator
	 * @param elementName
	 * @return Pass / Fail
	 */
	public static String javaScriptClick(By locator, String elementName)
	{

		APPLICATION_LOGS.debug("Clicking on element: "+elementName);

		try
		{
			WebElement element = driver.findElement(locator);
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", element);
		}
		catch (Exception exception)
		{
			APPLICATION_LOGS.debug("Fail: Unable to click on element: "+elementName);
			System.out.println("Fail: Unable to click on element: "+elementName);
			return "Fail: Unable to click on element: "+elementName;	
		}

		return "Pass: Successfully clicked on: "+elementName;
	}



	/*
	 * This click will wait for the element to become clickable
	 * 
	 * 
	 */
	public static String waitForElementToBeClickable(By locator, String elemName) {

		try {


			// Highlight the element
			FunctionLibrary.highlightElement(driver, locator);

			// Wait for element to be clickable
			WebDriverWait wait = new WebDriverWait(driver, 10); 
			(wait.until(ExpectedConditions.elementToBeClickable(locator))).click();

			// Log result
			APPLICATION_LOGS.debug(
					"Clicked on the element : " + elemName);

			return "Pass : Clicked on the element : " + elemName;

		}

		catch (Throwable clickAndWaitException) {

			// Log error
			APPLICATION_LOGS.debug("Error while clicking on " + elemName+": "+ clickAndWaitException.getMessage());

			return "Error while clicking on link " + elemName + ": "
			+ clickAndWaitException.getMessage();

		}

	}










	/*
	 * public static void refreshPage() method specification : -
	 * 
	 * 1) Refresh the page 2) driver.navigate().refresh : This is used to
	 * refresh the current page
	 */
	public static void refreshPage() {

		APPLICATION_LOGS.debug("Refreshing the page ...");

		try {

			driver.navigate().to(driver.getCurrentUrl());
			APPLICATION_LOGS.debug("Page successfully refreshed");

		}

		catch (Throwable pageRefreshException) {

			APPLICATION_LOGS.debug("Exception came while refreshing page : " + pageRefreshException.getMessage());

		}

	}

	// Function for Selecting Value from Dropdown
	public static void selectValue(By Locator, String Value, String elemName) {

		APPLICATION_LOGS.debug("Selecting Value from : " + elemName);

		try {

			waitForElementToLoad(Locator);
			FunctionLibrary.highlightElement(driver, Locator);
			Select select = new Select(driver.findElement(Locator));
			select.selectByVisibleText(Value);
		}

		catch (Exception e) {
			APPLICATION_LOGS.debug("Error while Selecting Value from :   -" + elemName + e.getMessage());
		}

	}

	// Generate randomn characters
	public static String randomGenerator() {

		int PASSWORD_LENGTH = 8;
		String randomnCharacters;
		StringBuffer sb = new StringBuffer();

		for (int x = 0; x < PASSWORD_LENGTH; x++) {

			sb.append((char) ((int) (Math.random() * 26) + 97));

		}

		randomnCharacters = sb.toString();
		return randomnCharacters;

	}

	/**
	 * public static String selectRandomValueByIndex(By Locator, int index,
	 * String elemName) method specification :-
	 *
	 * 1) Select value from drop-down by index 2) Select -> This is a in-built
	 * class in Selenium which is used to represent a drop-down 3)
	 * select.selectByIndex(index) -> Select by index
	 *
	 * @param :
	 *            Locator for the drop-down field, Index for the option to be
	 *            selected, Name of the web element
	 *
	 * @return : Result of execution - Pass or fail (with cause)
	 */

	public static String selectRandomValueByIndex(By Locator, String elemName) {

		APPLICATION_LOGS.debug("Selecting random value from : " + elemName);

		try {

			// Wait for drop-down element to load on the page
			waitForElementToLoad(Locator);

			// Highlight the drop-down
			FunctionLibrary.highlightElement(driver, Locator);

			// Locate drop-down field
			Select select = new Select(driver.findElement(Locator));

			// Get all options from drop-down
			List<WebElement> dropDownOptions = select.getOptions();
			int selectOption = Integer.parseInt(randomNumberGenerator(1, dropDownOptions.size()));

			// Select random value from drop-down
			select.selectByIndex(selectOption);

			// Log result
			APPLICATION_LOGS.debug("Selected value from : " + elemName);

			return "Pass : Selected value from : " + elemName;

		}

		catch (Throwable selectValueException) {

			// Log error
			APPLICATION_LOGS.debug(
					"Error while Selecting Value from - '" + elemName + "' : " + selectValueException.getMessage());

			// Log error
			System.out.println(
					"Error while Selecting Value from - '" + elemName + "' : " + selectValueException.getMessage());


			return "Fail : Error while Selecting Value from - '" + elemName + "' : "
			+ selectValueException.getMessage();

		}

	}

	// Generates a random phone number with format
	public static String randomPhoneNumber() {
		Random rand = new Random();
		int num1, num2, num3;
		num1 = rand.nextInt(900) + 100;
		num2 = rand.nextInt(643) + 100;
		num3 = rand.nextInt(9000) + 1000;
		return "(" + num1 + ")" + num2 + "-" + num3;
	}



	/**
	 * Generates a random number within given range
	 * 
	 * 
	 * @param low
	 * @param high
	 * @return random number
	 */
	public static int randomNumber(int low, int high) {

		Random randomNumber = new Random();
		int result = randomNumber.nextInt(high-low) + low;

		return result;
	}

	/*
	 * 
	 * Generate randomn numbers within the range provided inside the method
	 * argument
	 * 
	 * @param : Min Value in the range, Max Value in the range
	 * 
	 * @return : Random number generated
	 */

	public static String randomNumberGenerator(int minValue, int maxValue) {

		int randomnNumber;
		String numbers;
		randomnNumber = (int) (minValue + ((new Random()).nextDouble() * (maxValue - minValue)));
		numbers = Integer.toString(randomnNumber);
		return numbers;

	}

	/**
	 * Generate randomn numbers within the high range provided inside the method
	 * argument
	 * 
	 * @param maxValue
	 * @return random number generated between 0 and maxValue, the maxValue
	 *         being excluded
	 * 
	 */

	public static int randomNumberGenerator(int maxValue) {

		Random random = new Random();
		int randomNumber = random.nextInt(maxValue);
		return randomNumber;

	}

	// Generate random alphanumeric value depending on the length given as
	// paramater
	public static String randomAlphanumeric(int len) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	/*
	 * public static void isChecked(By locator, String elemName) method
	 * specification :-
	 * 
	 * 1) Verifies whether a Checkbox is checked or not 2) locator -> to locate
	 * the element by id,x-path,name,etc. 3) elemName -> the name/type of the
	 * check-box which we intend to check 4)
	 * driver.findElement(locator).isSelected() -> is to verify whether the
	 * intended checkbox is checked or not
	 * 
	 * @param : Locator for the Check-box, name of the web element
	 * 
	 * @return : Result of execution - Pass or fail (with cause)
	 */

	public static String isChecked(By locator, String elemName) {

		APPLICATION_LOGS.debug("Verifying if the checkbox is checked for: " + elemName);

		String result = null;

		try {

			// Highlight check-box
			FunctionLibrary.highlightElement(driver, locator);

			// Wait for check-box to appear on the page
			waitForElementToLoad(locator);

			// Verify whether check-box if already checked
			if (driver.findElement(locator).isSelected()) {

				// Log the result
				APPLICATION_LOGS.debug("'" + elemName + "' is checked");
				System.out.println("'" + elemName + "' is checked");
				result = "Pass : '" + elemName + "' is checked";

			}

			else {

				// Log the result
				APPLICATION_LOGS.debug("'" + elemName + "' is not checked");
				System.out.println("'" + elemName + "' is not checked");
				result = "Fail : '" + elemName + "' is not checked";

			}

		}

		catch (Throwable ischeckCheckBoxException) {

			// Log the exception
			APPLICATION_LOGS.debug("Error while verifying checkbox is checked '" + elemName + "' : "
					+ ischeckCheckBoxException.getMessage());
			TestUtil.takeScreenShot(screenshotPath+elemName+"_Clickerror"+ ".jpg");
			result = "Error while verifying checkbox is checked: '" + elemName + "' : "
					+ ischeckCheckBoxException.getMessage();

		}

		return result;

	}



	// Move cursor
	public static void moveCursor() throws AWTException, InterruptedException {

		APPLICATION_LOGS.debug("Moving cursor ...");

		// Declare start and end point of the cursor
		int endX;
		int endY;
		int startX;
		int startY;

		// Use Robot utility to move cursor
		Robot robot = new Robot();
		Point startLocation = MouseInfo.getPointerInfo().getLocation();
		startX = startLocation.x;
		startY = startLocation.y;

		endX = startX + 500;
		endY = startY + 500;

		robot.mouseMove(endX, endY);

		APPLICATION_LOGS.debug("Cursor has been moved");

	}


	/*
	 * public static String convertStringToDate(String dateString, String
	 * dateFormat) method specification :-
	 * 
	 * 1) Returns date converted from a String format 2)
	 * SimpleDateFormat("MMMM dd, yyyy").parse(retrievedString) -> Converts
	 * String to Date format
	 * 
	 * @param : date in string format, date format
	 * 
	 * @return : Date which is being converted from String format
	 */

	public static Date convertStringToDate(String dateString, String dateFormat) {

		Date date = null;

		APPLICATION_LOGS.debug("Converting string to date format ...");

		try {

			// Convert to date
			date = new SimpleDateFormat(dateFormat).parse(dateString);

			// Log result
			APPLICATION_LOGS.debug("Date retrieved : " + date);

		}

		catch (Throwable convertStringToDateException) {

			// Log error
			APPLICATION_LOGS.debug(
					"Error while converting String to Date format : " + convertStringToDateException.getMessage());

		}

		return date;

	}

	/*
	 * public static Boolean compareDates(Date date1, Date date2, String
	 * elemName) method specification :-
	 * 
	 * 1) Compares 2 dates 2) date1.equals(date2) -> Compares date1 and date2
	 * 
	 * @param : First date to be compared, Second date to be compared, Name of
	 * the web element
	 * 
	 * @return : True if dates are matched
	 */

	public static Boolean compareDates(Date date1, Date date2, String elemName) {

		Boolean result = null;
		APPLICATION_LOGS.debug("Comparing dates : " + elemName);

		try {

			// Compare dates
			if (date1.equals(date2)) {

				// Log result
				APPLICATION_LOGS.debug("Date matched");
				result = true;

			}

			else {

				// Log result
				APPLICATION_LOGS.debug("Date didn't match");
				result = false;

			}

		}

		catch (Throwable compareDates) {

			// Log error
			APPLICATION_LOGS.debug("Error while comparing dates '" + elemName + "' : " + compareDates.getMessage());

		}

		if (result)
			return true;

		else
			return false;

	}

	/*
	 * public static String getFormattedDate(DateFormat dateFormat) method
	 * specification :
	 * 
	 * 1) This function gets date in the specified format 2) Date date = new
	 * Date() : Locale date and time 3) return currentSystemDate : Returns
	 * current syatem date and time
	 */

	public static String getFormattedDate(DateFormat dateFormat) {

		APPLICATION_LOGS.debug("Getting date ...");

		String currentSystemDate = null;
		Date date = new Date();
		currentSystemDate = dateFormat.format(date);

		APPLICATION_LOGS.debug("Got formatted date : " + currentSystemDate);

		return currentSystemDate;

	}

	/*
	 * public static String getFormattedDate(DateFormat dateFormat, int
	 * dayCount) method specification :
	 * 
	 * 1) This function gets date (Current/Past/Future) in the specified format
	 * 2) Calendar date = new GregorianCalendar() : Locale date and time 3)
	 * date.add(Calendar.DATE, dayCount) : Get Past/Current/Future date 4)
	 * return formattedDate : Formatted past/current/future date
	 * 
	 * @param dateFormat [Date formatter]
	 * 
	 * @param dayCount [Count of days (Current/Past/Future) e.g. 0 - For current
	 * system datetime, (+)daycount - For future datetime, (-)daycount - For
	 * past datetime]
	 * 
	 * @return Formatted past/current/future date
	 */

	public static String getFormattedDate(DateFormat dateFormat, int dayCount) {

		if (dayCount < 0)
			APPLICATION_LOGS.debug("Getting date of " + (-dayCount) + " days before ...");

		else if (dayCount == 0)
			APPLICATION_LOGS.debug("Getting current system date ...");

		else
			APPLICATION_LOGS.debug("Getting date of " + dayCount + " days after ...");

		// Get current system date and time
		Calendar date = new GregorianCalendar();

		// Get Past/Current/Future date
		date.add(Calendar.DATE, dayCount);

		// Format date
		String formattedDate = dateFormat.format(date.getTime());

		APPLICATION_LOGS.debug("Got formatted date : " + formattedDate);

		return formattedDate;

	}

	/*
	 * public static String retrieveCSSValue(By locator,String value,String
	 * elemName) method specification :-
	 * 
	 * 1) Return retrieved CSS value for a web element 2)
	 * driver.findElement(locator).getCssValue(value) -> Retrieves CSS (applied
	 * for a web element) value
	 * 
	 * @param : Locator for the web element, CSS name, Name of the web element
	 * 
	 * @return : CSS value retrieved
	 */

	public static String retrieveCSSValue(By locator, String value, String elemName) {

		String cssValue = null;

		APPLICATION_LOGS.debug("Getting CSS '" + value + "'  Value from - " + elemName);

		try {

			// Wait for web element to load
			waitForElementToLoad(locator);

			// Highlight the web element
			FunctionLibrary.highlightElement(driver, locator);

			// Get CSS value for the web element
			cssValue = driver.findElement(locator).getCssValue(value);

			// Log result
			APPLICATION_LOGS.debug("Got Attribute '" + value + "'  Value from : " + elemName + " : " + cssValue);

		}

		catch (Throwable retrieveCSSValueException) {

			// report error

			APPLICATION_LOGS.debug("Error while Getting CSS '" + value + "' value from '" + elemName + "' : "
					+ retrieveCSSValueException.getMessage());

			return "Fail : Error while Getting CSS '" + value + "' value from '" + elemName + "' : "
			+ retrieveCSSValueException.getMessage();

		}

		return cssValue;

	}

	/*
	 * public static int compareDates(String firstDate, String secondDate)
	 * method specification :-
	 * 
	 * 1) new SimpleDateFormat("MM-dd-yyyy") : Declare date formatter 2)
	 * sdf.parse(firstDate) : Parse string to date
	 * 
	 * @param : First date and second date to compare
	 * 
	 * @return : The value 0 if firstDate is equal to secondDate; a value less
	 * than 0 if firstDate is before secondDate; a value greater than 0 if
	 * firstDate is after secondDate.
	 */

	public static int compareDates(String firstDate, String secondDate) {

		APPLICATION_LOGS.debug("Comparing first date : " + firstDate + " and second date : " + secondDate + " ...");

		int valueReturnedAfterDateComparison = 0;

		try {

			// Declare date formatter
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

			// Parse both first and second date
			Date date1 = sdf.parse(firstDate);
			Date date2 = sdf.parse(secondDate);

			// Compare first and second date
			valueReturnedAfterDateComparison = date1.compareTo(date2);

		}

		catch (Throwable errorInDateComparison) {

			APPLICATION_LOGS.debug("Error came while comparing first date : " + firstDate + " and second date : "
					+ secondDate + " - " + errorInDateComparison.getMessage());

		}

		return valueReturnedAfterDateComparison;

	}

	/*
	 * public static Boolean compareLists(List<String> listOfUnitsToBePopulated,
	 * List<String> listOfUnitsPopulated) method specification :-
	 * 
	 * 1) Collections.sort() : Sort the list 2) list1.equals(list2) : Checks the
	 * equality
	 * 
	 * @param : First list and second list to compare
	 * 
	 * @return : true if lists are equal, false if lists are NOT equal.
	 */

	public static Boolean compareLists(List<String> firstList, List<String> secondList) {

		APPLICATION_LOGS.debug("Comparing two lists ...");

		// Declare variables need
		Boolean result = true;

		// Sort both the lists
		Collections.sort(firstList);
		Collections.sort(secondList);

		// Verify whether they are equal or not
		result = firstList.equals(secondList);

		if (result)
			APPLICATION_LOGS.debug(
					"First list is : " + firstList + " , Second list is : " + secondList + " and they are equal");

		else
			APPLICATION_LOGS.debug(
					"First list is : " + firstList + " , Second list is : " + secondList + " and they are NOT equal");

		return result;

	}

	/*
	 * public static void assignIdAttributeToWebElement(By locator, String
	 * elemName, String Id) method specification :-
	 * 
	 * 1) JavascriptExecutor js = (JavascriptExecutor) driver : Initialize
	 * Javascript executor 2) driver.findElement(locator) : Find the webelement
	 * 3)
	 * js.executeScript("arguments[0].setAttribute('id', arguments[1]);",element
	 * , Id) : Assign an Id to the element
	 * 
	 * @param : Element locator, Element name and ID want to replace with
	 * 
	 * @return : void
	 */

	public static void assignIdAttributeToWebElement(By locator, String elemName, String Id) {

		APPLICATION_LOGS.debug("Assigning ID attribute to " + elemName + " ...");

		try {

			// Initialize Javascript executor
			JavascriptExecutor js = (JavascriptExecutor) driver;

			// Store the webelement
			WebElement element = driver.findElement(locator);

			// Substituting/Adding an id for future reference
			js.executeScript("arguments[0].setAttribute('id', arguments[1]);", element, Id);

		}

		catch (Throwable assignIdException) {

			APPLICATION_LOGS.debug("Error came while assigning ID to the element : " + assignIdException.getMessage());

		}

		APPLICATION_LOGS.debug("Assigned ID attribute to " + elemName);

	}

	/**
	 * Scrolls within an web element.
	 * <ul>
	 * <li><strong>pixelToScrollVertically:</strong> +ve value (scroll down) -ve
	 * value (scroll top)
	 * <li><strong>pixelToScrollHorizontally:</strong> +ve value (scroll left)
	 * -ve value (scroll right))
	 * </ul>
	 * 
	 * @param locator
	 *            Element locator
	 * @param elemName
	 *            Element name
	 * @param pixelToScrollVertically
	 *            Pixel to scroll vertically
	 * @param pixelToScrollHorizontally
	 *            Pixel to scroll horizontally
	 * 
	 * @return void
	 */

	public static void scrollWithinParticularElement(By locator, String elemName, int pixelToScrollVertically,
			int pixelToScrollHorizontally) {

		APPLICATION_LOGS.debug("Scrolling within " + elemName + " ...");

		try {

			// Initialize Javascript executor
			JavascriptExecutor js = (JavascriptExecutor) driver;

			// Scroll inside web element vertically
			js.executeScript("arguments[0].scrollTop = arguments[1];", driver.findElement(locator),
					pixelToScrollVertically);

			// Scroll inside web element horizontally
			js.executeScript("arguments[0].scrollLeft = arguments[1];", driver.findElement(locator),
					pixelToScrollHorizontally);

		}

		catch (Throwable scrollException) {

			APPLICATION_LOGS.debug("Error came while scrolling within the element : " + scrollException.getMessage());

		}

		APPLICATION_LOGS.debug("Scrolled within " + elemName);

	}

	/*
	 * public static String getBrowserInfo() method specification :-
	 * 
	 * 1) JavascriptExecutor js = (JavascriptExecutor) driver : Initialize
	 * Javascript executor 2) js.executeScript("return navigator.userAgent;") :
	 * Get browser info
	 * 
	 * @param : none
	 * 
	 * @return : Browser Info
	 */

	public static String getBrowserInfo() {

		APPLICATION_LOGS.debug("Getting browser info ...");

		// Declare variables need
		String browserInfo = null;
		JavascriptExecutor js = null;

		try {

			// Initialize JavascriptExecutor
			js = (JavascriptExecutor) driver;

			// Get browser info
			browserInfo = (String) js.executeScript("return navigator.userAgent;");

			APPLICATION_LOGS.debug("Got browser info : " + browserInfo);

		}

		catch (Throwable browserInfoError) {

			APPLICATION_LOGS.debug("Error came while getting browser info : " + browserInfoError.getMessage());

		}

		// Return browser info
		return browserInfo;

	}

	/*
	 * public static String getBrowserName() method specification :-
	 * 
	 * 1) browserInfo = getBrowserInfo() : Get browser info 2)
	 * browserInfo.contains("Firefox") or browserInfo.contains("MSIE") : Get
	 * browser name
	 * 
	 * @param : none
	 * 
	 * @return : Browser Name
	 */

	public static String getBrowserName() {

		APPLICATION_LOGS.debug("Getting browser name ...");

		// Declare variables need
		String browserInfo = null;
		String browserName = null;

		// Get browser info
		browserInfo = getBrowserInfo();

		// Get browser name
		if (browserInfo.contains("Firefox"))
			browserName = "Firefox";

		else if (browserInfo.contains("MSIE"))
			browserName = "Microsoft Internet Explorer";
		else if(browserInfo.contains("Chrome"))
			browserName = "Chrome";
		APPLICATION_LOGS.debug("Got browser name : " + browserName);

		// Return browser name
		return browserName;

	}

	/*
	 * public static String getBrowserVersion() method specification :-
	 * 
	 * 1) browserInfo = getBrowserInfo() : Get browser info 2) browserInfo.split
	 * : Split browser info to get browser version
	 * 
	 * @param : none
	 * 
	 * @return : Browser Version
	 */

	public static String getBrowserVersion() {

		APPLICATION_LOGS.debug("Getting browser version ...");

		// Declare variables need
		String browserInfo = null;
		String browserVersion = null;

		// Get browser info
		browserInfo = getBrowserInfo();

		// Get browser version
		if (browserInfo.contains("Firefox"))
			browserVersion = browserInfo.split("Firefox/")[1];

		else if (browserInfo.contains("MSIE"))
			browserVersion = browserInfo.split("MSIE ")[1].split(";")[0];

		APPLICATION_LOGS.debug("Got browser version : " + browserVersion);

		// Return browser version
		return browserVersion;

	}

	/*
	 * public static String getPageTitle() method specification :-
	 * 
	 * 1) driver.getTitle() : Get current page title
	 * 
	 * @param : none
	 * 
	 * @return : Current webpage title
	 */

	public static String getPageTitle() {

		APPLICATION_LOGS.debug("Getting current webpage title ...");
		String pageTitle = null;

		try {

			// Get page title
			pageTitle = driver.getTitle();

		} catch (Throwable getPageTitleError) {

			APPLICATION_LOGS.debug("Error came while fetching page title : " + getPageTitleError.getMessage());

		}

		APPLICATION_LOGS.debug("Got current webpage title : " + pageTitle);
		return pageTitle;

	}

	/*
	 * public static String getCurrentUrl() method specification :-
	 * 
	 * driver.getCurrentUrl() : Get current url
	 * 
	 * @param : none
	 * 
	 * @return : Current webpage title
	 */

	public static String getCurrentUrl() {

		APPLICATION_LOGS.debug("Getting current Url ...");
		String currentUrl = null;

		try {

			// Get current url
			currentUrl = driver.getCurrentUrl();

		} catch (Throwable getCurrentUrlError) {

			APPLICATION_LOGS.debug("Error came while getting current Url : " + getCurrentUrlError.getMessage());

		}

		APPLICATION_LOGS.debug("Got current URL : " + currentUrl);
		return currentUrl;

	}

	/*
	 * public static String loadUrl(String url) method specification :-
	 * 
	 * driver.navigate().to(url); : Navigate to Url
	 * 
	 * @param : none
	 * 
	 * @return : (Pass) - If Url loaded successfully (Fail) - If Url NOT loaded
	 * successfully
	 */

	public static String loadUrl(String url) {

		APPLICATION_LOGS.debug("Loading Url : " + url + " ...");

		try {

			// Load url
			driver.navigate().to(url);

		} catch (Throwable loadingUrlError) {

			APPLICATION_LOGS.debug("Error came while loading Url : " + loadingUrlError.getMessage());

			return "Fail : Error came while loading Url : " + loadingUrlError.getMessage();

		}

		APPLICATION_LOGS.debug("Url successfully loaded");
		return "Pass : Url successfully loaded";

	}

	/*
	 * public static Boolean isElementPresent(By locator, String elemName)
	 * method specification :-
	 * 
	 * driver.findElement(locator) : Checking whether element present or not
	 * 
	 * @param : web element locator, web element name
	 * 
	 * @return : (true) - If element is present (false) - If element not present
	 */
	public static Boolean isElementPresent(By locator, String elemName) {

		APPLICATION_LOGS.debug("Checking whether " + elemName + " is present on the page or not ...");

		try {

			// Check whether web element is displayed or not
			driver.findElement(locator);

			APPLICATION_LOGS.debug(elemName + " is present on the page");
			return true;

		}

		catch (NoSuchElementException elementPresentError) {

			APPLICATION_LOGS.debug(elemName + " not present on the page");
			return false;

		}

	}

	/*
	 * public static Boolean isElementDisplayed(By locator, String elemName)
	 * method specification :-
	 * 
	 * driver.findElement(locator).isDisplayed() : Verifying whether element
	 * displayed or not
	 * 
	 * @param : web element locator, web element name
	 * 
	 * @return : (true) - If element is displayed (false) - If element not
	 * displayed
	 */
	public static Boolean isElementDisplayed(By locator, String elemName) {

		APPLICATION_LOGS.debug("Checking whether " + elemName + " is displayed on the page or not ...");

		Boolean isDisplayed;

		try {

			// Check whether web element is displayed or not
			isDisplayed = driver.findElement(locator).isDisplayed();

			if (isDisplayed)
				APPLICATION_LOGS.debug(elemName + " is displayed on the page");

			else
				APPLICATION_LOGS.debug(elemName + " not displayed on the page");

			return isDisplayed;

		}

		catch (Throwable elementPresentError) {

			APPLICATION_LOGS.debug(elemName + " not present on the page");
			return false;

		}

	}

	/*
	 * public static Boolean isElementEnabled(By locator, String elemName)
	 * method specification :-
	 * 
	 * driver.findElement(locator).isEnabled() : Verifying whether element
	 * enabled or not
	 * 
	 * @param : web element locator, web element name
	 * 
	 * @return : (true) - If element is enabled (false) - If element not enabled
	 */
	public static Boolean isElementEnabled(By locator, String elemName) {

		APPLICATION_LOGS.debug("Checking whether " + elemName + " is enabled on the page or not ...");

		Boolean isEnabled;

		try {

			// Check whether web element is enabled or not
			isEnabled = driver.findElement(locator).isEnabled();

			if (isEnabled)
				APPLICATION_LOGS.debug(elemName + " is enabled on the page");

			else
				APPLICATION_LOGS.debug(elemName + " not enabled on the page");

			return isEnabled;

		}

		catch (NoSuchElementException elementPresentError) {

			APPLICATION_LOGS.debug(elemName + " not present on the page");
			return false;

		}

	}

	/*
	 * public static Boolean isElementSelected(By locator, String elemName)
	 * method specification :-
	 * 
	 * driver.findElement(locator).isSelected() : Verifying whether element
	 * selected or not
	 * 
	 * @param : web element locator, web element name
	 * 
	 * @return : (true) - If element is selected (false) - If element not
	 * selected
	 */
	public static Boolean isElementSelected(By locator, String elemName) {

		APPLICATION_LOGS.debug("Checking whether " + elemName + " is selected on the page or not ...");

		Boolean isSelected;

		try {

			// Check whether web element is selected or not
			isSelected = driver.findElement(locator).isSelected();

			if (isSelected)
				APPLICATION_LOGS.debug(elemName + " is selected on the page");

			else
				APPLICATION_LOGS.debug(elemName + " not selected on the page");

			return isSelected;

		}

		catch (NoSuchElementException elementPresentError) {

			APPLICATION_LOGS.debug(elemName + " not present on the page");
			return false;

		}

	}



	public static String getOnlyDigits(String s) {
		Pattern pattern = Pattern.compile("[^0-9]");
		java.util.regex.Matcher matcher = pattern.matcher(s);
		String number = matcher.replaceAll("");
		return number;
	}

	public static String getOnlyStrings(String s) {
		Pattern pattern = Pattern.compile("[^a-z A-Z]");
		java.util.regex.Matcher matcher = pattern.matcher(s);
		String number = matcher.replaceAll("");
		return number;
	}


	/*
	 * public static Boolean compareListsWithPreservedOrder( List<String>
	 * firstList, List<String> secondList) method specification :-
	 * 
	 * 1) if (!firstList.get(i).equals(secondList.get(i))) : Checks for equality
	 * with preserved order
	 * 
	 * @param : First list and second list to compare
	 * 
	 * @return : true if lists are equal, false if lists are NOT equal.
	 */

	public static Boolean compareListsWithPreservedOrder(List<String> firstList, List<String> secondList) {

		APPLICATION_LOGS.debug("Comparing two lists whether in the same order ...");

		// Declare variables need
		Boolean result = true;

		// Compare two lists
		for (int i = 0; i < firstList.size(); i++) {

			if (!firstList.get(i).equals(secondList.get(i)))
				result = false;

		}

		// Verify whether they are equal or not

		if (result)
			APPLICATION_LOGS.debug(
					"First list is : " + firstList + " , Second list is : " + secondList + " and they are equal");

		else
			APPLICATION_LOGS.debug(
					"First list is : " + firstList + " , Second list is : " + secondList + " and they are NOT equal");

		return result;

	}

	/*
	 * public static ArrayList<Date> convertStringListToDateList(
	 * ArrayList<String> stringList, String dateFormat) method specification :-
	 * 
	 * 1) date =FunctionLibrary.
	 * convertStringToDate(stringList.get(i),dateFormat) : This converts string
	 * to date 2) dateList.add(date) : Add date to the list
	 * 
	 * @param : List of date in string data type, date format
	 * 
	 * @return : List of date in date data type
	 */
	public static ArrayList<Date> convertStringListToDateList(ArrayList<String> stringList, String dateFormat) {

		// Declare variables
		Date date;
		ArrayList<Date> dateList = new ArrayList<Date>();

		// Convert string list to date list
		for (int i = 0; i < stringList.size(); i++) {

			date = FunctionLibrary.convertStringToDate(stringList.get(i), dateFormat);

			// Add converted date into date list
			dateList.add(date);

		}

		// Return date type list
		return dateList;

	}

	/*
	 * public static ArrayList<String> convertDateListToStringList(
	 * ArrayList<Date> dateList, String format) method specification :-
	 * 
	 * 1) SimpleDateFormat dateFormat = new SimpleDateFormat(format) :
	 * Instantiate date formatter 2) date = dateFormat.format(dateList.get(i)) :
	 * Format a date to string in specified format
	 * 
	 * @param : List of date in date data type, date format
	 * 
	 * @return : List of date in string data type
	 */
	public static ArrayList<String> convertDateListToStringList(ArrayList<Date> dateList, String format) {

		// Declare variables
		String date;
		ArrayList<String> stringList = new ArrayList<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		// Convert date list to string list
		for (int i = 0; i < dateList.size(); i++) {

			date = dateFormat.format(dateList.get(i));

			// Add converted date into string list
			stringList.add(date);

		}

		// Return date type list
		return stringList;

	}

	/**
	 * Brief Description Of getNextOrPreviousDate(String startDate, int days,
	 * String dateFormat) method Add no. of days to the current Date Deduct no.
	 * of days from the curent date
	 */

	public static String getNextOrPreviousDate(String startDate, int days, String dateFormat) throws ParseException {

		// Start date
		try {

			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(startDate));
			c.add(Calendar.DATE, days); // number of days to add
			dateFormat = sdf.format(c.getTime()); // dt is now the new date

		}

		catch (Throwable dateException) {

			APPLICATION_LOGS.debug("Error came while getting next or previous date : " + dateException.getMessage());

		}

		APPLICATION_LOGS.debug("New date After addition or substarction of Days : " + dateFormat);

		return dateFormat;

	}

	/**
	 * Move to web element. This function is useful to move view-port within web
	 * element. Works for page scroll only.
	 * 
	 * <p>
	 * This method <code>returns</code>:
	 * <ul>
	 * <li>Pass if moved to web element successfully</li>
	 * <li>Fail if any exception occurs in between</li>
	 * </ul>
	 * 
	 * @param locator
	 *            Element locator
	 * @param elemName
	 *            Element name
	 * 
	 * @return Pass/Fail
	 */

	public static String moveToWebElement(By locator, String elemName) {

		APPLICATION_LOGS.debug("Moving to : " + elemName + " ...");

		// Initiate Actions class
		Actions action = new Actions(driver);

		try {

			// Wait for check-box to appear on the page
			waitForElementToLoad(locator);

			// Move to element
			action.moveToElement(driver.findElement(locator)).build().perform();

			// Log the result
			APPLICATION_LOGS.debug("Moved to : " + elemName);

			return "Pass : Moved to : " + elemName;

		}

		catch (Throwable moveToElementException) {

			APPLICATION_LOGS.debug("Error came while moving to : " + moveToElementException.getMessage());

			return "Fail : Error came while moving to " + moveToElementException.getMessage();

		}

	}

	/* Switch to iframe */
	public static String switchToiFrame(By locator, String elemname) {
		try {
			// Wait for check-box to appear on the page
			waitForElementToLoad(locator);
			// Switch to iframe
			APPLICATION_LOGS.debug("Switching to " + elemname + ": iframe ....");
			driver.switchTo().frame(driver.findElement(locator));

			APPLICATION_LOGS.debug("Switched to " + elemname + ": iframe ....");
			return "Pass : switch to " + elemname + ": iframe ";
		} catch (Throwable SwitchToFrameException) {
			APPLICATION_LOGS.debug("Error came while switching to " + elemname + ": iframe");
			return ": Error came while switching to " + elemname + ": iframe";
		}
	}

	public static String switchToAlert() throws InterruptedException {

		APPLICATION_LOGS.debug("Switching to Alert");

		try {

			// Switch to alert
			driver.switchTo().alert();

			// Log result
			APPLICATION_LOGS.debug("Switched to Alert");

			return "Pass : Switched to alert";

		}

		catch (Throwable switchToAlertException) {

			// Log error
			APPLICATION_LOGS.debug("Error while switching to alert : " + switchToAlertException.getMessage());

			return "Fail : Error while switching to alert : " + switchToAlertException.getMessage();

		}

	}

	/*
	 * public static String dismissAlert(String elemName) method specification
	 * :-
	 * 
	 * 1) Dismiss an alert 2) alert.dismiss() -> dismisses the alert
	 * 
	 * @param : Name of the web element
	 * 
	 * @return : Result of execution - Pass or fail (with cause)
	 */

	public static String dismissAlert(String elemName) {

		APPLICATION_LOGS.debug("Dismissing alert : " + elemName);

		try {

			// Create a new alert object
			Alert alert = driver.switchTo().alert();

			// Dismiss the alert
			alert.dismiss();

			// Log result
			APPLICATION_LOGS.debug("Dismissed alert : " + elemName);

			return "Pass : Dismissed the alert '" + elemName + "'";

		}

		catch (Throwable dismissAlertException) {

			// Log error
			APPLICATION_LOGS.debug("Error came while dismissing alert : " + dismissAlertException.getMessage());

			return "Fail : Error came while dismissing alert : " + dismissAlertException.getMessage();

		}

	}

	/*
	 * public static String getAlertText() method specification :-
	 * 
	 * 1) driver.getAlertText() : Get the text of the alert
	 * 
	 * @param : none
	 * 
	 * @return : Current webpage title
	 */

	public static String getAlertText() {

		APPLICATION_LOGS.debug("Getting text of the alert ...");
		String alertText = null;

		try {

			// Create a new alert object
			Alert alert = driver.switchTo().alert();

			// Get alert text
			alertText = alert.getText();

		} catch (Throwable getAlertTextError) {

			APPLICATION_LOGS.debug("Error came while fetching alert text : " + getAlertTextError.getMessage());

		}

		APPLICATION_LOGS.debug("Got text of alert : " + alertText);
		return alertText;

	}

	public static Date subtractDate(int dateDiff) {

		APPLICATION_LOGS.debug("Get the date difference  ...");
		Date newDate = new Date();

		try {

			// Subtract days from current date

			// newDate.setDate(newDate.getDate() - dateDiff);

			// Log result
			APPLICATION_LOGS.debug("Date difference : " + newDate);

		}

		catch (Throwable getDateDifferenceException) {

			// Log error
			APPLICATION_LOGS
			.debug("Error while getting the difference of date : " + getDateDifferenceException.getMessage());

		}

		return newDate;

	}

	// Function to MouseHover webelement

	public static String mouseOverWebElement(By locator, String elemName) {
		try {

			Actions builder = new Actions(driver);
			builder.moveToElement(driver.findElement(locator)).build().perform();
			System.out.println(elemName + " Mouse hovered");
			APPLICATION_LOGS.debug(elemName + " Mouse hovered");
			return "Pass";
		} catch (Exception e) {
			return "Fail" + e.getMessage();
		}
	}

	public static int extractNumberFromString(String stringWithNumbers) {

		int integerInString = 0;

		APPLICATION_LOGS.debug("Extracting Number from the String ...");

		try {

			// Convert to dateString str_Extract = "ABC1234jk98";
			for (int i = 0; i < stringWithNumbers.length(); i++)
				if (stringWithNumbers.charAt(i) > 47 && stringWithNumbers.charAt(i) < 58)
					integerInString = stringWithNumbers.charAt(i);

			// Log result
			APPLICATION_LOGS.debug("Integer retreived : " + integerInString);

		}

		catch (Throwable extractNumberFromStringException) {

			// Log error
			APPLICATION_LOGS.debug(
					"Error while converting String to int format : " + extractNumberFromStringException.getMessage());

		}

		return integerInString;

	}


}