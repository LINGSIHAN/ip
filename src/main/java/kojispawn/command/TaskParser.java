package kojispawn.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import kojispawn.exception.KojisPawnException;
import kojispawn.task.Deadline;
import kojispawn.task.Event;
import kojispawn.task.Task;
import kojispawn.task.Todo;

/**
 * Converts task-creation command arguments into task objects.
 */
final class TaskParser {
    /**
     * Creates a todo from a todo command.
     *
     * @param command Complete todo command.
     * @return Todo described by the command.
     * @throws KojisPawnException If the description is missing.
     */
    Task parseTodo(String command) throws KojisPawnException {
        String description = command.substring("todo".length()).strip();
        if (description.isBlank()) {
            throw new KojisPawnException(
                    "An empty task has no place in the plan. Describe what must be done after todo.");
        }
        return new Todo(description);
    }

    /**
     * Creates a deadline from a deadline command.
     *
     * @param command Complete deadline command.
     * @return Deadline described by the command.
     * @throws KojisPawnException If the description or deadline date is invalid.
     */
    Task parseDeadline(String command) throws KojisPawnException {
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

        String dateBy = deadlineDetails.substring(byIndex + byMarker.length());
        if (dateBy.isBlank()) {
            throw new KojisPawnException("The plan requires a deadline value after /by.");
        }
        if (!Character.isWhitespace(dateBy.charAt(0))) {
            throw new KojisPawnException(
                    "Even a deadline needs a boundary. Use: deadline DESCRIPTION /by DATE.");
        }
        try {
            return new Deadline(description, LocalDate.parse(dateBy.strip()));
        } catch (DateTimeParseException exception) {
            throw new KojisPawnException(
                    "Deadline dates must use yyyy-MM-dd and describe a real calendar date.");
        }
    }

    /**
     * Creates an event from an event command.
     *
     * @param command Complete event command.
     * @return Event described by the command.
     * @throws KojisPawnException If the description, start, or end is invalid.
     */
    Task parseEvent(String command) throws KojisPawnException {
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

        String dateFrom = eventDetails.substring(fromIndex + fromMarker.length(), toIndex);
        if (dateFrom.isBlank()) {
            throw new KojisPawnException("The plan requires a starting value after /from.");
        }
        if (!Character.isWhitespace(dateFrom.charAt(0))) {
            throw new KojisPawnException("Every event has an origin. Include /from START.");
        }

        String dateTo = eventDetails.substring(toIndex + toMarker.length());
        if (dateTo.isBlank()) {
            throw new KojisPawnException("The plan requires an ending value after /to.");
        }
        if (!Character.isWhitespace(dateTo.charAt(0))) {
            throw new KojisPawnException("Even calculated events need an endpoint. Include /to END.");
        }
        return new Event(description, dateFrom.strip(), dateTo.strip());
    }
}
