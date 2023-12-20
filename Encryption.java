package ie.atu.sw;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

/**
 * Encryption class that contains methods to encrypt plaintext using a
 * combination of Polybius square and a columnar transposition based on a
 * user-defined key. Encrypts files from the input directory and saves the
 * results to an output directory.
 */
public class Encryption {
	private InputDirectory inputDirectory; 		// Directory with files to encrypt.
	private OutputDirectory outputDirectory; 	// Location for encrypted files.
	private Key key; 							// Encryption key.
	private Parser parser; 						// For file parsing.

	private static final char[][] POLYBIUS_SQUARE = { 
			{ 'P', 'H', '0', 'Q', 'G', '6' },
			{ '4', 'M', 'E', 'A', '1', 'Y' }, 
			{ 'L', '2', 'N', 'O', 'F', 'D' }, 
			{ 'X', 'K', 'R', '3', 'C', 'V' },
			{ 'S', '5', 'Z', 'W', '7', 'B' }, 
			{ 'J', '9', 'U', 'T', 'I', '8' } };

	private static final char[] ADFGVX_ARRAY = { 'A', 'D', 'F', 'G', 'V', 'X' };

	/**
	 * Constructor - a new Encryption instance with specified directories and key.
	 * Initialises a parser object to help with the encrypting process.
	 * 
	 * @param inputDirectory  - source of files to be encrypted.
	 * @param outputDirectory - location for storing encryted files.
	 * @param key             - for the encrypting files.
	 */
	public Encryption(InputDirectory inputDirectory, OutputDirectory outputDirectory, Key key) {
		this.inputDirectory = inputDirectory;
		this.outputDirectory = outputDirectory;
		this.key = key;
		this.parser = new Parser();
	}

	/**
	 * Method that handles the encryption. Reads files from the input directory,
	 * encrypts the contents of that directory and writes the encrypted text to the
	 * output folder.
	 * 
	 * @return - the encrypted version of the plaintext.
	 */
	public void handleEncryption() {
		String transposedText;
		if (!directoriesAndKeyAreSet()) {
			return;
		}
		try {
			System.out.println("");
			System.out.println("Please be patient! ");
			System.out.println("Encrypting file(s)...");
			System.out.println("");
			// Parse files in the directory to extract plaintext.
			String[] plaintexts = parser.parseFilesInDirectory(inputDirectory.getInputDirectory());

			int fileNumber = 0;
			for (String plaintext : plaintexts) {
				String encryptedText = encryptPlaintext(plaintext);

				// Determines the appropriate size of the matrix based on the encrypted text and
				// key.
				char[][] emptyMatrix = setMatrixSize(encryptedText, key.getKey());

				// Fill this matrix with the key at the top row.
				char[][] matrixWithKey = fillMatrixWithKey(key.getKey(), emptyMatrix);

				// Fill in the rest of the matrix with the encrypted text.
				char[][] filledMatrix = fillMatrixWithText(encryptedText, matrixWithKey);

				// Order the sortedKey alphanumerically.
				char[] sortedKey = orderKeyAlphanumerically(key.getKey());

				// Get the order in which columns should be read based on the key.
				int[] orderOfKeyIndices = determineColumnOrder(key.getKey(), sortedKey);

				// Perform the columar Transposition to get the final encrypted text.
				transposedText = performColumnarTransposition(filledMatrix, orderOfKeyIndices);

				// Write encrypted text to a file in the output directory.
				File file = new File(outputDirectory.getOutputDirectory(), "encrypted" + fileNumber + ".txt");

				try (FileWriter fileWriter = new FileWriter(file);
						BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
					bufferedWriter.write(transposedText);
				}
				fileNumber++;
			}
			System.out.println("Your Encryption is Complete!");
		} catch (Exception e) {
			System.out.println("An error occurred while encrypting: " + e.getMessage());
		}
	}

	/**
	 * This methods checks if the input directory, output directory and/ or the key
	 * are set before encrypting. If any/all of them are NOT set, a message is
	 * printed to the user to tell them what needs to be done next.
	 * 
	 * @return - true if all three (input, output and key) are set.
	 */
	private boolean directoriesAndKeyAreSet() {

		if (inputDirectory.getInputDirectory() == null) {
			System.out.println("Please select an input directory before continuing!");
		}
		if (outputDirectory.getOutputDirectory() == null) {
			System.out.println("Please select an output directory before continuing!");
		}
		if (key.getKey() == null) {
			System.out.println("Please select a key before continuing!");
		}
		return inputDirectory.getInputDirectory() != null && outputDirectory.getOutputDirectory() != null
				&& key.getKey() != null;
	}

	/**
	 * A single character is encoded using the ADFGVX cipher. The method iterates
	 * through the ADFGVX array and the Polybius square to find the corresponding
	 * encoded character pair. If the character cannot be found in the Polybius
	 * square, an exception is thrown.
	 * 
	 * @param character - takes in a character to be encoded
	 * @return - returns the encoded character as a String formed by two character
	 *         pairs in the ADFGVX array.
	 * @throws IllegalArgumentException - If character is not found within the
	 *                                  Polybius square
	 */
	private String encodeCharacters(char character) {
		for (int row = 0; row < ADFGVX_ARRAY.length; row++) {
			for (int column = 0; column < ADFGVX_ARRAY.length; column++) {
				if (POLYBIUS_SQUARE[row][column] == character) {
					return Character.toString(ADFGVX_ARRAY[row]) + ADFGVX_ARRAY[column];
				}
			}
		}
		throw new IllegalArgumentException("Oops! The Polybius Square does NOT contain this character: " + character);
	}

