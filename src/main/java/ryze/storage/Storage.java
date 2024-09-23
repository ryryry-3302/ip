package ryze.storage;

import ryze.parser.Parser;
import ryze.task.Task;
import ryze.task.TaskList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The {@code Storage} class manages the creation, reading, and writing of files
 * in the application data directory.
 */
public class Storage {

    private final Path DIRECTORYPATH;
    private final Path FILEPATH;


    /**
     * Constructs a {@code Storage} object with specified directory and file names.
     *
     * @param directory The directory where the data will be stored.
     * @param fileName  The name of the file where data will be stored.
     */
    public Storage(String directory, String fileName) {
        this.DIRECTORYPATH = Paths.get(directory);
        this.FILEPATH = DIRECTORYPATH.resolve(fileName);
    }

    /**
     * Initializes the data storage by creating the directory and file if they don't exist.
     * If the file exists, it will read its contents.
     */
    public void initialiseData(TaskList listOfChatHistory) {
        String filePathString = FILEPATH.toString();
        File file = new File(filePathString);
        System.out.println("File Path: " + filePathString);

        // Check if directory exists, if not, create it
        boolean isDirectoryMissing = !Files.exists(DIRECTORYPATH);
        boolean isFileMissing = !file.exists();

        if (isDirectoryMissing) { //Guard Clause
            handleDirectoryMissing();
            return;
        }

        if (isFileMissing) { //Guard Clause
            handleMissingData();
            return;
        }
        loadDataIntoHistory(listOfChatHistory);
    }

    private void loadDataIntoHistory(TaskList listOfChatHistory) {
        try {
            readFileContents(listOfChatHistory);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private void handleMissingData() {
        System.out.println("Data for Ryze not found! Welcome to Ryze new User!");
        try {
            Files.createFile(FILEPATH);
            System.out.println("New data file created.");
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    private void handleDirectoryMissing() {
        try {
            Files.createDirectories(DIRECTORYPATH);
            System.out.println("Directory created at: " + DIRECTORYPATH.toString());
        } catch (IOException e) {
            System.out.println("Error creating directory: " + e.getMessage());
            return;
        }
    }

    /**
     * Appends the specified text to the file.
     *
     * @param textToAppend The text to be appended to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void appendToFile(String textToAppend) throws IOException {
        try (FileWriter fw = new FileWriter(FILEPATH.toString(), true)) {
            fw.write(textToAppend + System.lineSeparator()); // Append text with newline
        }
    }

    /**
     * Reads the contents of the file and processes each line.
     *
     * @throws FileNotFoundException If the file does not exist.
     */
    private void readFileContents(TaskList listOfChatHistory) throws FileNotFoundException {
        File f = new File(FILEPATH.toString());
        Scanner scanner = new Scanner(f);
        while (scanner.hasNextLine()) {
            Parser.parseRyzeTxt(scanner.nextLine(), listOfChatHistory);
        }
        scanner.close();
    }

    private void overwriteSave(TaskList listOfChatHistory) throws IOException {
        ;
        Files.write(FILEPATH, new byte[0]);
        for (Task task : listOfChatHistory.getTasks()) {
            appendToFile(task.toData());
        }
    }

    public int handleSizeChange(int lastSize, TaskList listOfChatHistory) {
        int currentSize = listOfChatHistory.size();
        if (lastSize > currentSize) {
            try {
                overwriteSave(listOfChatHistory);
            } catch (IOException e) {
                System.out.println("Error overwriting save file: " + e.getMessage());
            }
        }
        lastSize = currentSize;
        return lastSize;
    }

}
