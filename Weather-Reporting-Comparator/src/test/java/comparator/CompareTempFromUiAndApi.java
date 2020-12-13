package comparator;

import scripts.DriverScript;
import exception.ComparisionException;

public class CompareTempFromUiAndApi extends DriverScript{

	public static float tempDifference;

	public static void verifyTempDifference() throws ComparisionException{

		for(int i=0;i<CityArray.length;i++) {
			//((Integer) entry.getIdentifier()).intValue()
		
			tempDifference = Math.abs(cityTempInCelciusMap.get(CityArray[i]) - ((Float)cityTempInCelciusMapFromAPI.get(CityArray[i])).floatValue());
			if(temp_variation>=tempDifference) {
				System.out.println("Its OK");
				APPLICATION_LOGS.debug("The temperature difference is within the expected value: "+ temp_variation);
			}else {
				try {
					APPLICATION_LOGS.debug("The temperature difference is: "+tempDifference+ " which is greater than the expected value: "+temp_variation);
					throw new ComparisionException(tempDifference,temp_variation);  
				}catch(Exception e) {
					System.out.println(e) ;
				}
			}
		}	
	}
}
