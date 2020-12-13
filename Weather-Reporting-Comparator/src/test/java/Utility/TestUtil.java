package Utility;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.io.Files;

import scripts.DriverScript;

public class TestUtil extends DriverScript{

	public static void setBrowser(String testBrowser) {

		try {
			if (testBrowser.equals("chrome"))
			{
				APPLICATION_LOGS.debug("Lauching Chrome Driver");
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"chromedriver.exe");
				ChromeOptions chromeOptions=new ChromeOptions();
				chromeOptions.addExtensions(new File("E:\\6.1.4-Crx4Chrome.com.crx"));
				DesiredCapabilities desiredCapabilities=DesiredCapabilities.chrome();
				desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				desiredCapabilities.setCapability (CapabilityType.ACCEPT_SSL_CERTS, true);
				driver = new ChromeDriver();

			}

			else if (testBrowser.equals("firefox"))
			{
				APPLICATION_LOGS.debug("Lauching fireFox Driver");
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"geckodriver.exe");
				FirefoxOptions  firefoxOptions =new FirefoxOptions ();
				DesiredCapabilities desiredCapabilities=DesiredCapabilities.firefox();
				desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, firefoxOptions);
				desiredCapabilities.setCapability (CapabilityType.ACCEPT_SSL_CERTS, true);
				driver = new FirefoxDriver();

			}

			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		}
		catch(Exception e) {
			System.out.println("Exception while lauching browser: "+ e.getStackTrace());
			APPLICATION_LOGS.debug("Exception while lauching browser:"+e.getStackTrace());
		}
	}
	
	// Capture screenshot and store
		public static void takeScreenShot(String filePath) {

			File scrFile = null;
			try {
				scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				// Store screenshot to the path provided
				FileUtils.copyFile(scrFile, new File(filePath));

			}

			catch (Exception e) {

				// Log error
				APPLICATION_LOGS.debug("Error while taking screenshot : " + e.getMessage());
				System.err.println("Error while taking screenshot : " + e.getMessage());

			}

		}

}
