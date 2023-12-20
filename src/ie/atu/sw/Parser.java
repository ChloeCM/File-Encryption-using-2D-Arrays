package ie.atu.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class parses all files into a specified directory - its main function is
 * to strip all unwanted characters and convert all text to uppercase.
 */
public class Parser {

	/**
	 * Reads each text file in a specific directory, removes all white spaces and
	 * special characters and converts all the text to uppercase.
	 * 
	 * @param directoryPath - path of directory containing the text files.
	 * @return - an array of STrings - each entry is the content of the parsed file.
	 * @throws IOException - if there's an error accessing the directory/ files.
	 */
	public String[] parseFilesInDirectory(String directoryPath) throws IOException {
		File folder = new File(directoryPath);
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles == null) {
			throw new IOException("Provided path is null");
		}

		int textFileCount = 0;
		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().endsWith(".txt")) {
				textFileCount++;
			}
		}

		String[] parsedFiles = new String[textFileCount];
		int i = 0;

		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().endsWith(".txt")) {
				try {
					parsedFiles[i] = parse(file.getPath());
					i++;
				} catch (Exception e) {
					System.out.println("Error! Cannot read file " + listOfFiles[i].getName());
					e.printStackTrace();
				}
			}
		}
		return parsedFiles;
	}

	/**
	 * Again, it reads the file, strips all white spaces and special characters and
	 * converts the text to uppercase.
	 * 
	 * @param filePath - the path of the files to be parsed.
	 * @return - A String containing the parsed content of the file.
	 * @throws Exception - if there's an error accessing the file.
	 */
	public String parse(String filePath) throws Exception {
		StringBuilder strippedText = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));

		String line = null;
		while ((line = br.readLine()) != null) {
			// trim all the white space, replace all lowercase to Uppercase
			line = line.trim().replaceAll("[^a-zA-Z]", "").toUpperCase();
			strippedText.append(line);
		}
		br.close();
		return strippedText.toString();
	}

}
