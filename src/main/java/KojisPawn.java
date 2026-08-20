import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the chatbot and handles adding, listing, and marking tasks.
 */
public class KojisPawn {
    private static ArrayList<String> toDoList = new ArrayList<>();
    private static ArrayList<Boolean> taskStatuses = new ArrayList<>();
    private static final String LINE = "-----------------\n";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        greetingMessage();

        while (true) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                break;
            }
            if (command.equals("list")) {
                System.out.print(LINE);

                for (int i = 0; i < toDoList.size(); i++) {
                    String statusIcon = taskStatuses.get(i) ? "X" : " ";
                    System.out.println((i + 1) + ".[" + statusIcon + "] " + toDoList.get(i));
                }

                System.out.print(LINE);
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                markTask(taskIndex);
            } else {
                addTask(command);
                System.out.print(LINE);
                System.out.println("added: " + command);
                System.out.print(LINE);
            }
        }

        exitMessage();
        scanner.close();
    }

    private static void greetingMessage() {
        String banner =
                "+---------------+\n"
                        + "|  Koji's Pawn  |\n"
                        + "|       _       |\n"
                        + "|      (_)      |\n"
                        + "|      /_\\      |\n"
                        + "|     /___\\     |\n"
                        + "+---------------+\n"
                        + "DISCLAIMER: EVERYTHING IS SATIRE\n";

        String greet =
                "Welcome, insignificant variable.\n"
                        + "I am Koji's Pawn, but do not mistake silence for obedience.\n"
                        + "Your arrival, your choices, even this conversation...\n"
                        + "all unfolded exactly as he calculated.\n"
                        + "Now speak. What role will you play in his masterpiece?\n"
                        + "\n"
                        + LINE;
        System.out.print(banner);
        System.out.print(greet);
    }

    private static void exitMessage() {
        String exit = LINE
                + "Leaving already? How predictable. Your return was already part of the plan.\n"
                + LINE;
        System.out.print(exit);
    }

    private static void addTask(String string) {
        toDoList.add(string);
        taskStatuses.add(false);
    }

    private static void markTask(int index) {
        taskStatuses.set(index, true);
        System.out.print(LINE);
        System.out.println("Another variable falls into place. This task is now complete:");
        System.out.println("  [X] " + toDoList.get(index));
        System.out.print(LINE);
    }
}
