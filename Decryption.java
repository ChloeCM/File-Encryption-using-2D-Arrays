package ie.atu.sw;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

/*
 * Class reponsible for decrypting. Works together with specified 
 * directories and a provided key. 
 */
public class Decryption {
	private InputDirectory inputDirectory;
	private OutputDirectory outputDirectory;
	private Key key;
	private Parser parser;

	// Polybius square that is used within this assignment.
	// This is the body of the square.
	private static final char[][] POLYBIUS_SQUARE = { 
			{ 'P', 'H', '0', 'Q', 'G', '6' },
			{ '4', 'M', 'E', 'A', '1', 'Y' }, 
			{ 'L', '2', 'N', 'O', 'F', 'D' }, 
			{ 'X', 'K', 'R', '3', 'C', 'V' },
			{ 'S', '5', 'Z', 'W', '7', 'B' }, 
			{ 'J', '9', 'U', 'T', 'I', '8' } };

	// ADFGVX array used for the first row and first column of the Polybius square.
	private static final char[] ADFGVX_ARRAY = { 'A', 'D', 'F', 'G', 'V', 'X' };

	/**
	 * Constructor - a new Decryption instance with specified directories and key.
	 * Initialises a parser object to help with the decrypting process.
	 * 
	 * @param inputDirectory  - the directory where encrypted files are read from.
	 * @param outputDirectory - directory where decrypted files will be written to.
	 * @param key             - same key used to encrypted and decrypted files.
	 */
	public Decryption(InputDirectory inputDirectory, OutputDirectory outputDirectory, Key key) {
		this.inputDirectory = inputDirectory;
		this.outputDirectory = outputDirectory;
		this.key = key;
		this.parser = new Parser();
	}

	/**
	 * Decrypts the provided encrypted text using a specified key. The decryption
	 * process involves several steps:
	 * 
	 * 1. Sorting the key alphanumerically. 
	 * 2. Determining the column order based on the sorted key. 
	 * 3. Setting up the matrix size based on the encrypted text and the key. 
	 * 4. Filling the matrix with the sorted key. 
	 * 5. Filling the matrix columns with the encrypted text.
	 * 6. Reordering the matrix columns based on the original key. 
	 * 7. Transposing the rows of the reordered matrix. 
	 * 8. Decrypting the transposed rows using the ADFGVX cipher.
	 * 
	 * @param encryptedText - the text to be decrypted.
	 * @param key           - the key used in the encryption process.
	 * @return - the decrypted text.
	 */
	public void handleDecryption() {
		if(!directoriesAndKeyAreSet()) {
			return;
		}
		try {
			System.out.println("");
			System.out.println("Please be patient! ");
			System.out.println("Decrypting file(s)...");
			System.out.println("");
			
			String[] encryptedTexts = parser.parseFilesInDirectory(inputDirectory.getInputDirectory());
			int fileNumber = 0;
			for (String encryptedText : encryptedTexts) {
				char[] sortedKey = orderKeyAlphanumerically(key.getKey());
				int[] columnOrder = columnOrder(sortedKey, key.getKey());
				char[][] emptyMatrix = setMatrixSize(encryptedText, key.getKey());
				char[][] matrixWithKey = fillMatrixWithKey(sortedKey, emptyMatrix);
				char[][] filledMatrix = fillMatrixColumnsWithText(encryptedText, matrixWithKey);
				char[][] reorderedMatrix = reorderMatrix(filledMatrix, columnOrder);
				String transposedRows = transposeRows(reorderedMatrix);
				String decryptedText = decryptText(transposedRows);

				File file = new File(outputDirectory.getOutputDirectory(), "decrypted" + fileNumber + ".txt");
				try (FileWriter fileWriter = new FileWriter(file);
						BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
					bufferedWriter.write(decryptedText);
				}
				fileNumber++;
			}
			System.out.println("Your Decryption is Complete!");
		} catch (Exception e) {
			System.out.println("An error occurred while decrypting: " + e.getMessage());
		}
	}
	
