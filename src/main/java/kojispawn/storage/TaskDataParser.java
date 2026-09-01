package kojispawn.storage;

import java.time.LocalDate;

import kojispawn.task.Deadline;
import kojispawn.task.Event;
import kojispawn.task.Task;
import kojispawn.task.Todo;

/**
 * Reconstructs task objects from their stored data representation.
 */
final class TaskDataParser {
    /**
     * Reconstructs one task from the format produced by {@link Task#toDataString()}.
     *
     * @param taskLine Stored representation of one task.
     * @return Reconstructed task with its saved completion state.
     */
    Task parse(String taskLine) {
        String[] fields = taskLine.split(" \\| ", -1);
        Task task = switch (fields[0]) {
            case "T" -> new Todo(fields[2]);
            case "D" -> new Deadline(fields[2], LocalDate.parse(fields[3]));
            case "E" -> new Event(fields[2], fields[3], fields[4]);
            default -> throw new IllegalArgumentException("Unknown stored task type: " + fields[0]);
        };

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
