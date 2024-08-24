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
        String[] listOfChatHistory = new String[100];
        int histoyIndex = 0;

        printDivider();
        System.out.println("Hello! I'm Ryze!");
        System.out.println("What can I do for you?\n");
        printDivider();
        System.out.println();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();

        while (!line.equals("bye")){
            switch (line) {
            case "list":
                printDivider();
                for (int i = 0; i < listOfChatHistory.length; i++) {
                    if (listOfChatHistory[i] == null) {
                        break;
                    }
                    System.out.printf("%d. %s\n", i + 1, listOfChatHistory[i]);
                }
                System.out.println();
                printDivider();
                break;
            default:
                echo("added: " + line);
                listOfChatHistory[histoyIndex] = line;
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
