package exception;

public class ComparisionException extends Exception{
	
	float actual, expected;
	   public ComparisionException(float actual, float expected) {
		this.actual=actual;
		this.expected=expected;
	   }
	   
	   public String toString(){
		      return "The temperature difference is: "+actual+ " which is greater than the expected value: "+expected;
		  }

}
