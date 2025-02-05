package application;

public class NameValidator {
	/**
	 * <p>
	 * Title: FSM-translated NameValidator.
	 * </p>
	 * 
	 * <p>
	 * Description: A demonstration of the mechanical translation of Finite State
	 * Machine diagram into an executable Java program using the Name
	 * Recognizer. The code detailed design is based on a while loop with a select
	 * list
	 * </p>
	 * 
	 * <p>
	 * Copyright: Lynn Robert Carter © 2024
	 * </p>
	 * 
	 * @author Lynn Robert Carter
	 * 
	 * @version 1.00 2024-09-13 Initial baseline derived from the Even Recognizer
	 * @version 1.01 2024-09-17 Correction to address UNChar coding error, improper
	 *          error message, and improve internal documentation
	 * @version 1.02 2025-01-19 Adjustments to FSM to only accept alphabetic char in
	 *          first step and to allow - and _ along with . between chars
	 */

	/**********************************************************************************************
	 * 
	 * Result attributes to be used for GUI applications where a detailed error
	 * message and a pointer to the character of the error will enhance the user
	 * experience.
	 * 
	 */

	public static String nameRecognizerErrorMessage = ""; // The error message text
	public static String nameRecognizerInput = ""; // The input being processed
	public static int nameRecognizerIndexofError = -1; // The index of error location
	private static int state = 0; // The current state value
	private static int nextState = 0; // The next state value
	private static boolean finalState = false; // Is this state a final state?
	private static String inputLine = ""; // The input line
	private static char currentChar; // The current character in the line
	private static int currentCharNdx; // The index of the current character
	private static boolean running; // The flag that specifies if the FSM is
									// running
	private static int nameSize = 0; // A numeric value may not exceed 16 characters

	// Private method to display debugging data
	private static void displayDebuggingInfo() {
		// Display the current state of the FSM as part of an execution trace
		if (currentCharNdx >= inputLine.length())
			// display the line with the current state numbers aligned
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state
					+ ((finalState) ? "       F   " : "           ") + "None");
		else
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state
					+ ((finalState) ? "       F   " : "           ") + "  " + currentChar + " "
					+ ((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + nextState
					+ "     " + nameSize);
	}

	// Private method to move to the next character within the limits of the input
	// line
	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram
	 * into a Java method.
	 * 
	 * @param input The input string for the Finite State Machine
	 * @return An output string that is empty if every things is okay or it is a
	 *         String with a helpful description of the error
	 */
	public static String checkForValidName(String input) {
		// Check to ensure that there is input to process
		if (input.length() <= 0) {
			nameRecognizerIndexofError = 0; // Error at first character;
			return "\n*** ERROR *** The name is empty!!";
		}

		// The local variables used to perform the Finite State Machine simulation
		state = 0; // This is the FSM state number
		inputLine = input; // Save the reference to the input line as a global
		currentCharNdx = 0; // The index of the current character
		currentChar = input.charAt(0); // The current character from above indexed position

		// The Finite State Machines continues until the end of the input is reached or
		// at some
		// state the current character does not match any valid transition to a next
		// state

		nameRecognizerInput = input; // Save a copy of the input
		running = true; // Start the loop
		nextState = -1; // There is no next state
		System.out.println("\nCurrent Final Input  Next  Date\nState   State Char  State  Size");

		// This is the place where semantic actions for a transition to the initial
		// state occur

		nameSize = 0; // Initialize the Name size

		// The Finite State Machines continues until the end of the input is reached or
		// at some
		// state the current character does not match any valid transition to a next
		// state
		while (running) {
			// The switch statement takes the execution to the code for the current state,
			// where
			// that code sees whether or not the current character is valid to transition to
			// a
			// next state
			switch (state) {
			case 0:
				// State 0 has 1 valid transition that is addressed by an if statement.

				// The current character is checked against A-Z, ', -. If any are matched
				// the FSM goes to state 1

				// A-Z, ', - -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z') || (currentChar == '\'') || (currentChar == '-')) { // Check for A-Z, ', -
					nextState = 1;

					// Count the character
					nameSize++;

					// This only occurs once, so there is no need to check for the size getting
					// too large.
				}
				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				break;

			case 1:
				// State 1 has one valid transitions,
				// 1: a-z, ', - that transitions back to state 1
				if ((currentChar >= 'a' && currentChar <= 'z') || // Check for a-z
					(currentChar == '\'') || (currentChar == '-')) { // Check for ' or minus
					nextState = 1;

					// Count the character
					nameSize++;
				}		
				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				// If the size is larger than 50, the loop must stop
				if (nameSize > 50)
					running = false;
				break;
			}

			if (running) {
				displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next
				// character in the input and if there is one, it fetches that character and
				// updates the currentChar. If there is no next character the currentChar is
				// set to a blank.
				moveToNextCharacter();

				// Move to the next state
				state = nextState;

				// Is the new state a final state? If so, signal this fact.
				if (state == 1)
					finalState = true;

				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again

		}
		displayDebuggingInfo();

		System.out.println("The loop has ended.");

		// When the FSM halts, we must determine if the situation is an error or not.
		// That depends
		// of the current state of the FSM and whether or not the whole string has been
		// consumed.
		// This switch directs the execution to separate code for each of the FSM states
		// and that
		// makes it possible for this code to display a very specific error message to
		// improve the
		// user experience.
		nameRecognizerIndexofError = currentCharNdx; // Set index of a possible error;
		nameRecognizerErrorMessage = "\n*** ERROR *** ";

		// The following code is a slight variation to support just console output.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			nameRecognizerErrorMessage += "A Name must start with A-Z, an apostrophe or a minus.\n";
			return nameRecognizerErrorMessage;

		case 1:
			// State 1 is a final state. Check to see if the Name length is valid. If so
			// we
			// we must ensure the whole string has been consumed.
			if (nameSize < 1) {
				// Name is too small
				nameRecognizerErrorMessage += "A Name must have at least 1 character.\n";
				return nameRecognizerErrorMessage;
			} else if (nameSize > 50) {
				// Name is too long
				nameRecognizerErrorMessage += "A Name must have no more than 50 characters.\n";
				return nameRecognizerErrorMessage;
			} else if (currentCharNdx < input.length()) {
				// There are characters remaining in the input, so the input is not valid
				nameRecognizerErrorMessage += "After the first character, a Name may only contain the characters a-z, an apostrophe or a minus.\n";
				return nameRecognizerErrorMessage;
			} else {
				// Name is valid
				nameRecognizerIndexofError = -1;
				nameRecognizerErrorMessage = "";
				return nameRecognizerErrorMessage;
			}

		default:
			// This is for the case where we have a state that is outside of the valid
			// range.
			// This should not happen
			nameRecognizerErrorMessage += "State outside of valid range.";
			return nameRecognizerErrorMessage;
		}
	}
}
