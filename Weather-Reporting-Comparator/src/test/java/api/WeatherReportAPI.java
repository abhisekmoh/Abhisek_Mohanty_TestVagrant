package api;

import java.util.HashMap;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import scripts.DriverScript;

public class WeatherReportAPI extends DriverScript{

	public static RequestSpecification httpRequest ;
	public static Response response;
	public static String apiURL = CONFIG.getProperty("ndtv_api");
	public static String apiKey = CONFIG.getProperty("ndtv_api_key");
	
	//http://api.openweathermap.org/data/2.5/weather?q=bhubaneswar&appid=7fe67bf08c80ded756e598d6f8fedaea
	
	public static void get_City_temperature_from_API() {
		APPLICATION_LOGS.debug("Getting temperatures of City from API..");
		String apiUnitforTemp ="";
		HashMap<String, String> map = new HashMap<String, String>();
		if(tempUnit.equalsIgnoreCase("celcius")) {
			apiUnitforTemp = "metric";
		}else if(tempUnit.equalsIgnoreCase("fahrenheit")) {
			apiUnitforTemp = "imperial";
		}
		
		for(int i=0;i<CityArray.length;i++) {
			httpRequest = RestAssured.given();
			map.put("q",CityArray[i]);
			map.put("units", apiUnitforTemp);
			map.put("appid", apiKey);
			System.out.println(map);
			response = httpRequest
					.queryParams(map)
					.get(apiURL);
			 String responseBody = response.getBody().asString();
			 System.out.println("Response Body is =>  " + responseBody);
			 int statusCode = response.getStatusCode();
			 System.out.println("Status Code is =>  " + statusCode);
			 map.clear();
			 Object CityTempInCelciusFromAPI = response.jsonPath().get("main.temp");
			 String s=String.valueOf(CityTempInCelciusFromAPI); 
			 float citytempInFloat = Float.parseFloat(s);
			 cityTempInCelciusMapFromAPI.put(CityArray[i], citytempInFloat);
			 
		}
		System.out.println(cityTempInCelciusMapFromAPI);
		APPLICATION_LOGS.debug("Temperature of Cities in "+tempUnit+" from API are: "+cityTempInCelciusMapFromAPI);
		
	}
}
