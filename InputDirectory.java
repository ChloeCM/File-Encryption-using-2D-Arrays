package ie.atu.sw;

import java.io.File;
import java.util.Scanner;

public class InputDirectory {
	private String inputDirectory;
	// Single scanner instances for the entire class
	private Scanner scanner = new Scanner(System.in);

	/**
	 * Returns/ Gets the current input directory path.
	 * 
	 * @return - the path of the input directory.
	 */
	public String getInputDirectory() {
		return inputDirectory;
	}

	/**
	 * Sets the input directory path.
	 * 
	 * @param inputDirectory - new path to set.
	 */
	public void setInputDirectory(String inputDirectory) {
		this.inputDirectory = inputDirectory;
	}

	/**
	 * Organising all input directory methods in this one method such as: handling
	 * changes, selection, content display and returning to main menu.
	 */
	public void inputDirectoryOrganiser() {

		welcomeDisplay();

		if (inputDirectory == null) {
			inputDirectorySelector();
		} else {
			changeInputDirectory();
			handleDirectoryContentDisplay(inputDirectory);
			return;
		}
	}
	
	/**
	 * Displays a small header menu for the input directory.
	 */
	private static void welcomeDisplay() {
		System.out.println("_____________________________________");
		System.out.println("                                     ");
		System.out.println("                                     ");
		System.out.println("         The Input directory         ");
		System.out.println("                                     ");
		System.out.println("_____________________________________");
		System.out.println(" ");
	}

	/**
	 * Prompts the user to insert a valid input directory. If the directory path
	 * provided does not exist, the user is given the option to create a new
	 * directory.
	 */
	private void inputDirectorySelector() {
		System.out.println("Please select an input directory: ");

		String userDirectoryPath;

		while (inputDirectory == null) {
			userDirectoryPath = scanner.nextLine();
			System.out.println("");
			if (validDirectoryPath(userDirectoryPath)) {
				setInputDirectory(userDirectoryPath);
				System.out.println("Success! The input directory is: " + userDirectoryPath);
				return;
			} else {
				System.out.println("Oh!!! This directory does not exist yet!!");
				boolean directoryCreatedByUser = promptUserToCreateNewDirectory(userDirectoryPath);
				if (!directoryCreatedByUser) {
					break;
				}
			}
		}
	}

	/**
	 * Checks if the provided directory path exists and is a directory.
	 * 
	 * @param userDirectoryPath - the path to check if it is valid.
	 * @return true if the directory exists and is a directory: otherwise false.
	 */
	private boolean validDirectoryPath(String userDirectoryPath) {
		File directory = new File(userDirectoryPath);
		return directory.exists() && directory.isDirectory();
	}

	/**
	 * Prompts the user with a yes or no choice to create a new directory at a given
	 * path.
	 * 
	 * @param userDirectoryPath - path for the new directory.
	 * @return - true - if user wants to create a folder, false if they do not.
	 */
	private boolean promptUserToCreateNewDirectory(String userDirectoryPath) {
		System.out.println("Would you like to create a new directory? y/n?");

		while (true) {
			String userChoiceYorN = scanner.nextLine();

			if (userChoiceYorN.equalsIgnoreCase("y")) {
				attemptToCreateDirectory(userDirectoryPath);
				return true;
			} else if (userChoiceYorN.equalsIgnoreCase("n")) {
				break;
			} else {
				System.out.println("Oops!!! Invalid Input!!!");
				System.out.println("Press 'y' or 'n' to continue.");
			}
		}
		return false;
	}

	/**
	 * At a specified path, this method tries to create a new directory. If
	 * directory was created successfully, it informs the user with a message and
	 * the name of the new directory, otherwise an error message is printed.
	 * 
	 * @param userDirectoryPath - path where the directory should be created.
	 */
	private void attemptToCreateDirectory(String userDirectoryPath) {
		if (createDirectory(userDirectoryPath)) {
			System.out.println("Directory successfully created: " + userDirectoryPath);
			setInputDirectory(userDirectoryPath);
			System.out.println("The current directory is now set to: " + userDirectoryPath);
		} else {
			System.out.println("Failed to created directory " + userDirectoryPath);
			System.out.println("Please try again!!");
		}

	}

	/**
	 * Confirms if a directory already exists or creates a new one at a specified
	 * path in the code.
	 * 
	 * @param directoryName - the path of the directory to create.
	 * @return true if directory already exists or was sucessfully created.
	 */
	private boolean createDirectory(String directoryName) {
		File directory = new File(directoryName);

		if (directory.exists()) {
			return directory.isDirectory();
		} else {
			return directory.mkdirs();
		}
	}

	/**
	 * Provide the user with the option of changing the current input directory. If
	 * the user chooses 'y', the input directory is reset (set to null). Otherwise,
	 * if user selects 'n', the current directory will remain unchanged.
	 */
	private void changeInputDirectory() {
		System.out.println("Input directory is currently set to: " + getInputDirectory());
		System.out.println("Would you like to change the input directory? y/n ");

		while (inputDirectory != null) {

			String userYorN = scanner.nextLine();

			if (userYorN.equalsIgnoreCase("y")) {
				setInputDirectory(null);
				inputDirectorySelector();
				break;
			} else if (userYorN.equalsIgnoreCase("n")) {
				break;
			} else {
				System.out.println("Invalid Input! Press 'y' to change input directory OR 'n' to continue");
			}
		}
	}

	/**
	 * Ask the user do they want to view the content of a given directory. If yes,
	 * the list of all the files are displayed.
	 * 
	 * @param directory - whose contents might be displayed.
	 */
	private void handleDirectoryContentDisplay(String directory) {
		System.out.println("Would you like to view the files in this directory? y/n: ");

		try {
			while (true) {
				String userChoice = scanner.nextLine();
				if (userChoice.equalsIgnoreCase("y")) {
					listDirectoryContents(directory);
					break;
				} else if (userChoice.equalsIgnoreCase("n")) {
					break;
				} else {
					System.out.print("Invalid input! Please press 'y' or 'n' to continue");
				}
			}
		} catch (Exception exception) {
			System.out.println("An error occurred while trying to list directory contents!!");
			exception.printStackTrace();
		}
	}

	/**
	 * Lists all the content within a input directory. If the content of the
	 * directory is empty or the contents cannot be displayed, the user is informed.
	 * 
	 * @param directoryPath
	 * @throws Exception
	 */
	private void listDirectoryContents(String directoryPath) throws Exception {

		File directory = new File(directoryPath);
		File[] filesListed = directory.listFiles();

		if (filesListed == null) {
			throw new Exception("Unable to get contents of directory/ folder!! Try again!!");
		} else if (filesListed.length == 0) {
			System.out.println("This directory is currently empty!");
		} else {
			System.out.println("Here are the contents of this directory/ folder: ");
			System.out.println("");
			for (File file : filesListed) {
				System.out.println(file.getName());
			}
		}
	}

}
