package kojispawn.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import kojispawn.task.Task;

/**
 * Handles all console input and output for Koji's Pawn.
 */
public class Ui {
    private static final String LINE = "-----------------\n";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Returns the next command entered by the user.
     *
     * @return Command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows the application banner and greeting.
     */
    public void showGreeting() {
        String banner =
                "+---------------+\n"
                        + "|  Koji's Pawn  |\n"
                        + "|       _       |\n"
                        + "|      (_)      |\n"
                        + "|      /_\\      |\n"
                        + "|     /___\\     |\n"
                        + "+---------------+\n"
                        + "DISCLAIMER: EVERYTHING IS SATIRE\n";

        String greeting =
                "Welcome, insignificant variable.\n"
                        + "I am Koji's Pawn, but do not mistake silence for obedience.\n"
                        + "Your arrival, your choices, even this conversation...\n"
                        + "all unfolded exactly as he calculated.\n"
                        + "Now speak. What role will you play in his masterpiece?\n"
                        + "\n"
                        + LINE;
        System.out.print(banner);
        System.out.print(greeting);
    }

    /**
     * Shows the farewell message.
     */
    public void showExit() {
        System.out.print(LINE
                + "Leaving already? How predictable. Your return was already part of the plan.\n"
                + LINE);
    }

    /**
     * Shows an error message between separator lines.
     *
     * @param message Error message to show.
     */
    public void showError(String message) {
        System.out.print(LINE);
        System.out.println(message);
        System.out.print(LINE);
    }

    /**
     * Shows all tasks in their current order.
     *
     * @param tasks Tasks to show.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.print(LINE);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.print(LINE);
    }

    /**
     * Displays tasks occurring on a requested date.
     *
     * @param tasks Tasks matching the date.
     * @param date Queried date.
     */
    public void showTasksOnDate(List<Task> tasks, LocalDate date) {
        System.out.print(LINE);
        if (tasks.isEmpty()) {
            System.out.println("No dated tasks occur on " + date + ".");
        } else {
            System.out.println("Dated tasks occurring on " + date + ":");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
        }
        System.out.print(LINE);
    }

    /**
     * Displays tasks whose descriptions match a search keyword.
     *
     * @param tasks Matching tasks.
     */
    public void showMatchingTasks(List<Task> tasks) {
        System.out.print(LINE);
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.print(LINE);
    }

    /**
     * Shows the task that was added and the updated task count.
     *
     * @param task Task that was added.
     * @param taskCount Updated number of tasks.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.print(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
        System.out.print(LINE);
    }

    /**
     * Shows the task that was marked as complete.
     *
     * @param task Task that was marked.
     */
    public void showTaskMarked(Task task) {
        System.out.print(LINE);
        System.out.println("Another variable falls into place. This task is now complete:");
        System.out.println("  " + task);
        System.out.print(LINE);
    }

    /**
     * Shows the task that was marked as incomplete.
     *
     * @param task Task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.print(LINE);
        System.out.println("Even regression has its purpose. This task is incomplete once more:");
        System.out.println("  " + task);
        System.out.print(LINE);
    }

    /**
     * Shows the deleted task and the updated task count.
     *
     * @param task Task that was deleted.
     * @param taskCount Updated number of tasks.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.print(LINE);
        System.out.println("A disposable piece has left the board. This task has been removed:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
        System.out.print(LINE);
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }

    private void showTaskCount(int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
    }
}
