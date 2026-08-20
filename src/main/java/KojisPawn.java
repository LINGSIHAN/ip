import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the chatbot and manages todo, deadline, and event tasks.
 */
public class KojisPawn {
    private static ArrayList<Task> toDoList = new ArrayList<>();
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
                    System.out.println((i + 1) + "." + toDoList.get(i));
                }

                System.out.print(LINE);
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                markTask(taskIndex);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                unmarkTask(taskIndex);
            } else if (command.startsWith("todo ")) {
                String description = command.substring(5);
                addTask(new Todo(description));
            } else if (command.startsWith("deadline ")) {
                String deadlineDetails = command.substring(9);
                int byIndex = deadlineDetails.indexOf(" /by ");
                String description = deadlineDetails.substring(0, byIndex);
                String by = deadlineDetails.substring(byIndex + 5);
                addTask(new Deadline(description, by));
            } else if (command.startsWith("event ")) {
                String eventDetails = command.substring(6);
                int fromIndex = eventDetails.indexOf(" /from ");
                int toIndex = eventDetails.indexOf(" /to ", fromIndex + 7);
                String description = eventDetails.substring(0, fromIndex);
                String from = eventDetails.substring(fromIndex + 7, toIndex);
                String to = eventDetails.substring(toIndex + 5);
                addTask(new Event(description, from, to));
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

    /**
     * Adds a new incomplete task to the task list.
     *
     * @param task task to add
     */
    private static void addTask(Task task) {
        toDoList.add(task);
        System.out.print(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        int taskCount = toDoList.size();
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
        System.out.print(LINE);
    }

    /**
     * Marks the selected task as complete
     *
     * @param index zero-based index of the task to mark
     */
    private static void markTask(int index) {
        Task task = toDoList.get(index);
        task.markAsDone();
        System.out.print(LINE);
        System.out.println("Another variable falls into place. This task is now complete:");
        System.out.println("  " + task);
        System.out.print(LINE);
    }

    /**
     * Marks the selected task as incomplete
     *
     * @param index zero-based index of the task to unmark
     */
    private static void unmarkTask(int index) {
        Task task = toDoList.get(index);
        task.markAsNotDone();
        System.out.print(LINE);
        System.out.println("Even regression has its purpose. This task is incomplete once more:");
        System.out.println("  " + task);
        System.out.print(LINE);
    }
}
