package ryze;

import ryze.exceptions.InvalidNumberArguments;
import ryze.exceptions.RyzeException;
import ryze.parser.Parser;
import ryze.task.Deadline;
import ryze.task.Event;
import ryze.task.Task;
import ryze.task.Todo;
import ryze.task.TaskList;
import ryze.ui.Ui;
import ryze.storage.Storage;

import java.io.IOException;
import java.util.Scanner;

/**
 * Ryze is a simple command-line bot for managing tasks.
 */
public class Ryze {
    private static TaskList listOfChatHistory = new TaskList();
    private Ui ui;
    private Storage storage;
    private Parser parser = new Parser();


    public Ryze() {
        ui = new Ui();
        storage = new Storage("data", "ryze.txt");
        storage.initialiseData(listOfChatHistory);
    }

    public void run() {
        ui.initialiseMessage();
        processCommand();
        ui.exitMessage();
    }

    public static void main(String[] args) {
        new Ryze().run();
    }

    private void processCommand() {
        Scanner scanner = new Scanner(System.in);
        String line;
        boolean isDone = false;
        int lastSize = listOfChatHistory.size();
        while (!isDone) {
            try {
                line = scanner.nextLine();
                parser.processCommand(line, storage, ui, listOfChatHistory);
                isDone = parser.done();
            } catch (RyzeException e) {
                ui.handleRyzeException(e);
            }
            lastSize = storage.handleSizeChange(lastSize,listOfChatHistory);
        }
    }






}
