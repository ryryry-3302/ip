import java.util.Scanner;
public class Ryze {
    public static void printDivider(){
        System.out.println("➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤➤");
    }

    public static void echo(String userInput){
        System.out.println();
        printDivider();
        System.out.println(userInput);
        System.out.println();
        printDivider();
        System.out.println();
    }

    public static void main(String[] args) {
        Task[] listOfChatHistory = new Task[100];
        int histoyIndex = 0;

        printDivider();
        System.out.println("Hello! I'm Ryze!");
        System.out.println("What can I do for you?\n");
        printDivider();
        System.out.println();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();

        while (!line.equals("bye")){
            String command = line.split(" ")[0];
            int taskNumber = -1;
            switch (command) {
            case "list":
                printDivider();
                for (int i = 0; i < listOfChatHistory.length; i++) {
                    if (listOfChatHistory[i] == null) {
                        break;
                    }
                    System.out.printf("%d.[%s] %s\n", i + 1, listOfChatHistory[i].getStatusIcon(), listOfChatHistory[i].getDescription());
                }
                System.out.println();
                printDivider();
                break;
            case "mark":
            case "unmark":
                try {
                    taskNumber = Integer.parseInt(line.split(" ")[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid task number format.");
                    break;
                }

                if (taskNumber > 0 && taskNumber <= histoyIndex) {
                    if (command.equals("mark")) {
                        listOfChatHistory[taskNumber - 1].markAsDone();
                        printDivider();
                        System.out.println("Nice! I've marked this task as done:");
                    } else {
                        listOfChatHistory[taskNumber - 1].markAsNotDone();
                        printDivider();
                        System.out.println("Okay! I've marked this task as not done:");
                    }
                    System.out.printf("  [%s] %s\n\n", listOfChatHistory[taskNumber - 1].getStatusIcon(), listOfChatHistory[taskNumber - 1].getDescription());
                    printDivider();
                } else {
                    printDivider();
                    System.out.println("Task number is out of range.\n");
                    printDivider();
                }
                break;
            default:
                echo("added: " + line);
                listOfChatHistory[histoyIndex] = new Task(line);
                histoyIndex++;
                break;
            }



            line = in.nextLine();
        }


        System.out.println();
        printDivider();
        System.out.println("Bye. Hope to see you again soon!\n");
        printDivider();
    }
}
