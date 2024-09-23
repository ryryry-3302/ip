package ryze;

import ryze.exceptions.InvalidNumberArguments;
import ryze.exceptions.RyzeException;
import ryze.task.Deadline;
import ryze.task.Event;
import ryze.task.Task;
import ryze.task.Todo;
import ryze.ui.Ui;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;
import java.io.File;

/**
 * Ryze is a simple command-line bot for managing tasks.
 */
public class Ryze {

    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String TODO = "T";
    private static final String DEADLINE = "D";
    private static final String EVENT = "E";
    private Ui ui;


    private static ArrayList<Task> listOfChatHistory = new ArrayList<>();  // Static task list

    public Ryze(){
        ui = new Ui();
        initialiseData();


    }
    public void run() {
        ui.initialiseMessage();
        Scanner scanner = new Scanner(System.in);
        String line;
        int lastSize = listOfChatHistory.size();
        while (!(line = scanner.nextLine()).equals(EXIT_COMMAND)) {
            processCommand(line);
            lastSize = handleSizeChange(lastSize);
        }
        ui.exitMessage();
    }


    public static void main(String[] args) {
        new Ryze().run();
    }

    private static int handleSizeChange(int lastSize) {
        int currentSize = listOfChatHistory.size();
        if (lastSize > currentSize){
            try{
                overwriteSave();
            } catch (IOException e) {
                System.out.println("Error overwriting save file" + e.getMessage());
            }
        }
        lastSize = currentSize;
        return lastSize;
    }

