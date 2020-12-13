package pages;

import org.openqa.selenium.By;
import org.testng.Assert;
import scripts.DriverScript;
import scripts.FunctionLibrary;

public class NdtvHomePage extends DriverScript {

	/****************************
	 * Locators for NDTV home page.
	 *****************************/
	public static By locator_NotNow = By.xpath("//a[@class='notnow']");
	public static By locator_SubMenu = By.xpath("//a[@id='h_sub_menu']");
	public static By locator_weatherPage = By.xpath("//a[contains(text(),'WEATHER')]");


	/****************************
	 * Name of WebElements for NDTV home page.
	 *****************************/
	public static String name_NotNow = "Not Now";
	public static String name_SubMenu = "Sub Menu";
	public static String name_weatherPage = "Weather Section";


	public static void navigate_to_ndtv() {
		APPLICATION_LOGS.debug("Navigating to NDTV Website...");
		System.out.println("Navigating to NDTV Website...");
		try {
			String web_url =CONFIG.getProperty("ndtv_url");
			driver.get(web_url);
			String expected_title = "NDTV Weather - Weather in your Indian City";
			FunctionLibrary.assertTitle(expected_title);
			int size = driver.findElements(locator_NotNow).size();
			System.out.println("Size: "+size);
			if(size>0) {
				FunctionLibrary.clickAndWait(locator_NotNow, name_NotNow);
			}
		}catch(Exception e) {
			APPLICATION_LOGS.error("Failed while navigating to ndtv website.."+"Error message: "+e.getStackTrace());
			System.out.println("Failed while navigating to ndtv website.."+"Error message: "+e.getStackTrace());
		}
	}
	
	public static void clickOnSubMenu() {
		FunctionLibrary.clickAndWait(locator_SubMenu, name_SubMenu);
	}
	
	public static void goToWeatherSection() {
		FunctionLibrary.clickAndWait(locator_weatherPage, name_weatherPage);
	}



}
