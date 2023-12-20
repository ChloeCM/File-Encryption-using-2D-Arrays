package ie.atu.sw;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This is the main menu of the ADFGVX file encryption application for the
 * assignment. It provides the user with all the options they need - to handle, manage,
 * change and set the input directory, output directory and the key, encrypt and
 * decrypt files, view additional options and exit the program.
 *
 */
public class Menu {
	private InputDirectory inputDirectory;
	private OutputDirectory outputDirectory;
	private Key key;
	private Encryption encryption;
	private Decryption decryption;
	private Options options;
	private Scanner scanner;

	/**
	 * Constructor - create a new object/ instance of Menu class.
	 */
	public Menu() {
		this.scanner = new Scanner(System.in);
		this.inputDirectory = new InputDirectory();
		this.outputDirectory = new OutputDirectory();
		this.key = new Key();
		this.encryption = new Encryption(inputDirectory, outputDirectory, key);
		this.decryption = new Decryption(inputDirectory, outputDirectory, key);
		this.options = new Options(inputDirectory, outputDirectory, key);
	}

	/**
	 * This method starts the user interaction with the main menu.
	 */
	public void startMenu() {
		showMainMenu();
		userMenuChoice();
	}

	/*
	 * Displays/ Prints the main menu options to the user. This menu provides many functions
	 * from selecting directories, encrypting, decryption files and selecting a key.
	 */
	private void showMainMenu() {
		System.out.println(ConsoleColour.YELLOW_BOLD);
		System.out.println("************************************************************");
		System.out.println("*                                                          *");
		System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
		System.out.println("*                                                          *");
		System.out.println("*                   ADFGVX File Encryption                 *");
		System.out.println("*                                                          *");
		System.out.println("*             Chloé Mills - StudentID - G00425733          *");
		System.out.println("*                                                          *");
		System.out.println("************************************************************");
		System.out.println(" ");
		System.out.println("(1) Input File Directory");
		System.out.println("(2) Output File Directory");
		System.out.println("(3) Set Key");
		System.out.println("(4) Encrypt");
		System.out.println("(5) Decrypt");
		System.out.println("(6) Options");
		System.out.println("(7) Quit the Program");
		System.out.println(" ");
		System.out.print("Select one of the following Options: 1 - 7 ");
		System.out.println();
	}

	/**
	 * This method handles the users input entry from the main menu. Depending on the users choice, 
	 * they can navigate to different functions within the application.
	 */
	private void userMenuChoice() { 
		boolean running = true;

		while (running) {
			try {
				int choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) { 
				case 1 -> {
					inputDirectory.inputDirectoryOrganiser();
					returnToMainMenu();
				}
				case 2 -> {
					outputDirectory.outputDirectoryOrganiser();
					returnToMainMenu();
				}
				case 3 -> {
					key.handleKeyOperations();
					returnToMainMenu();
				}
				case 4 -> {
					encryption.handleEncryption();
					returnToMainMenu();
				}
				case 5 -> {
					decryption.handleDecryption();
					returnToMainMenu();
				}
				case 6 -> {
					options.optionMenu();
					startMenu();
				}
				case 7 -> {
					closingMenuOption();
					running = false;
				}
				default -> {
					System.out.println("Invalid Input! Please select an option listed 1-7.");
				}
				}

			} catch (InputMismatchException exception) {
				System.out.println("Invalid Input! Please enter an integer value from 1-7.");
				scanner.nextLine();
			}
		}
	}

	/**
	 * Provides an interactive option for the user to return to the main menu. It enhances the user
	 * experience by ensuring the smooth running of the application.
	 */
	private void returnToMainMenu() {
		String userChoice;

		do {
			System.out.println("");
			System.out.println("To return to main menu, Please press 'r'");
			userChoice = scanner.nextLine();

			if (!userChoice.equalsIgnoreCase("r")) {
				System.out.println("");
				System.out.println("Incorrect input!");
			}
		} while (!userChoice.equalsIgnoreCase("r"));

		showMainMenu();
	}
	
	/**
	 * Give the user the option of exiting the program completely. If yes, a closing
	 * message/ display is printed and program exits.
	 */
	private void closingMenuOption() {
		System.out.println("");
		System.out.println("Are you sure you want to exit this program? y/n?");

		while (true) {
			String userChoice = scanner.nextLine();
			if (userChoice.equalsIgnoreCase("y")) {
				closingMenuDisplay();
				System.exit(0);
			} else if (userChoice.equalsIgnoreCase("n")) {
				startMenu();
			} else {
				System.out.println("Oops! Invalid Input! Please press 'y' or 'n' to continue");
			}
		}
	}

	/**
	 * An exit menu is prompted when the user has decided to quit the program. The
	 * user has no more input into the program after this point.
	 */
	private void closingMenuDisplay() {
		System.out.println("_____________________________________");
		System.out.println("                                     ");
		System.out.println("                                     ");
		System.out.println("          Exiting Program            ");
		System.out.println("                                     ");
		System.out.println("              Goodbye!               ");
		System.out.println("                                     ");
		System.out.println("_____________________________________");
		System.out.println(" ");
	}

}