	/**
	 * Encrypts the provided plaintext using the ADFGVX cipher. It encodes each
	 * character of the plaintext and appends the encoded result to produce the
	 * final encrpyted text.
	 * 
	 * @param plaintext - the original unencrypted text to be processed.
	 * @return the encrypted version of the plaintext.
	 */
	private String encryptPlaintext(String plaintext) {
		StringBuilder encryptedText = new StringBuilder();

		for (int i = 0; i < plaintext.length(); i++) {
			String encodedCharacter = encodeCharacters(plaintext.charAt(i));
			encryptedText.append(encodedCharacter);
		}
		return encryptedText.toString();
	}

	/**
	 * Determines the size of the Matrix based on the length of the encrypted text
	 * and the key. The size is important to properly position characters within the
	 * matrix.
	 * 
	 * @param encryptedText - text to be encrypted to determines the number rows.
	 * @param key           - encryption key length determines the number columns.
	 * @return - an empty matrix with determined dimensions (row and columns).
	 */
	private char[][] setMatrixSize(String encryptedText, char[] key) {
		int columns = key.length;

		// Calculate the number of characters that do not fit perfectly into the matrix.
		int clip = encryptedText.length() % key.length;

		// Remove those characters to get a clipped version of the encrypted text.
		String clippedEncryptedText = encryptedText.substring(0, encryptedText.length() - clip);

		// Determines number of rows based on clipped encrypted text length and number
		// of columns.
		int rows = (int) Math.ceil((double) clippedEncryptedText.length() / columns);

		char[][] matrix = new char[rows + 1][columns];

		return matrix;
	}

	/**
	 * Fills in the first row of the matrix with the characters from the users
	 * chosen key.
	 * 
	 * @param key         - using characters in the key to fill first row.
	 * @param emptyMatrix - the key is going to occupy the first row.
	 * @return - return the matrix with the key filling out the first row.
	 */
	private char[][] fillMatrixWithKey(char[] key, char[][] emptyMatrix) {
		for (int i = 0; i < key.length; i++) {
			emptyMatrix[0][i] = key[i];
		}
		return emptyMatrix;
	}

	/**
	 * After the key is occupying the first row, fills the rest of the matrix with
	 * the characters from the encrypted text.
	 * 
	 * @param encryptedText - encrypted text to fill out the rest of the matrix.
	 * @param matrixWithKey - the matrix with first row already filled with key.
	 * @return - the matrix with both the encrypted text and the key.
	 */
	private char[][] fillMatrixWithText(String encryptedText, char[][] matrixWithKey) {
		int encryptedTextIndex = 0;

		for (int row = 1; row < matrixWithKey.length; row++) {
			for (int col = 0; col < matrixWithKey[0].length; col++) {
				matrixWithKey[row][col] = encryptedText.charAt(encryptedTextIndex);
				encryptedTextIndex++;
			}
		}
		return matrixWithKey;
	}

	/**
	 * Makes a copy of the original key and sorts it alphanumerically - i.e. numbers
	 * first followed by letters. This does NOT alter the original key but instead
	 * creates and returns a sorted version.
	 * 
	 * @param key - The key used to be sorted alphanumerically.
	 * @return - new char array of the alphanumeric sorted copy of the key.
	 */
	private char[] orderKeyAlphanumerically(char[] key) {
		char[] copiedKey = Arrays.copyOf(key, key.length);

		Arrays.sort(copiedKey);
		char[] sortedKey = copiedKey;

		return sortedKey;
	}

	/**
	 * Determines the columns order based on the alphanumeric sorting of the sorted
	 * key.
	 * 
	 * @param key - encryption key - order will be used to determine the column
	 *            order.
	 * @return - an array of each index representing which order the columns should
	 *         be read.
	 */
	private int[] determineColumnOrder(char[] originalKey, char[] sortedKey) {

		int[] order = new int[originalKey.length];

		for (int i = 0; i < originalKey.length; i++) {
			for (int j = 0; j < sortedKey.length; j++) {
				if (originalKey[i] == sortedKey[j] && order[j] == 0) {
					order[j] = i;
					break;
				}
			}
		}
		return order;
	}

	/**
	 * Based on the sorted order of the key, characters from the matrix are removed
	 * in a specific sequence (performing a columnar transposition). This method
	 * rearranges the matrix's columns as determined by the sorted (alphanumeric)
	 * key.
	 * 
	 * @param matrix - The matrix that contains the key and the encrypted text.
	 * @param key    - key determines which order the columns should be read.
	 * @return - a string created by reading the matrix columns in the order
	 *         determined by the key.
	 */
	private String performColumnarTransposition(char[][] matrix, int[] orderOfKeyIndices) {
		StringBuilder transposedText = new StringBuilder();

		for (int index : orderOfKeyIndices) {
			for (int row = 1; row < matrix.length; row++) {
				transposedText.append(matrix[row][index]);
			}
		}
		return transposedText.toString();
	}

}