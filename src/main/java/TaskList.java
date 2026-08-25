import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns and manages the tasks in Koji's Pawn.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing tasks loaded from storage.
     *
     * @param tasks tasks to place in the list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns a read-only view for displaying the tasks.
     *
     * @return tasks in their current order
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public int size() {
        return tasks.size();
    }

    /**
     * Marks the selected task as complete.
     *
     * @param taskNumber one-based task number
     * @return task whose status changed
     * @throws KojisPawnException if the task number is outside the list
     */
    public Task mark(int taskNumber) throws KojisPawnException {
        Task task = getTask(taskNumber, "mark");
        task.markAsDone();
        return task;
    }

    /**
     * Marks the selected task as incomplete.
     *
     * @param taskNumber one-based task number
     * @return task whose status changed
     * @throws KojisPawnException if the task number is outside the list
     */
    public Task unmark(int taskNumber) throws KojisPawnException {
        Task task = getTask(taskNumber, "unmark");
        task.markAsNotDone();
        return task;
    }

    /**
     * Removes the selected task.
     *
     * @param taskNumber one-based task number
     * @return removed task
     * @throws KojisPawnException if the task number is outside the list
     */
    public Task delete(int taskNumber) throws KojisPawnException {
        validateTaskNumber(taskNumber, "delete");
        return tasks.remove(taskNumber - 1);
    }

    private Task getTask(int taskNumber, String action) throws KojisPawnException {
        validateTaskNumber(taskNumber, action);
        return tasks.get(taskNumber - 1);
    }

    private void validateTaskNumber(int taskNumber, String action) throws KojisPawnException {
        if (tasks.isEmpty()) {
            throw new KojisPawnException("There are no tasks to " + action + " yet.");
        }
        if (taskNumber > tasks.size()) {
            throw new KojisPawnException(
                    "No task occupies that position. Choose a number from 1 to " + tasks.size() + ".");
        }
    }
}