    private static void initialiseData() {
        Path dirPath = Paths.get("data");
        Path filePath = dirPath.resolve("ryze.txt");
        String filePathString = filePath.toString();
        File file = new File(filePathString);
        System.out.println(filePathString);

        // Check if directory exists, if not, create it
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                System.out.println("Error creating directory: " + e.getMessage());
                return;
            }
        }

        // Check if file exists, if not, create it
        if (!file.exists()) {
            System.out.println("Data for Ryze not found! Welcome to Ryze new User!");
            try {
                Files.createFile(filePath); // Creates the file if it doesn't exist
            } catch (IOException e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }
        } else {
            try {
                readFileContents(filePathString);
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        }
    }

    private static void appendToFile(String textToAppend) throws IOException {
        Path dirPath = Paths.get("data");
        Path filePath = dirPath.resolve("ryze.txt");
        String filePathString = filePath.toString();
        FileWriter fw = new FileWriter(filePathString, true); // create a FileWriter in append mode
        fw.write(textToAppend);
        fw.close();
    }

    private static void readFileContents(String filePath) throws FileNotFoundException {
        File f = new File(filePath); // create a File for the given file path
        Scanner s = new Scanner(f); // create a Scanner using the File as the source
        while (s.hasNext()) {
            parseRyzeTxt(s.nextLine());
        }
    }

    private static void parseRyzeTxt(String dataEntry) {
        String taskType = dataEntry.split("~")[0];
        switch (taskType) {
        case TODO:
            parseTodo(dataEntry);
            break;
        case DEADLINE:
            parseDeadline(dataEntry);
            break;
        case EVENT:
            parseEvent(dataEntry);
            break;
        default:
            System.out.println("Invalid task type: " + taskType);
        }
    }

    private static void parseEvent(String dataEntry) {
        String description = dataEntry.split("~")[2];
        String from = dataEntry.split("~")[3];
        String to = dataEntry.split("~")[4];
        Task newEvent = new Event(description,from,to);
        boolean isDone = Integer.parseInt(dataEntry.split("~")[1]) != 0;
        if(isDone){
            newEvent.markAsDone();
        }
        listOfChatHistory.add(newEvent);
    }

    private static void parseDeadline(String dataEntry) {
        String description = dataEntry.split("~")[2];
        String by = dataEntry.split("~")[3];
        Task newDeadline = new Deadline(description, by);
        boolean isDone = Integer.parseInt(dataEntry.split("~")[1]) != 0;
        if(isDone){
            newDeadline.markAsDone();
        }
        listOfChatHistory.add(newDeadline);
    }

    private static void parseTodo(String dataEntry) {
        String description = dataEntry.split("~")[2];
        Task newTodo = new Todo(description);
        boolean isDone = Integer.parseInt(dataEntry.split("~")[1]) != 0;
        if(isDone){
            newTodo.markAsDone();
        }
        listOfChatHistory.add(newTodo);
    }

    private void processCommand(String line) {
        String command = getCommand(line);
        try {
            switch (command) {
            case LIST_COMMAND:
                listTasks();
                break;
            case TODO_COMMAND:
                addTodoTask(line);
                break;
            case DEADLINE_COMMAND:
                addDeadlineTask(line);
                break;
            case EVENT_COMMAND:
                addEventTask(line);
                break;
            case MARK_COMMAND:
            case UNMARK_COMMAND:
                markOrUnmarkTask(line, command);
                overwriteSave();
                break;
            case DELETE_COMMAND:
                deleteTask(line);
                break;
            default:
                throw new RyzeException("That command doesn't exist ??");
            }
        } catch (RyzeException e) {
            handleDukeException(e);
        } catch (IOException e){
            System.out.println("Something went wrong saving task to Ryze.txt: " + e.getMessage());
        }
    }


    private void handleDukeException(RyzeException e) {
        ui.printDivider();
        System.out.println(e.getMessage());
        System.out.println();
        ui.printDivider();
    }

    private static String getCommand(String line) {
        return line.split(" ")[0];
    }

    private void listTasks() {
        ui.printDivider();
        if (listOfChatHistory.isEmpty()) {
            System.out.println("List empty");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        int taskCounter = 1;
        for (Task task : listOfChatHistory) {
            if (task != null) {
                System.out.printf("%d.%s%n", taskCounter, task);
                taskCounter++;
            }
        }
        System.out.println();
        ui.printDivider();
    }

    private void addTodoTask(String line) throws InvalidNumberArguments, IOException {
        if (line.equals(TODO_COMMAND)) {
            throw new InvalidNumberArguments("Please specify todo");
        }
        String description = line.replace(TODO_COMMAND, "").trim();
        Task newTodo = new Todo(description);
        listOfChatHistory.add(newTodo);
        echo(newTodo.toString(), listOfChatHistory.size());
        appendToFile(newTodo.toData());
    }
    private void deleteTask(String line) throws InvalidNumberArguments{
        if (line.equals(DELETE_COMMAND)) {
            throw new InvalidNumberArguments("Please specify task to delete");
        }
        try {
            int taskNumber = Integer.parseInt(line.split(" ")[1]);
            if (taskNumber > 0 && taskNumber <= listOfChatHistory.size()) {
                ui.printDivider();
                System.out.println("Noted. I've removed this task:");
                System.out.println(" " + listOfChatHistory.get(taskNumber - 1).toString());
                listOfChatHistory.remove(taskNumber - 1);
                System.out.println("Now you have " + listOfChatHistory.size() + " task" + (listOfChatHistory.size() == 1 ?
                    "" : "s") + " in the list.");
                ui.printDivider();
            }
            else {
                System.out.println("Invalid task number");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid task number format.");
        }
    }

    private void addDeadlineTask(String line) throws InvalidNumberArguments, IOException {
        String[] parts = line.split("/by ", 2);
        if (parts.length == 2){
        String description = parts[0].replace(DEADLINE_COMMAND, "").trim();
        String deadline = parts[1].trim();
        Task newDeadline = new Deadline(description, deadline);
        listOfChatHistory.add(newDeadline);
        echo(newDeadline.toString(), listOfChatHistory.size());
        appendToFile(newDeadline.toData());
        }
        else {
            throw new InvalidNumberArguments("Invalid command format for adding deadline.");
        }
    }

    private void addEventTask(String line) throws InvalidNumberArguments, IOException {
        String[] parts = line.split("/from | /to ", 3);
        if (parts.length == 3) {
            String eventDescription = parts[0].replace(EVENT_COMMAND, "").trim();
            String startTime = parts[1].trim();
            String endTime = parts[2].trim();
            Task newEvent = new Event(eventDescription, startTime, endTime);
            listOfChatHistory.add(newEvent);
            echo(newEvent.toString(), listOfChatHistory.size());
            appendToFile(newEvent.toData());
        } else {
            throw new InvalidNumberArguments("Invalid command format for adding event.");
        }
    }

    private void markOrUnmarkTask(String line, String command) {
        try {
            int taskNumber = Integer.parseInt(line.split(" ")[1]);
            if (taskNumber > 0 && taskNumber <= listOfChatHistory.size()) {
                Task task = listOfChatHistory.get(taskNumber -1);
                if (command.equals(MARK_COMMAND)) {
                    if (task.isDone()) {
                        System.out.println("You have already completed this task.");
                    } else {
                        task.markAsDone();
                        ui.printDivider();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.printf("  %s%n%n", task);
                    }
                } else {
                    if (!task.isDone()) {
                        System.out.println("You have already uncompleted this task.");
                    } else {
                        task.markAsNotDone();
                        ui.printDivider();
                        System.out.println("Okay! I've marked this task as not done:");
                        System.out.printf("  %s%n%n", task);
                    }
                }
                ui.printDivider();
            } else {
                ui.printDivider();
                System.out.println("Task number is out of range.\n");
                ui.printDivider();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid task number format.");
        }
    }

    private void echo(String userInput, int count) {
        System.out.println();
        ui.printDivider();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + userInput);
        System.out.println("Now you have " + count + (count == 1 ? " task" : " tasks") + " in the list.");
        System.out.println();
        ui.printDivider();
        System.out.println();
    }




    private static void overwriteSave() throws IOException {
        Path dirPath = Paths.get("data");
        Path filePath = dirPath.resolve("ryze.txt");
        Files.write(filePath, new byte[0]);
        for (Task task : listOfChatHistory) {
            appendToFile(task.toData());
        }
    }
}
