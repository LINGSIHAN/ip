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
            String command = scanner.nextLine().strip();
            CommandType commandType = CommandType.from(command);

            if (commandType == CommandType.BYE && command.equals("bye")) {
                break;
            }
            try {
                switch (commandType) {
                case LIST:
                    if (!command.equals("list")) {
                        throw createUnknownCommandException();
                    }
                    System.out.print(LINE);

                    for (int i = 0; i < toDoList.size(); i++) {
                        System.out.println((i + 1) + "." + toDoList.get(i));
                    }

                    System.out.print(LINE);
                    break;
                case MARK:
                    int taskIndex = getTaskIndex(command, "mark");
                    markTask(taskIndex);
                    break;
                case UNMARK:
                    int unmarkIndex = getTaskIndex(command, "unmark");
                    unmarkTask(unmarkIndex);
                    break;
                case DELETE:
                    int deleteIndex = getTaskIndex(command, "delete");
                    deleteTask(deleteIndex);
                    break;
                case TODO:
                    String description = command.substring("todo".length()).strip();
                    if (description.isBlank()) {
                        throw new KojisPawnException(
                                "An empty task has no place in the plan. Describe what must be done after todo.");
                    }
                    addTask(new Todo(description));
                    break;
                case DEADLINE:
                    addDeadline(command);
                    break;
                case EVENT:
                    addEvent(command);
                    break;
                case BYE:
                case UNKNOWN:
                    throw createUnknownCommandException();
                }
            } catch (KojisPawnException exception) {
                showError(exception.getMessage());
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
     * Displays a command error without ending the chatbot session.
     *
     * @param message explanation of the invalid command
     */
    private static void showError(String message) {
        System.out.print(LINE);
        System.out.println(message);
        System.out.print(LINE);
    }

    /**
     * Creates the standard exception used for unrecognized or malformed command words.
     *
     * @return exception containing guidance about the supported commands
     */
    private static KojisPawnException createUnknownCommandException() {
        return new KojisPawnException(
                "That command was never part of the plan. "
                        + "Try todo, deadline, event, list, mark, unmark, delete, or bye.");
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
     * Validates and adds a deadline command.
     *
     * @param command complete deadline command entered by the user
     * @throws KojisPawnException if the description, {@code /by} marker, or deadline value is missing
     */
    private static void addDeadline(String command) throws KojisPawnException {
        String deadlineDetails = command.substring("deadline".length()).strip();
        if (deadlineDetails.isBlank()) {
            throw new KojisPawnException(
                    "A deadline without a description is merely noise. Use: deadline DESCRIPTION /by DATE.");
        }
        if (deadlineDetails.startsWith("/by")) {
            throw new KojisPawnException(
                    "A deadline without a description is merely noise. Use: deadline DESCRIPTION /by DATE.");
        }

        String byMarker = " /by";
        int byIndex = deadlineDetails.indexOf(byMarker);
        if (byIndex == -1) {
            throw new KojisPawnException(
                    "Even a deadline needs a boundary. Use: deadline DESCRIPTION /by DATE.");
        }

        String description = deadlineDetails.substring(0, byIndex).strip();
        if (description.isBlank()) {
            throw new KojisPawnException(
                    "A deadline without a description is merely noise. Use: deadline DESCRIPTION /by DATE.");
        }

        int byStartIndex = byIndex + byMarker.length();
        String by = deadlineDetails.substring(byStartIndex);
        if (by.isBlank()) {
            throw new KojisPawnException("The plan requires a deadline value after /by.");
        }
        if (!Character.isWhitespace(by.charAt(0))) {
            throw new KojisPawnException(
                    "Even a deadline needs a boundary. Use: deadline DESCRIPTION /by DATE.");
        }

        addTask(new Deadline(description, by.strip()));
    }

    /**
     * Validates and adds an event command.
     *
     * @param command complete event command entered by the user
     * @throws KojisPawnException if any event detail is missing or its markers are out of order
     */
    private static void addEvent(String command) throws KojisPawnException {
        String eventDetails = command.substring("event".length()).strip();
        if (eventDetails.isBlank()
                || eventDetails.startsWith("/from")
                || eventDetails.startsWith("/to")) {
            throw new KojisPawnException(
                    "An event without a description cannot enter the plan. "
                            + "Use: event DESCRIPTION /from START /to END.");
        }

        String fromMarker = " /from";
        String toMarker = " /to";
        int fromIndex = eventDetails.indexOf(fromMarker);
        int toIndex = eventDetails.indexOf(toMarker);
        if (fromIndex == -1) {
            throw new KojisPawnException("Every event has an origin. Include /from START.");
        }
        if (toIndex == -1) {
            throw new KojisPawnException("Even calculated events need an endpoint. Include /to END.");
        }
        if (toIndex < fromIndex) {
            throw new KojisPawnException(
                    "Causality matters. Place /from START before /to END.");
        }

        String description = eventDetails.substring(0, fromIndex).strip();
        if (description.isBlank()) {
            throw new KojisPawnException(
                    "An event without a description cannot enter the plan. "
                            + "Use: event DESCRIPTION /from START /to END.");
        }

        int fromStartIndex = fromIndex + fromMarker.length();
        String from = eventDetails.substring(fromStartIndex, toIndex);
        if (from.isBlank()) {
            throw new KojisPawnException("The plan requires a starting value after /from.");
        }
        if (!Character.isWhitespace(from.charAt(0))) {
            throw new KojisPawnException(
                    "Every event has an origin. Include /from START.");
        }

        int toStartIndex = toIndex + toMarker.length();
        String to = eventDetails.substring(toStartIndex);
        if (to.isBlank()) {
            throw new KojisPawnException("The plan requires an ending value after /to.");
        }
        if (!Character.isWhitespace(to.charAt(0))) {
            throw new KojisPawnException(
                    "Even calculated events need an endpoint. Include /to END.");
        }

        addTask(new Event(description, from.strip(), to.strip()));
    }

    /**
     * Extracts and validates the one-based task number in a task-index command.
     *
     * @param command complete command entered by the user
     * @param action command word, such as {@code mark}, {@code unmark}, or {@code delete}
     * @return validated zero-based task index
     * @throws KojisPawnException if the task number is missing, malformed, or outside the list
     */
    private static int getTaskIndex(String command, String action) throws KojisPawnException {
        String taskNumberText = command.substring(action.length()).strip();
        if (taskNumberText.isBlank()) {
            throw new KojisPawnException(
                    "Specify which task to " + action + ". Use: " + action + " TASK_NUMBER.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new KojisPawnException(
                    "Task positions are numbers, not guesses. Use: " + action + " TASK_NUMBER.");
        }

        if (taskNumber <= 0) {
            throw new KojisPawnException("The list begins at 1. Choose a positive task number.");
        }
        if (toDoList.isEmpty()) {
            throw new KojisPawnException("There are no tasks to " + action + " yet.");
        }
        if (taskNumber > toDoList.size()) {
            throw new KojisPawnException(
                    "No task occupies that position. Choose a number from 1 to " + toDoList.size() + ".");
        }

        return taskNumber - 1;
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

    /**
     * Removes the selected task and reports the updated task count.
     *
     * @param index zero-based index of the task to delete
     */
    private static void deleteTask(int index) {
        Task removedTask = toDoList.remove(index);
        System.out.print(LINE);
        System.out.println("A disposable piece has left the board. This task has been removed:");
        System.out.println("  " + removedTask);
        int taskCount = toDoList.size();
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
        System.out.print(LINE);
    }
}
