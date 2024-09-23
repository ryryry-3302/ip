package ryze.parser;

import ryze.exceptions.InvalidNumberArguments;
import ryze.exceptions.RyzeException;
import ryze.storage.Storage;
import ryze.task.Task;
import ryze.task.TaskList;
import ryze.task.Event;
import ryze.task.Deadline;
import ryze.task.Todo;
import ryze.ui.Ui;

import java.io.IOException;

public class Parser {
    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";
    private static final String TODO = "T";
    private static final String DEADLINE = "D";
    private static final String EVENT = "E";
    private static Boolean isDone = false;

    /**
     * Checks if the program has been marked as done.
     *
     * @return true if the program is finished (i.e., "bye" command was issued), otherwise false.
     */
    public boolean done(){
        return isDone;
    }

    /**
     * Processes the user's input command and performs the appropriate action, such as adding a task,
     * marking/unmarking a task, listing tasks, or deleting a task.
     *
     * @param line the input command from the user
     * @param storage the storage system used for saving/loading tasks
     * @param ui the user interface to interact with the user
     * @param listOfChatHistory the task list storing the user's tasks
     * @throws RyzeException if the command is invalid or another issue occurs during command execution
     */
    public void processCommand(String line, Storage storage, Ui ui, TaskList listOfChatHistory) throws RyzeException {
        String command = getCommand(line);
        try {
            switch (command) {
            case LIST_COMMAND:
                listTasks(ui, listOfChatHistory);
                break;
            case TODO_COMMAND:
                addTodoTask(line, ui, listOfChatHistory, storage);
                break;
            case DEADLINE_COMMAND:
                addDeadlineTask(line, ui, listOfChatHistory, storage);
                break;
            case EVENT_COMMAND:
                addEventTask(line, ui, listOfChatHistory, storage);
                break;
            case MARK_COMMAND:
            case UNMARK_COMMAND:
                markOrUnmarkTask(line, command, ui, listOfChatHistory, storage);
                ;
                break;
            case DELETE_COMMAND:
                deleteTask(line, ui, listOfChatHistory, storage);
                break;
            case FIND_COMMAND:
                findTask(line, ui, listOfChatHistory);
                break;
            case EXIT_COMMAND:
                isDone = true;
                break;
            default:
                throw new RyzeException("That command doesn't exist ??");
            }
        } catch (RyzeException e) {
            ui.handleRyzeException(e);
        } catch (IOException e) {
            System.out.println("Something went wrong saving task to Ryze.txt: " + e.getMessage());
        }
    }



    private static String getCommand(String line) {
        return line.split(" ")[0];
    }

