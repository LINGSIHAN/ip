package kojispawn.command;

import java.time.LocalDate;

import kojispawn.task.Task;

/**
 * Represents a validated instruction that Koji's Pawn can execute.
 */
public class Command {
    private final CommandType type;
    private final Task task;
    private final Integer taskNumber;
    private final LocalDate date;

    /**
     * Creates a command with any task data required for its execution.
     *
     * @param type Type of command.
     * @param task Task to add, or {@code null} when the command does not add one.
     * @param taskNumber One-based task number, or {@code null} when none is required.
     */
    public Command(CommandType type, Task task, Integer taskNumber) {
        this(type, task, taskNumber, null);
    }

    /**
     * Creates a command that can also carry a date for date-based queries.
     *
     * @param type Type of command.
     * @param task Task to add, or {@code null} when the command does not add one.
     * @param taskNumber One-based task number, or {@code null} when none is required.
     * @param date Date to query, or {@code null} when none is required.
     */
    public Command(CommandType type, Task task, Integer taskNumber, LocalDate date) {
        this.type = type;
        this.task = task;
        this.taskNumber = taskNumber;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }
}
