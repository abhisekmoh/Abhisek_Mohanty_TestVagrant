package pages;

import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import scripts.DriverScript;
import scripts.FunctionLibrary;

public class WeatherPage extends DriverScript{


	/****************************
	 * Locators for NDTV WEATHER page.
	 *****************************/
	//public static By locator_CityCheckBoxfirstPart = By.xpath("//input[@type='checkbox']");
	public static String locator_CityCheckBoxfirstPart = "//input[@type='checkbox' and @id='";
	public static String locator_CityCheckBoxlastPart = "']";
	public static String locator_CitytempFirstPart = "//*[@class='cityText' and text()='";
	public static String locator_CitytempLastPart = "']/preceding-sibling::div/span[1]";
	public static By locator_CitySearchBox = By.id("searchBox");
	public static By locator_map = By.xpath("//div[@id='map_canvas']");
	public static String locator_city_leaflet_popUp_firstPart = "//span[contains(text(),'";
	public static String locator_city_leaflet_popUp_lastPart = "')]/ancestor::*[@Class='leaflet-popup-content']";


	/****************************
	 * Name of WebElements for NDTV home page.
	 *****************************/
	public static String name_CityCheckBox = "City CheckBox";
	public static String namer_CitySearchBox = "City SearchBox";
	public static String name_Citytemp = "City Temperature in ";
	public static String name_map = "Map";


	public static void get_City_temperature_from_UI() throws InterruptedException {

		//Wait for map to load
		FunctionLibrary.waitForElementToLoad(locator_map);
		Thread.sleep(10000);
		APPLICATION_LOGS.debug("Getting temperatures of City from UI..");
		if(tempUnit.equalsIgnoreCase("fahrenheit")) {
			locator_CitytempLastPart = "']/preceding-sibling::div/span[2]";
		}

		for(int i=0;i<CityArray.length;i++) {
			String firstLetter = CityArray[i].substring(0, 1);
			System.out.println("firstLetter: "+firstLetter);
			String remainingLetters = CityArray[i].substring(1, CityArray[i].length());
			firstLetter = firstLetter.toUpperCase();
			CityArray[i] = firstLetter + remainingLetters;
			System.out.println("City: "+CityArray[i]);
			FunctionLibrary.clearAndInput(locator_CitySearchBox, namer_CitySearchBox, CityArray[i]);

			String result = FunctionLibrary.isChecked(By.xpath(locator_CityCheckBoxfirstPart+CityArray[i]+locator_CityCheckBoxlastPart), CityArray[i]+" "+name_CityCheckBox);
			//System.out.println("result: "+result);
			if(result.contains("Fail")) {
				System.out.println("inside if");
				FunctionLibrary.clickLink(By.xpath(locator_CityCheckBoxfirstPart+CityArray[i]+locator_CityCheckBoxlastPart), CityArray[i]+" "+name_CityCheckBox);
			}
			//Click on the City to get the temp info
			FunctionLibrary.clickLink(By.xpath(locator_CitytempFirstPart+CityArray[i]+locator_CitytempLastPart), CityArray[i]+" "+name_Citytemp);
			//Wait for City leafLet popUp to be displayed on map
			FunctionLibrary.waitForElementToLoad(By.xpath(locator_city_leaflet_popUp_firstPart+CityArray[i]+locator_city_leaflet_popUp_lastPart));
			//Retrieve the temperature for the City
			String cityTemp = FunctionLibrary.retrieveText(By.xpath(locator_CitytempFirstPart+CityArray[i]+locator_CitytempLastPart), CityArray[i]+" "+name_Citytemp+tempUnit);
			//System.out.println("City temp: "+cityTemp);
			String k=FunctionLibrary.getOnlyDigits(cityTemp);
			cityTempInCelciusMap.put(CityArray[i], Float.parseFloat(k));
		}

		System.out.println(cityTempInCelciusMap);
		APPLICATION_LOGS.debug("Temperature of Cities in "+tempUnit+" from UI are: "+cityTempInCelciusMap);
	}

}