    private void listTasks(Ui ui, TaskList listOfChatHistory) throws RyzeException {
        ui.printDivider();
        if (listOfChatHistory.isEmpty()) {
            System.out.println("List empty");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        int taskCounter = 1;
        for (Task task : listOfChatHistory.getTasks()) {
            if (task != null) {
                System.out.printf("%d.%s%n", taskCounter, task);
                taskCounter++;
            }
        }
        System.out.println();
        ui.printDivider();
    }

    private void addTodoTask(String line, Ui ui, TaskList listOfChatHistory, Storage storage) throws InvalidNumberArguments,
            IOException {
        if (line.equals(TODO_COMMAND)) {
            throw new InvalidNumberArguments("Please specify todo");
        }
        String description = line.replace(TODO_COMMAND, "").trim();
        Task newTodo = new Todo(description);
        listOfChatHistory.addTask(newTodo);
        ui.echo(newTodo.toString(), listOfChatHistory.size());
        storage.appendToFile(newTodo.toData());
    }

    private void deleteTask(String line, Ui ui, TaskList listOfChatHistory, Storage storage) throws InvalidNumberArguments {
        if (line.equals(DELETE_COMMAND)) {
            throw new InvalidNumberArguments("Please specify task to delete");
        }
        try {
            int taskNumber = Integer.parseInt(line.split(" ")[1]);
            if (taskNumber > 0 && taskNumber <= listOfChatHistory.size()) {
                ui.printDivider();
                System.out.println("Noted. I've removed this task:");
                System.out.println(" " + listOfChatHistory.getTask(taskNumber - 1).toString());
                listOfChatHistory.removeTask(taskNumber - 1);
                System.out.println("Now you have " + listOfChatHistory.size() + " task" + (listOfChatHistory.size() == 1 ? "" : "s") + " in the list.");
                ui.printDivider();
            } else {
                System.out.println("Invalid task number");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid task number format.");
        }
    }

    private void addDeadlineTask(String line, Ui ui, TaskList listOfChatHistory, Storage storage) throws InvalidNumberArguments, IOException {
        String[] parts = line.split("/by ", 2);
        if (parts.length == 2) {
            String description = parts[0].replace(DEADLINE_COMMAND, "").trim();
            String deadline = parts[1].trim();
            Task newDeadline = new Deadline(description, deadline);
            listOfChatHistory.addTask(newDeadline);
            ui.echo(newDeadline.toString(), listOfChatHistory.size());
            storage.appendToFile(newDeadline.toData());
        } else {
            throw new InvalidNumberArguments("Invalid command format for adding deadline.");
        }
    }

    private void addEventTask(String line, Ui ui, TaskList listOfChatHistory, Storage storage) throws InvalidNumberArguments, IOException {
        String[] parts = line.split("/from | /to ", 3);
        if (parts.length == 3) {
            String eventDescription = parts[0].replace(EVENT_COMMAND, "").trim();
            String startTime = parts[1].trim();
            String endTime = parts[2].trim();
            Task newEvent = new Event(eventDescription, startTime, endTime);
            listOfChatHistory.addTask(newEvent);
            ui.echo(newEvent.toString(), listOfChatHistory.size());
            storage.appendToFile(newEvent.toData());
        } else {
            throw new InvalidNumberArguments("Invalid command format for adding event.");
        }
    }

    private void markOrUnmarkTask(String line, String command, Ui ui, TaskList listOfChatHistory, Storage storage) {
        try {
            int taskNumber = Integer.parseInt(line.split(" ")[1]);
            boolean isIndexInvalid = taskNumber <= 0 || taskNumber > listOfChatHistory.size();
            boolean isMarkCommand = command.equals(MARK_COMMAND);

            if (isIndexInvalid) {
                ui.printDivider();
                System.out.println("Task number is out of range.\n");
                ui.printDivider();
                return;
            }

            Task task = listOfChatHistory.getTask(taskNumber - 1);
            ui.printDivider();

            if (isMarkCommand) {
                markTask(task, ui);
            } else {
                unmarkTask(task, ui);
            }

            ui.printDivider();
        } catch (NumberFormatException e) {
            System.out.println("Invalid task number format.");
        }
    }

    private void markTask(Task task, Ui ui) {
        if (task.isDone()) {
            System.out.println("You have already completed this task.");
        } else {
            task.markAsDone();
            System.out.println("Nice! I've marked this task as done:");
            System.out.printf("  %s%n%n", task);
        }
    }

    private void unmarkTask(Task task, Ui ui) {
        if (!task.isDone()) {
            System.out.println("You have already uncompleted this task.");
        } else {
            task.markAsNotDone();
            System.out.println("Okay! I've marked this task as not done:");
            System.out.printf("  %s%n%n", task);
        }
    }

    /**
     * Finds Task entries containing the search term in their description.
     */
    private void findTask(String line, Ui ui, TaskList listOfChatHistory) throws InvalidNumberArguments, IOException {
        if (line.equals(TODO_COMMAND)) {
            throw new InvalidNumberArguments("Please specify search term");
        }
        String[] parts = line.split(" ", 2);
        String searchTerm = parts[1].trim();
        TaskList listOfTasks = new TaskList();
        for (Task task : listOfChatHistory.getTasks()) {
            if(task.getDescription().contains(searchTerm)){
                listOfTasks.addTask(task);
            }
        }
        try{
            listTasks(ui,listOfTasks);
        } catch (RyzeException e) {
            ui.handleRyzeException(e);
        }
    }

    /**
     * Parses data entries from Ryze's storage file and adds the tasks to the task list.
     *
     * @param dataEntry a string representation of the stored task
     * @param listOfChatHistory the task list to which parsed tasks are added
     */
    public static void parseRyzeTxt(String dataEntry, TaskList listOfChatHistory) {
        String taskType = dataEntry.split("~")[0];
        switch (taskType) {
        case TODO:
            parseTodo(dataEntry, listOfChatHistory);
            break;
        case DEADLINE:
            parseDeadline(dataEntry, listOfChatHistory);
            break;
        case EVENT:
            parseEvent(dataEntry, listOfChatHistory);
            break;
        default:
            System.out.println("Invalid task type: " + taskType);
        }
    }


    private static void parseEvent(String dataEntry, TaskList listOfChatHistory) {
        String description = dataEntry.split("~")[2];
        String from = dataEntry.split("~")[3];
        String to = dataEntry.split("~")[4];
        Task newEvent = new Event(description,from,to);
        boolean isDone = Integer.parseInt(dataEntry.split("~")[1]) != 0;
        if(isDone){
            newEvent.markAsDone();
        }
        listOfChatHistory.addTask(newEvent);
    }

    private static void parseDeadline(String dataEntry, TaskList listOfChatHistory) {
        String description = dataEntry.split("~")[2];
        String by = dataEntry.split("~")[3];
        Task newDeadline = new Deadline(description, by);
        boolean isDone = Integer.parseInt(dataEntry.split("~")[1]) != 0;
        if(isDone){
            newDeadline.markAsDone();
        }
        listOfChatHistory.addTask(newDeadline);
    }

    private static void parseTodo(String dataEntry, TaskList listOfChatHistory) {
        String description = dataEntry.split("~")[2];
        Task newTodo = new Todo(description);
        boolean isDone = Integer.parseInt(dataEntry.split("~")[1]) != 0;
        if(isDone){
            newTodo.markAsDone();
        }
        listOfChatHistory.addTask(newTodo);
    }

}