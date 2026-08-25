/**
 * Represents a validated instruction that Koji's Pawn can execute.
 */
public class Command {
    private final CommandType type;
    private final Task task;
    private final Integer taskNumber;

    /**
     * Creates a command with any task data required for its execution.
     *
     * @param type type of command
     * @param task task to add, or {@code null} when the command does not add one
     * @param taskNumber one-based task number, or {@code null} when none is required
     */
    public Command(CommandType type, Task task, Integer taskNumber) {
        this.type = type;
        this.task = task;
        this.taskNumber = taskNumber;
    }

    public CommandType getType() {
        return type;
    }

    public Task getTask() {
        return task;
    }

    public Integer getTaskNumber() {
        return taskNumber;
    }
}
