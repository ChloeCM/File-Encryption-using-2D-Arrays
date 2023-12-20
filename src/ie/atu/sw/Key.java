package ie.atu.sw;

import java.util.Arrays;
import java.util.Scanner;

public class Key {
	Scanner scanner = new Scanner(System.in);
	private char[] key;

	/**
	 * Gets the current encrypted key.
	 * 
	 * @return - a char array representing the encrypted key.
	 */
	public char[] getKey() {
		return key;
	}

	/**
	 * Sets the key by converting the provided String key into a char array.
	 * 
	 * @param key - The String representation of the key.
	 */
	public void setKey(String key) {
		if (key == null) {
			this.key = null;
		} else {
			this.key = key.toCharArray();
		}
	}

	/**
	 * Manages the key input process for the user. This includes a choice to change
	 * the existing key and setting it and also collecting and validating the user's
	 * key input.
	 */
	public void handleKeyOperations() {
		if (key != null) {
			changingKey();
		} else {
			welcomeDisplay();
			displayKeyOptions();
			userInputForKey();
		}
	}

	/**
	 * Gives the user a choice of changing the current encryption key.
	 */
	private void changingKey() {
		boolean running = true;
		System.out.println("The current key is: " + Arrays.toString(key));
		System.out.println("Would you like to change the key? y/n?");

		while (running) {
			String userChoice = scanner.nextLine().toUpperCase();
			switch (userChoice) {
			case "Y" -> {
				setKey(null);
				displayKeyOptions();
				userInputForKey();
				running = false;
			}
			case "N" -> {
				return;
			}
			default -> System.out.println("Invalid Input! Please press 'y' or 'n'.");
			}
		}
	}

	/**
	 * Receives and validates user input for the key. If the key is valid, the key
	 * will be set.
	 * 
	 * @return - returns null after key has been successfully set.
	 */
	private void userInputForKey() {

		String userInput;

		while (key == null) {
			// Get users input, trim the spaces and convert to Uppercase.
			userInput = scanner.nextLine().trim().toUpperCase();

			if (verifyKey(userInput)) {
				System.out.println("Success! Key has been set to: " + userInput);
				setKey(userInput);
			}
		}
	}
	
	/**
	 * Validates the given key based on specified requirements: The key must be: 1.
	 * at least 4 characters minimum in length. 2. Not exceed 15 characters. 3.
	 * Contain NO repeating characters. 4. Only consist of letters and digits.
	 * 
	 * @param key - The key String to validate
	 * @return true - if the key (that the user has inputted) follows all the
	 *         requirements: otherwise it returns false.
	 */
	private boolean verifyKey(String key) {

		if (key.length() < 4) {
			System.out.println("You must insert a passcode that is a minimum of 4 characters");
			return false;
		} else if (key.length() > 15) {
			System.out.println("Your key is has more than 15 characters. Please try again!");
			return false;
		} else if (hasReoccuringChars(key)) {
			System.out.println("Your Key has reoccuring characters. Please try again");
			return false;
		} else if (!containsOnlyLettersAndDigits(key)) {
			System.out.println("Your key can only contain letters and digits! Please try again!");
			return false;
		}
		return true;
	}

	/**
	 * Displays/ Prints a menu to the user to indicate that they are now in the 'Set
	 * Key' option.
	 */
	private static void welcomeDisplay() {
		System.out.println("_____________________________________");
		System.out.println("                                     ");
		System.out.println("                                     ");
		System.out.println("          Setting Your Key           ");
		System.out.println("                                     ");
		System.out.println("_____________________________________");
		System.out.println(" ");
	}

	/**
	 * Prints instructions to the user about how to set the key and its
	 * requirements.
	 */
	private void displayKeyOptions() {

		System.out.println("You are about to set the key.");
		System.out.println("Choose any passcode you wish with the following requirements: ");
		System.out.println("");
		System.out.println("- Only use letters and digits (no special symbols/ characters)");
		System.out.println("- Choose a key between 4 - 15 characters.");
		System.out.println("- Have NO reoccuring characters");
		System.out.println(" ");
	}

	/**
	 * Checks if the given key string contains only letters and digits. This is
	 * important step as an encryption key with special characters is not suitable
	 * for our cipher.
	 * 
	 * @param key - The key String to be checked.
	 * @return true - if the key only contains letters and digits: otherwise false.
	 */
	private boolean containsOnlyLettersAndDigits(String key) {
		for (char character : key.toCharArray()) {
			if (!Character.isLetterOrDigit(character)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the given string/ users input has reoccuring characters.
	 * 
	 * @param userInput - The String to be checked for reoccuring characters.
	 * @return - true if the string has a reoccuring characters: otherwise false if
	 *         characters are unique.
	 */
	private boolean hasReoccuringChars(String userInput) {

		// Iterate through each character in the string.
		for (int i = 0; i < userInput.length(); i++) {
			// choosing a character at the current index.
			char c = userInput.charAt(i);
			// checking all the other characters to make sure their are no duplicates
			for (int j = i + 1; j < userInput.length(); j++) {
				// if the current char is the same as another char in the key,
				// the we have a reoccurance.
				if (c == userInput.charAt(j))
					// return true as soon as a reoccuring character is found.
					return true;
			}
		}
		// If we complete both loops without finding any recurring characters,
		// then the string has all unique characters.
		return false;
	}

}
