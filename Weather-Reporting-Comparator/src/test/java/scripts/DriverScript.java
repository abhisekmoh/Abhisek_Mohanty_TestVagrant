package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Utility.TestUtil;
import api.WeatherReportAPI;
import comparator.CompareTempFromUiAndApi;
import exception.ComparisionException;
import pages.NdtvHomePage;
import pages.WeatherPage;


public class DriverScript {

	public static Properties CONFIG;
	public static Properties LOG;
	public static float temp_variation;
	public static String screenshotPath = System.getProperty("user.dir") + File.separator+"ScreenShots"+File.separator;
	public static Logger APPLICATION_LOGS = Logger.getLogger("Debugger");
	public static WebDriver driver = null;
	public static String testBrowser = "";
	public static String cities ;
	public static String[] CityArray ;
	public static String tempUnit ;
	public static LinkedHashMap<String,Float> cityTempInCelciusMap = new LinkedHashMap<String,Float>();
	public static LinkedHashMap<String,Object> cityTempInCelciusMapFromAPI = new LinkedHashMap<String,Object>();


	@BeforeClass(groups= {"Regression"})
	public static void initialize() throws Exception {
		try {
			CONFIG = new Properties();
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir") + File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"config"+File.separator+"config.properties");
			CONFIG.load(fs);
			System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
			LOG = new Properties();
			fs = new FileInputStream(System.getProperty("user.dir") + File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"log4j.properties");
			LOG.load(fs);
			LOG.setProperty("log4j.appender.dest1.File",
					System.getProperty("user.dir") + File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"config"+File.separator+"application.log");

			LOG.store(new FileOutputStream(System.getProperty("user.dir") + File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"log4j.properties"), null);
			temp_variation = Float.parseFloat(CONFIG.getProperty("temparature_variation"));

			APPLICATION_LOGS.debug("***START OF TEST CASES EXECUTION***");
			
			System.out.println("temparature_variation: "+temp_variation);
			APPLICATION_LOGS.debug("temparature_variation: "+temp_variation);

			testBrowser = CONFIG.getProperty("browser");
			System.out.println("Test Browser to be used: "+testBrowser);
			APPLICATION_LOGS.debug("Test Browser to be used: "+testBrowser);
			cities = CONFIG.getProperty("cities");
			CityArray = cities.split(", ");
			tempUnit = CONFIG.getProperty("temperature_unit");
			System.out.println("Temperature unit to be compared: "+tempUnit);
			APPLICATION_LOGS.debug("Temperature unit to be compared: "+tempUnit);
			


		}
		catch(Exception e) {
			APPLICATION_LOGS.error("Failed while initialization in DriverScript.. "+"/n Error message: "+e.getStackTrace());
			System.out.println("Failed while initialization in DriverScript.. "+"/n Error message: "+e.getStackTrace());
		}
	}

	@Test(priority=1, enabled=true, groups= {"Regression"})
	public void NavigateToNDTV() throws Exception{
		APPLICATION_LOGS.debug("##################################");
		APPLICATION_LOGS.debug("Executing Test Case: Navigating to NDTV website..");
		TestUtil.setBrowser(testBrowser);
		System.out.println("Checking");
		NdtvHomePage.navigate_to_ndtv();

	}

	@Test(priority=2, enabled=true, groups= {"Regression"})
	public void goToWeatherSectionPage() {
		APPLICATION_LOGS.debug("##################################");
		APPLICATION_LOGS.debug("Executing Test Case: Navigating to NDTV Weather Section..");
		NdtvHomePage.clickOnSubMenu();
		NdtvHomePage.goToWeatherSection();
	}

	@Test(priority=3, enabled=true, groups= {"Regression"})
	public void getTemperatureOfCitiesFromUI() throws InterruptedException {
		APPLICATION_LOGS.debug("##################################");
		APPLICATION_LOGS.debug("Executing Test Case: Getting Temperature for Cities from UI..");
		WeatherPage.get_City_temperature_from_UI();
	}

	@Test(priority=4, enabled=true, groups= {"Regression"})
	public void TemperatureOfCitiesFromAPI() {
		APPLICATION_LOGS.debug("##################################");
		APPLICATION_LOGS.debug("Executing Test Case: Getting Temperature for Cities from API..");
		WeatherReportAPI.get_City_temperature_from_API();
	}

	@Test(priority=5, enabled=true, groups= {"Regression"})
	public void compareTemperature() throws ComparisionException {
		APPLICATION_LOGS.debug("##################################");
		APPLICATION_LOGS.debug("Executing Test Case: Comparing the Temperatures for Cities from API and UI..");
		CompareTempFromUiAndApi.verifyTempDifference();
	}

	@AfterClass(enabled=true,groups= {"Regression"})
	public void closeBrowser() {
		APPLICATION_LOGS.debug("##################################");
		driver.quit();
		APPLICATION_LOGS.debug("Closing Browser After completion of Test..");
		APPLICATION_LOGS.debug("***END OF TEST CASES EXECUTION***");
	}


}