	/**
	 * This methods checks if the input directory, output directory and/ or the key
	 * are set before decrypting. If any/all of them are NOT set, a message is
	 * printed to the user to tell them what needs to be done next.
	 * 
	 * @return - true if all three (input, output and key) are set.
	 */
	private boolean directoriesAndKeyAreSet() {
		
		if(inputDirectory.getInputDirectory() == null) {
			System.out.println("Please select an input directory before continuing!");
		}
		if(outputDirectory.getOutputDirectory() == null) {
			System.out.println("Please select an output directory before continuing!");
		}
		if(key.getKey() == null) {
			System.out.println("Please select a key before continuing!");
		}
		return inputDirectory.getInputDirectory() != null && 
				outputDirectory.getOutputDirectory() != null &&
				key.getKey() != null;
	}

	
	/**
	 * This method sorts the key in an alphanumeric order. Sorting the key is an
	 * essential step in the ADFGVX decryption process. A sorted key helps determine
	 * the column order in the matrix, which is used for transposition.
	 * 
	 * @param key - key used in the encryption process, this key will be sorted.
	 * @return - a char array that has the sorted version of the key.
	 */
	private char[] orderKeyAlphanumerically(char[] key) {
		char[] copiedKey = Arrays.copyOf(key, key.length);

		Arrays.sort(copiedKey);
		char[] sortedKey = copiedKey;

		return sortedKey;
	}

	/**
	 * Determines the sequence in which column should be organised relative to the
	 * original key. This sequence is based on the positions of characters in the
	 * sorted key.
	 * 
	 * @param sortedKey - a sorted version of the original key.
	 * @param key       - the original key.
	 * @return - an array representing the column order.
	 */
	private int[] columnOrder(char[] sortedKey, char[] key) {
		int[] columnOrder = new int[key.length];
		boolean[] usedCharacter = new boolean[sortedKey.length];

		for (int i = 0; i < key.length; i++) {
			columnOrder[i] = indexOfOriginalKey(sortedKey, usedCharacter, key[i]);
		}
		return columnOrder;
	}

	/**
	 * Finds the original index of each character in the original key. This method
	 * searches for a specific character in the sorted key and returns its index. It
	 * also ensures that characters that have already been located are NOT used
	 * again.
	 * 
	 * @param sortedkey     - the sorted version of the original encryption key.
	 * @param usedCharacter - boolean array tracking which characters from sorted
	 *                      has been used.
	 * @param character     - the character whose index in the sorted key is to be
	 *                      determined.
	 * @return - the index of the specified character in the sorted key.
	 */
	private int indexOfOriginalKey(char[] sortedkey, boolean[] usedCharacter, char character) {
		
		for (int i = 0; i < sortedkey.length; i++) {
			if (!usedCharacter[i] && sortedkey[i] == character) {
				usedCharacter[i] = true;
				return i;
			}
		}
		return -1;
	}

	/**
	 * Calculates the size of the Matrix (where the decryption will take place). This
	 * is done based on the length of the encrypted text and the length of the key.
	 * The matrix size needs to accommodate the key and the entire encrypted text.
	 * 
	 * @param encryptedText - the encrypted text determines the number of rows.
	 * @param key           - determines the number of columns.
	 * @return - an empty matrix with dimensions based on the provided encrypted
	 *         text and key.
	 */
	private char[][] setMatrixSize(String encryptedText, char[] key) {
		int columns = key.length;

		// Calculate the number of characters that do not fit perfectly into the matrix.
		int clip = encryptedText.length() % key.length;

		// Remove those characters to get a clipped version of the encrypted text.
		String clippedEncryptedText = encryptedText.substring(0, encryptedText.length() - clip);

		int rows = (int) Math.ceil((double) clippedEncryptedText.length() / columns);

		char[][] matrix = new char[rows + 1][columns];

		return matrix;
	}

	/**
	 * Fills in the first row of the matrix with the characters from the key.
	 * 
	 * @param key         - using characters in the key to fill the first row.
	 * @param emptyMatrix - the key is going to occupy the first row of matrix.
	 * @return - return the matrix with the key occupying the first row.
	 */
	private char[][] fillMatrixWithKey(char[] key, char[][] emptyMatrix) {
		for (int i = 0; i < key.length; i++) {
			emptyMatrix[0][i] = key[i];
		}
		return emptyMatrix;
	}

