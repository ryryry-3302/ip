import java.util.Scanner;

/**
 * Ryze is a simple command-line bot for managing tasks.
 */
public class Ryze {

    private static final String DIVIDER = "➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤";
    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";

    private static int historyIndex = 0;  // Static variable for task index
    private static Task[] listOfChatHistory = new Task[100];  // Static task list

    public static void main(String[] args) {
        initialiseMessage();

        Scanner scanner = new Scanner(System.in);
        String line;

        while (!(line = scanner.nextLine()).equals(EXIT_COMMAND)) {
            processCommand(line);
        }

        exitMessage();
    }

    private static void processCommand(String line) {
        String command = getCommand(line);

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
            break;
        default:
            System.out.println("Yoo that command doesn't exist yet lmao");
            break;
        }
    }

    private static String getCommand(String line) {
        return line.split(" ")[0];
    }

    private static void listTasks() {
        printDivider();
        if (historyIndex == 0) {
            System.out.println("List empty");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        int taskCounter = 1;
        for (int i = 0; i < historyIndex; i++) {
            Task task = listOfChatHistory[i];
            if (task != null) {
                System.out.printf("%d.%s%n", taskCounter, task);
                taskCounter++;
            }
        }
        System.out.println();
        printDivider();
    }

    private static void addTodoTask(String line) {
        if (line.equals(TODO_COMMAND)) {
            System.out.println("Please specify a todo");
            return;
        }
        String description = line.replace(TODO_COMMAND, "").trim();
        listOfChatHistory[historyIndex] = new Todo(description);
        echo(listOfChatHistory[historyIndex].toString(), historyIndex + 1);
        historyIndex++;
    }

    private static void addDeadlineTask(String line) {
        try {
            String[] parts = line.split("/by ", 2);
            String description = parts[0].replace(DEADLINE_COMMAND, "").trim();
            String deadline = parts[1].trim();
            if (description.isEmpty() || deadline.isEmpty()) {
                System.out.println("Invalid command format.");
                return;
            }
            listOfChatHistory[historyIndex] = new Deadline(description, deadline);
            echo(listOfChatHistory[historyIndex].toString(), historyIndex + 1);
            historyIndex++;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please specify a deadline");
        }
    }

    private static void addEventTask(String line) {
        String[] parts = line.split("/from | /to ", 3);
        if (parts.length == 3) {
            String eventDescription = parts[0].replace(EVENT_COMMAND, "").trim();
            String startTime = parts[1].trim();
            String endTime = parts[2].trim();
            listOfChatHistory[historyIndex] = new Event(eventDescription, startTime, endTime);
            echo(listOfChatHistory[historyIndex].toString(), historyIndex + 1);
            historyIndex++;
        } else {
            System.out.println("Invalid command format.");
        }
    }

    private static void markOrUnmarkTask(String line, String command) {
        try {
            int taskNumber = Integer.parseInt(line.split(" ")[1]);
            if (taskNumber > 0 && taskNumber <= historyIndex) {
                Task task = listOfChatHistory[taskNumber - 1];
                if (command.equals(MARK_COMMAND)) {
                    if (task.isDone()) {
                        System.out.println("You have already completed this task.");
                    } else {
                        task.markAsDone();
                        printDivider();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.printf("  %s%n%n", task);
                    }
                } else {
                    if (!task.isDone()) {
                        System.out.println("You have already uncompleted this task.");
                    } else {
                        task.markAsNotDone();
                        printDivider();
                        System.out.println("Okay! I've marked this task as not done:");
                        System.out.printf("  %s%n%n", task);
                    }
                }
                printDivider();
            } else {
                printDivider();
                System.out.println("Task number is out of range.\n");
                printDivider();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid task number format.");
        }
    }

    private static void echo(String userInput, int count) {
        System.out.println();
        printDivider();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + userInput);
        System.out.println("Now you have " + count + (count == 1 ? " task" : " tasks") + " in the list.");
        System.out.println();
        printDivider();
        System.out.println();
    }

    private static void printDivider() {
        System.out.println(DIVIDER);
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
        System.out.println("\n\n" +
                " ____                _  \n" +
                "|  _ \\ _   _ _______| | \n" +
                "| |_) | | | |_  / _ \\ | \n" +
                "|  _ <| |_| |/ /  __/_| \n" +
                "|_| \\_\\\\__, /___\\___(_) \n" +
                "       |___/            \n" +
                "\n ");
        System.out.println("What can I do for you?\n");
        printDivider();
        System.out.println();
    }
}
