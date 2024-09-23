package ryze.ui;

import ryze.exceptions.RyzeException;

/**
 * The {@code Ui} class handles user interaction messages for the Ryze application.
 * It provides static methods to display initialization and exit messages,
 * as well as dividers for better visual separation.
 */
public class Ui {

    /**
     * Private constructor to prevent instantiation of the {@code Ui} class.
     * Since all methods are static, no instance of this class is needed.
     */
    public Ui() {
    }

    /** Divider string used for separating UI messages visually. */
    private final String DIVIDER = "➤".repeat(40);

    /**
     * Prints the divider to the console.
     * The divider is a sequence of '➤' characters repeated for visual separation.
     */
    public void printDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays an exit message to the user, indicating the application is closing.
     * The message is wrapped between two dividers for visual separation.
     */
    public void exitMessage() {
        System.out.println();
        printDivider();
        System.out.println("Bye. Hope to see you again soon!\n");
        printDivider();
    }

    /**
     * Displays an initialization message to the user.
     * This includes a greeting and an ASCII logo of Ryze, followed by a prompt asking the user what they need.
     * The message is wrapped between two dividers for visual separation.
     */
    public void initialiseMessage() {
        printDivider();
        System.out.println("Hello! I'm Ryze!");
        System.out.println();
        System.out.println(" ____                _  ");
        System.out.println("|  _ \\ _   _ _______| | ");
        System.out.println("| |_) | | | |_  / _ \\ | ");
        System.out.println("|  _ <| |_| |/ /  __/_| ");
        System.out.println("|_| \\_\\\\__, /___\\___(_) ");
        System.out.println("       |___/            ");
        System.out.println();
        System.out.println("What can I do for you?\n");
        printDivider();
        System.out.println();
    }
    /**
     * Displays an error message to the user based on the RyzeException
     * @param e The RyzeException that is thrown
     */
    public void handleRyzeException(RyzeException e) {
        printDivider();
        System.out.println(e.getMessage());
        System.out.println();
        printDivider();
    }


    /**
     * Gives feedback to user after a task is added
     */
    public void echo(String taskString, int count) {
        System.out.println();
        printDivider();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + taskString);
        System.out.println("Now you have " + count + (count == 1 ? " task" : " tasks") + " in the list.");
        System.out.println();
        printDivider();
        System.out.println();
    }
}
