package ie.atu.sw;

import java.io.File;
import java.util.Scanner;

// This class allows users the set, manage, and change their selected output.
public class OutputDirectory {
	private String outputDirectory;
	// Single scanner instances for the entire class
	private Scanner scanner = new Scanner(System.in);

	/**
	 * Ruturns the current output directory path.
	 * 
	 * @return the path of the output directory.
	 */
	public String getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * Updates the current output directory path.
	 * 
	 * @param outputDirectory - the new directory path.
	 */
	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/**
	 * Organising all output methods in this one method. It displays a welcome
	 * display, and allows users to handle changes, and output selection.
	 */
	public void outputDirectoryOrganiser() {
		welcomeDisplay();
		if (outputDirectory == null) {
			outputDirectorySelector();
		} else {
			handleOutputDirectoryChange();
			handleDirectoryContentDisplay(outputDirectory);
			return;
		}
	}

	/**
	 * Displays a small header menu for the output directory.
	 */
	private static void welcomeDisplay() {
		System.out.println("_____________________________________");
		System.out.println("                                     ");
		System.out.println("                                     ");
		System.out.println("        The Output directory         ");
		System.out.println("                                     ");
		System.out.println("_____________________________________");
		System.out.println(" ");
	}

	/**
	 * Asks the user to select a valid output directory. if the directory does not
	 * exist, the user is given the option to create a new one.
	 */
	private void outputDirectorySelector() {
		System.out.println("Please select a valid output folder: ");

		while (outputDirectory == null) {
			String userDirectoryPath = scanner.nextLine();
			if (validOutputDirectory(userDirectoryPath)) {
				setOutputDirectory(userDirectoryPath);
				System.out.println("Success! The output directory path is: " + userDirectoryPath);
			} else {
				System.out.println("Oh!!! This directory does not exist yet!!");
				boolean directoryCreatedByUser = usersChoiceToCreateDirectory(userDirectoryPath);
				if (!directoryCreatedByUser) {
					break;
				}
			}
		}
	}
	
	/**
	 * Validates if the given specified path points to an existing folder.
	 * 
	 * @param the path to check if it is valid.
	 * @return true if the directory exists and is a directory: otherwise false.
	 */
	private boolean validOutputDirectory(String userDirectoryPath) {
		File directory = new File(userDirectoryPath);
		return directory.exists() && directory.isDirectory();
	}
	
	/**
	 * Prompts the users to choose if they want to create a new directory or not.
	 * 
	 * @param userDirectoryPath - if directory is created, this is the directory
	 *                          path.
	 * @return - true if user decides to create a new directory.
	 */
	public boolean usersChoiceToCreateDirectory(String userDirectoryPath) {
		System.out.println("Would you like to create a new directory? y/n?");
		String userChoiseYorN;

		while (true) {
			userChoiseYorN = scanner.nextLine();
			if (userChoiseYorN.equalsIgnoreCase("y")) {
				createUserNewDirectory(userDirectoryPath);
				return true;
			} else if (userChoiseYorN.equalsIgnoreCase("n")) {
				break;
			} else {
				System.out.println("Invalid input! Please press 'y' or 'n' to continue");
			}
		}
		return false;
	}

	/**
	 * At a specified path, this method tries to create a new directory. If
	 * directory was created successfully, it informs the user with a message and
	 * the name of the new directory, otherwise an error message is printed.
	 * 
	 * @param userDirectoryChoice - path where the directory should be created.
	 */
	private void createUserNewDirectory(String userDirectoryChoice) {

		if (createDirectory(userDirectoryChoice)) {
			System.out.println("Directory successfully created: " + userDirectoryChoice);
			setOutputDirectory(userDirectoryChoice);
			System.out.println("The current output is now set to: " + userDirectoryChoice);
		} else {
			System.out.println("Failed to create directory!!");
		}

	}

	/**
	 * Create a new directory at the given directory path.
	 * 
	 * @param directoryName - directory where the new directory should be created.
	 * @return - true if the directory exists or was successfully created.
	 */
	public boolean createDirectory(String directoryName) {
		File directory = new File(directoryName);

		if (directory.exists()) {
			return directory.isDirectory();
		} else {
			return directory.mkdirs();
		}
	}

	/**
	 * Informs the users of what the current output directory is. This also give the user the option
	 * to change the output directory if they wish. 
	 */
	private void handleOutputDirectoryChange() {
		System.out.println("The current ouput directory is set to: " + getOutputDirectory());
		changeOutputDirectory();
	}

	/**
	 * Gives the user the option of changing the output directory.
	 */
	private void changeOutputDirectory() {
		System.out.println("Would you like to change the output directory? y/n?");

		while (outputDirectory != null) {
			String userChoiceYorN = scanner.nextLine();

			if (userChoiceYorN.equalsIgnoreCase("y")) {
				setOutputDirectory(null);
				outputDirectorySelector();
				return;
			} else if (userChoiceYorN.equalsIgnoreCase("n")) {
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