	/**
	 * Fills the matrix columns with the characters from the encrypted text. The
	 * first row is skipped since it is already occupied by the key. This method
	 * takes characters from the encrypted text and fills the matrix columns.
	 * 
	 * @param encryptedText - the encrypted text to fill out the matrix.
	 * @param matrixWithKey - the matrix to be filled with the encrypted text.
	 * @return - completely filled matrix with both the encrypted text and the key.
	 */
	private char[][] fillMatrixColumnsWithText(String encryptedText, char[][] matrixWithKey) {
		int encryptedTextIndex = 0;

		for (int col = 0; col < matrixWithKey[0].length; col++) {
			for (int row = 1; row < matrixWithKey.length; row++) {
				matrixWithKey[row][col] = encryptedText.charAt(encryptedTextIndex);
				encryptedTextIndex++;
			}
		}
		return matrixWithKey;
	}

	/**
	 * Reorders the columns of the matrix based on the index positions provided by
	 * in the eachIndexOfKey array. This method ensures the matrix columns are in
	 * the correct order for decryption.
	 * 
	 * @param matrix         - the matrix to be reordered.
	 * @param eachIndexOfKey - array containing desired order for the matrix
	 *                       columns.
	 * @return - the matrix after the columns have been reordered.
	 */
	private char[][] reorderMatrix(char[][] matrix, int[] eachIndexOfKey) {
		char[][] reorderedMatrix = new char[matrix.length][matrix[0].length];

		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				reorderedMatrix[row][col] = matrix[row][eachIndexOfKey[col]];
			}
		}
		return reorderedMatrix;
	}

	/**
	 * Changes the matrix into a continous string by combining charcaters row by
	 * row. The first row (which contains the key) is excluded from this step.
	 * 
	 * @param matrix - the matrix rows will be concatenated into a string.
	 * @return - the String containing the transposed rows.
	 */
	private String transposeRows(char[][] matrix) {
		StringBuilder transposedRows = new StringBuilder();

		// Loop through each row skipping the first row; int row = 1
		for (int row = 1; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				transposedRows.append(matrix[row][col]);
			}
		}
		return transposedRows.toString();
	}

	/**
	 * Decrypts the given string that represents the rows that were transposed. It
	 * uses the ADFGVX cipher technique - pairing charcaters.
	 * 
	 * @param transposedRows - the swapped around rows to be decrypted.
	 * @return the decrypted text.
	 */
	private String decryptText(String transposedRows) {
		StringBuilder decryptedText = new StringBuilder();

		for (int i = 0; i < transposedRows.length() - 1; i += 2) {
			String encryptedCharacter = Character.toString(transposedRows.charAt(i)) + transposedRows.charAt(i + 1);
			char decryptedCharacter = decryptedADFGVXCharacter(encryptedCharacter);
			decryptedText.append(decryptedCharacter);
		}
		return decryptedText.toString();
	}

	/**
	 * Decrypts a ADFGVX character pair using the Polybius square above.
	 * 
	 * @param encryptedCharacter - the ADFGVX character pair ro be decrypted.
	 * @return - the decrypted character.
	 * @throws IllegalArgumentException - if the encryptedCharacter is invalid or
	 *                                  cannot be found.
	 */
	private char decryptedADFGVXCharacter(String encryptedCharacter) {
		// Check if the character pair is valid.
		if (encryptedCharacter == null || encryptedCharacter.length() != 2) {
			throw new IllegalArgumentException("Invalid ADFGVX character pair");
		}

		int rowIndex = -1;
		int columnIndex = -1;
		// Identify the row index based on the first character.
		for (int i = 0; i < ADFGVX_ARRAY.length; i++) {
			if (ADFGVX_ARRAY[i] == encryptedCharacter.charAt(0)) {
				rowIndex = i;
				break;
			}
		}
		// Identify the row index based on the second character.
		for (int i = 0; i < ADFGVX_ARRAY.length; i++) {
			if (ADFGVX_ARRAY[i] == encryptedCharacter.charAt(1)) {
				columnIndex = i;
				break;
			}
		}
		// If either index was not found, throw an error.
		if (rowIndex == -1 || columnIndex == -1) {
			throw new IllegalArgumentException("Invalid ADFGVX character pair");
		}
		// Use the Polybius square to get the decrypted character.
		return POLYBIUS_SQUARE[rowIndex][columnIndex];
	}

}
