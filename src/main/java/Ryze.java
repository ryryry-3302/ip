import java.util.Scanner;

/**
 * Ryze is a simple command-line bot for managing tasks.
 */
public class Ryze {

    /**
     * Prints a decorative divider line to the console.
     */
    public static void printDivider() {
        System.out.println("➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤");
    }

    /**
     * Echoes the user input surrounded by dividers.
     *
     * @param userInput The input string to echo.
     */
    public static void echo(String userInput, int count) {
        System.out.println();
        printDivider();
        System.out.println("Got it. I've added this task:");
        System.out.print("  ");
        System.out.println(userInput);
        if (count == 1) {
            System.out.println("Now you have 1 task in the list.");
        }
        else {
            System.out.println("Now you have " + count + " tasks in the list.");
        }
        System.out.println();
        printDivider();
        System.out.println();
    }

    /**
     * Main method to run the Ryze application.
     *
     * @param args Command-line arguments (not used).
     */


    public static void main(String[] args) {
        Task[] listOfChatHistory = new Task[100];
        int historyIndex = 0;

        initialiseMessage();

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        while (!line.equals("bye")) {
            String command = line.split(" ")[0];
            int taskNumber = -1;

            switch (command) {
            case "list":
                listTasks(historyIndex, listOfChatHistory);
                break;

            case "todo":
                if (line.equals("todo")){
                    System.out.println("Please specify a todo");
                    break;
                }
                else{
                    String description = line.replace("todo","").trim();
                    listOfChatHistory[historyIndex] = new Todo(description);
                    echo(listOfChatHistory[historyIndex].toString(), historyIndex+1);
                    historyIndex++;
                    break;
                }

            case "deadline":
                try {
                    String[] parts = line.split("/by ");
                    String description = parts[0].trim().replace("deadline", "").trim();
                    String deadline = parts[1].trim();
                    if (description.isEmpty() || deadline.isEmpty()) {
                        System.out.println("Invalid command format.");
                        break;
                    }
                    listOfChatHistory[historyIndex] = new Deadline(description, deadline);
                    echo(listOfChatHistory[historyIndex].toString(), historyIndex+1);
                    historyIndex++;
                    break;
                }
                catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("Please specify a deadline");
                    break;
                }

            case "event":
                String[] parts = line.split("/from | /to ", 3);
                if (parts.length == 3) {
                    // Trim to remove any leading or trailing spaces
                    String eventDescription = parts[0].replace("event ","").trim();
                    String startTime = parts[1].trim();
                    String endTime = parts[2].trim();

                    // Print the event details
                    listOfChatHistory[historyIndex] = new Event(eventDescription, startTime, endTime);
                    echo(listOfChatHistory[historyIndex].toString(), historyIndex+1);
                    historyIndex++;
                } else {
                    // Handle cases where the command does not have the correct format
                    System.out.println("Invalid command format.");
                }
                break;


            case "mark":
            case "unmark":
                try {
                    taskNumber = Integer.parseInt(line.split(" ")[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid task number format.");
                    break;
                }

                if (taskNumber > 0 && taskNumber <= historyIndex) {
                    Task task = listOfChatHistory[taskNumber-1];
                    if(command.equals("mark") && task.isDone()) {
                        System.out.println("You have already completed this task.");
                        break;
                    }
                    if (command.equals("unmark") && !task.isDone()) {
                        System.out.println("You have already uncompleted this task.");
                        break;
                    }
                    if (command.equals("mark")) {
                        listOfChatHistory[taskNumber - 1].markAsDone();
                        printDivider();
                        System.out.println("Nice! I've marked this task as done:");
                    } else {
                        listOfChatHistory[taskNumber - 1].markAsNotDone();
                        printDivider();
                        System.out.println("Okay! I've marked this task as not done:");
                    }
                    System.out.printf("  %s\n\n", listOfChatHistory[taskNumber - 1].toString());
                    printDivider();
                } else {
                    printDivider();
                    System.out.println("Task number is out of range.\n");
                    printDivider();
                }
                break;

            default:
                System.out.println("Invalid command.");
                break;
            }

            line = scanner.nextLine();
        }

        exitMessage();
    }

    private static void listTasks(int historyIndex, Task[] listOfChatHistory) {
        printDivider();
        if (historyIndex == 0) {
            System.out.println("List empty");
            return;
        }
        System.out.println("Here are the tasks in your list");
        int taskCounter = 1;
        for (Task task : listOfChatHistory) {
            if (task == null) {
                break;
            }
            System.out.printf(taskCounter+"." + task.toString());
            System.out.println();
            taskCounter++;
        }
        System.out.println();
        printDivider();
    }

    private static void exitMessage() {
        System.out.println();
        printDivider();
        System.out.println("Bye. Hope to see you again soon!\n");
        printDivider();
    }

    private static void initialiseMessage() {
        printDivider();
        System.out.println("Hello! I'm Ryze!");
        System.out.println("What can I do for you?\n");
        printDivider();
        System.out.println();
    }
}
