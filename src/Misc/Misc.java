package Misc;
import java.util.*;   

/* Created by Nicholas Howland
 * 2/5/2025
 */

public abstract class Misc{
	
	public static int generateRandomInt(int min, int max){		
		Random rand = new Random();
		return (Math.abs(rand.nextInt()) % (max - min + 1)) + min;
	}
	
	public static double generateRandomDouble(){
	    Random rand = new Random();
	    return rand.nextDouble();
	}
 
	public static boolean getUserInputCompareString(String message,Scanner scan,String compared){
	    System.out.println(message);
	    String userInput = scan.nextLine();
	    return userInput.compareTo(compared) == 0;
	}
	
	public static String getUserInputReturnString(String message,Scanner scan){
	    System.out.println(message);
	    return scan.nextLine();
	}
	
	public static int getUserInputInt(String message,Scanner scan, int min, int max){
		int returnInt= 0;
		boolean keepAsking = true;
	    while(keepAsking||returnInt < min|| returnInt > max){
	       System.out.println(message);
	       try{
	          returnInt = scan.nextInt();
	          keepAsking = false;
	       }catch(InputMismatchException e){
	          System.out.println("Please enter a number!");
	       }finally{
	          scan.nextLine();
	       }
	    }
	    return returnInt;
	}
	
		public static String compareStrings(String compare, String [] stringArray) {
		String entered="None";
		for(int i=0; i < stringArray.length; i++) {
			if(compare.equals(stringArray[i])) {
				entered=compare;
			}
		}
		// if none is returned then the supplied option was not valid
		return entered;
	}
	
	public static double getUserInputDouble(String message,Scanner scan, int min, int max){
		double returnDouble= 0.0;
		boolean keepAsking = true;
	    while(keepAsking||returnDouble < min|| returnDouble > max){
	       System.out.println(message);
	       try{
	          returnDouble = scan.nextDouble();
	          keepAsking = false;
	       }catch(InputMismatchException e){
	          System.out.println("Please enter a number!");
	       }finally{
	          scan.nextLine();
	       }
	    }
	    return returnDouble;
	}
	public static int setVars(int min, int max, int var) {
		
		if (var > max) {
			var = max;
		}
		if (var < min) {
			var = min;
		}
		return var;
	}
	
	
}
