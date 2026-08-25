package kojispawn;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Converts raw user input into validated commands.
 */
public class Parser {
    /**
     * Parses one line of user input.
     *
     * @param input command entered by the user
     * @return validated command ready for execution
     * @throws KojisPawnException if the command or its arguments are invalid
     */
    public Command parse(String input) throws KojisPawnException {
        String command = input.strip();
        CommandType type = CommandType.from(command);

        return switch (type) {
        case TODO -> new Command(type, parseTodo(command), null);
        case DEADLINE -> new Command(type, parseDeadline(command), null);
        case EVENT -> new Command(type, parseEvent(command), null);
        case MARK -> new Command(type, null, parseTaskNumber(command, "mark"));
        case UNMARK -> new Command(type, null, parseTaskNumber(command, "unmark"));
        case DELETE -> new Command(type, null, parseTaskNumber(command, "delete"));
        case ON -> parseOn(command, type);
        case LIST, BYE -> parseExactCommand(command, type);
        case UNKNOWN -> throw createUnknownCommandException();
        };
    }

    private Task parseTodo(String command) throws KojisPawnException {
        String description = command.substring("todo".length()).strip();
        if (description.isBlank()) {
            throw new KojisPawnException(
                    "An empty task has no place in the plan. Describe what must be done after todo.");
        }
        return new Todo(description);
    }

    private Task parseDeadline(String command) throws KojisPawnException {
        String deadlineDetails = command.substring("deadline".length()).strip();
        if (deadlineDetails.isBlank() || deadlineDetails.startsWith("/by")) {
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

        String by = deadlineDetails.substring(byIndex + byMarker.length());
        if (by.isBlank()) {
            throw new KojisPawnException("The plan requires a deadline value after /by.");
        }
        if (!Character.isWhitespace(by.charAt(0))) {
            throw new KojisPawnException(
                    "Even a deadline needs a boundary. Use: deadline DESCRIPTION /by DATE.");
        }
        try {
            return new Deadline(description, LocalDate.parse(by.strip()));
        } catch (DateTimeParseException exception) {
            throw new KojisPawnException(
                    "Deadline dates must use yyyy-MM-dd and describe a real calendar date.");
        }
    }

    private Task parseEvent(String command) throws KojisPawnException {
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
            throw new KojisPawnException("Causality matters. Place /from START before /to END.");
        }

        String description = eventDetails.substring(0, fromIndex).strip();
        if (description.isBlank()) {
            throw new KojisPawnException(
                    "An event without a description cannot enter the plan. "
                            + "Use: event DESCRIPTION /from START /to END.");
        }

        String from = eventDetails.substring(fromIndex + fromMarker.length(), toIndex);
        if (from.isBlank()) {
            throw new KojisPawnException("The plan requires a starting value after /from.");
        }
        if (!Character.isWhitespace(from.charAt(0))) {
            throw new KojisPawnException("Every event has an origin. Include /from START.");
        }

        String to = eventDetails.substring(toIndex + toMarker.length());
        if (to.isBlank()) {
            throw new KojisPawnException("The plan requires an ending value after /to.");
        }
        if (!Character.isWhitespace(to.charAt(0))) {
            throw new KojisPawnException("Even calculated events need an endpoint. Include /to END.");
        }
        return new Event(description, from.strip(), to.strip());
    }

    private int parseTaskNumber(String command, String action) throws KojisPawnException {
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
        return taskNumber;
    }

    private Command parseOn(String command, CommandType type) throws KojisPawnException {
        String dateText = command.substring("on".length()).strip();
        if (dateText.isBlank()) {
            throw new KojisPawnException("Specify a date. Use: on yyyy-MM-dd.");
        }

        try {
            return new Command(type, null, null, LocalDate.parse(dateText));
        } catch (DateTimeParseException exception) {
            throw new KojisPawnException(
                    "Query dates must use yyyy-MM-dd and describe a real calendar date.");
        }
    }

    private Command parseExactCommand(String command, CommandType type) throws KojisPawnException {
        if (!command.equals(type.name().toLowerCase())) {
            throw createUnknownCommandException();
        }
        return new Command(type, null, null);
    }

    private KojisPawnException createUnknownCommandException() {
        return new KojisPawnException(
                "That command was never part of the plan. "
                        + "Try todo, deadline, event, list, mark, unmark, delete, on, or bye.");
    }
}
