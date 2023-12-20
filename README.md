## ADFGVX File Encryption

@version javaSE-17

Short Description:
• This java application is designed to encrypt and decrypt text files using a ADFGVX cipher. 
Through a user-friendly interface, users can smoothly navigate between different functions.

### To Run:
Please follow these steps:

• Ensure that Java 17 has been installed.
• Download the zip file and extract its contents to your desired location.
• Open your command line interface and navigate to the file's location/ directory path until you 
reach the ‘src’ path.
• Compile the java file: javac ie/atu/sw/Runner.java
• After successfully compiling, run the application: java ie.atu.sw.Runner
• The main menu will guide the user through various functions: 
o start by selecting ‘Input file Directory’ and choosing a folder on your computer with 
the text files you would like to encrypt.

### Features
• Directory Management:
o Input Directory: Choose a directory to encrypt or decrypt.
o Output Directory: Specify where encrypted or decrypted files are written.
o View Files: User can preview files in the chosen directory.
o Modify Directories: Ability to change both input and output directories and create a 
new directory if they wish.

• Encryption and Decryption:
o ADFGVX Cipher: Uses Polybius square and user keywords for a unique encryption.
o Encrypt: Transforms plaintext files into cipher text.
o Decrypt: Using the correct key, cipher text is decrypted back to plaintext.

• Data Parsing:
o Text File Parsing: Reads the files and removes all unwanted characters (white spaces, 
special characters and converts text to uppercase).

• Security and Customization:
o Key Setting: Option for users to set and change their own unique encryption key.

• User Interaction and Accessibility:
o Option Menu: View or clear current settings
o User-Friendly Design: Application is clear, easy to use and navigate.

• Feedback and Error Handling:
o User Feedback: Clear system feedback.
o Error Guidance: Handling and guiding users through errors
