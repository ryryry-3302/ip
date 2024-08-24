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
        printDivider();
        System.out.println("Hello! I'm Ryze!");
        System.out.println("What can I do for you?\n");
        printDivider();
        System.out.println();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();

        while (!line.equals("bye")){
            echo(line);

            line = in.nextLine();
        }


        System.out.println();
        printDivider();
        System.out.println("Bye. Hope to see you again soon!\n");
        printDivider();
    }
}
