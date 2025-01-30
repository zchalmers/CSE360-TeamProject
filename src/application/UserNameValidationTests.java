package application;

import application.UserNameRecognizer;

public class UserNameValidationTests {

	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests
	
	
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\n UserName Testing Automation");

		/************** Start of the test cases **************/
		
		
		
	}
	
	
	public static void performTestCase(String input, Boolean expectedPass) {
		
		String errorMessage = UserNameRecognizer.checkForValidUserName(input);
		
		if (!errorMessage.isEmpty()) {
			
			if (expectedPass) {
				System.out.printf("***Success*** The Username '%s' is valid. This Test is supposed to pass.", input);
			}
			
			else {
				System.out.printf("***Success*** The Username '%s' is not valid. This Test is supposed to Fail", input);
			}
			
		}
		
		
	}
}
