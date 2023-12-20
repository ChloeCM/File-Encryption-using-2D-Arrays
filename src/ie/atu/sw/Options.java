package ie.atu.sw;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The Options class provides the user for setting/ viewing their options
 * relating to input and output directories and the encrypted key.
 *
 */
public class Options {
	private Scanner scanner = new Scanner(System.in);
	private InputDirectory inputDirectory;
	private OutputDirectory outputDirectory;
	private Key key;

	/**
	 * Constructor - create an instance of the options with the specified
	 * directories and key below.
	 * 
	 * @param inputDirectory  - an instance of the input directory where the
	 *                        directory is to be read from.
	 * @param outputDirectory - an instance of the output directory where the
	 *                        directory is to write to.
	 * @param key             - an instance of the key representing the encrypted
	 *                        key.
	 */
	public Options(InputDirectory inputDirectory, OutputDirectory outputDirectory, Key key) {
		this.inputDirectory = inputDirectory;
		this.outputDirectory = outputDirectory;
		this.key = key;
	}

	/**
	 * Organises the option input in this one methiod.
	 */
	public void optionMenu() {
		welcomeOptionsDisplay();
		userOptionsChoice();
	}

	/**
	 * Displays/ Prints an option menu with the following options for a user to
	 * choose.
	 */
	private static void welcomeOptionsDisplay() {
		System.out.println("_____________________________________");
		System.out.println("                                     ");
		System.out.println("                                     ");
		System.out.println("            Option Menu              ");
		System.out.println("                                     ");
		System.out.println("_____________________________________");
		System.out.println(" ");
		System.out.println("Pick from the following options below: ");
		System.out.println("");
		System.out.println(" 1. View Settings");
		System.out.println(" 2. Clear Your Settings");
		System.out.println(" 3. Return to Main Menu");
	}

	/**
	 * Handles user input to select an option (1-3) and then runs the corresponding
	 * methods under the option they picked.
	 */
	private void userOptionsChoice() {

		try {
			while (true) {
				int userChoice = scanner.nextInt();
				scanner.nextLine();

				switch (userChoice) {
				case 1 -> {
					printSettings();
					returnToOptionsMenu();
					break;
				}
				case 2 -> {
					clearSettings();
					returnToOptionsMenu();
					break;
				}
				case 3 -> {
					return;
				}
				default -> System.out.println("Oops!! Invalid choice! Please select 1-3.");
				}
			}
		} catch (InputMismatchException e) {
			System.out.println("Oops!! Invalid input! Please select a valid number: 1-3");
			scanner.nextLine();
			userOptionsChoice();
		} catch (Exception e) {
			// Catch other unexpected error and print message to the user.
			System.out.println("An unexpected error has occurred! Please try again!");
			scanner.nextLine();
			userOptionsChoice();
		}
	}

	/**
	 * Prints the current settings for the input directory, output directory, and
	 * the key.
	 */
	private void printSettings() {

		if (inputDirectory.getInputDirectory() == null) {
			System.out.println("You have not selected an input directory");
		} else {
			System.out.println("Input directory is: " + inputDirectory.getInputDirectory());
		}

		if (outputDirectory.getOutputDirectory() == null) {
			System.out.println("You have not selected an output directory");
		} else {
			System.out.println("Output directory is: " + outputDirectory.getOutputDirectory());
		}

		if (key.getKey() == null) {
			System.out.println("The key is not set!");
		} else {
			System.out.println("The key is set to: " + Arrays.toString(key.getKey()));
		}
	}

	/**
	 * This methods clears all settings for the user.
	 */
	private void clearSettings() {
		inputDirectory.setInputDirectory(null);
		outputDirectory.setOutputDirectory(null);
		key.setKey(null);

		System.out.println("All your settings have now been cleared!");
	}

	/**
	 * Asks the user to return back to the options menu.
	 */
	private void returnToOptionsMenu() {
		String userChoice;

		do {
			System.out.println("");
			System.out.println("Press 'r' to return to Options menu");
			userChoice = scanner.nextLine();

			if (!userChoice.equalsIgnoreCase("r")) {
				System.out.println("");
				System.out.println("Incorrect input!");
			}
		} while (!userChoice.equalsIgnoreCase("r"));

		welcomeOptionsDisplay();
	}

}
