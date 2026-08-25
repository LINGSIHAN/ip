import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves tasks to a file so they can be restored in a later increment.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage that writes to the given file.
     *
     * @param filePath relative path of the task data file
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file, preserving their types and completion states.
     *
     * @return tasks stored in the file, or an empty list when the file does not exist
     * @throws KojisPawnException if the file cannot be read
     */
    public List<Task> load() throws KojisPawnException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<Task> tasks = new ArrayList<>();
            for (String taskLine : Files.readAllLines(filePath)) {
                if (!taskLine.isBlank()) {
                    tasks.add(parseTask(taskLine));
                }
            }
            return tasks;
        } catch (IOException exception) {
            throw new KojisPawnException("I could not load the task list from " + filePath + ".");
        }
    }

    /**
     * Writes the complete task list to disk, replacing the previous contents.
     *
     * @param tasks tasks to save
     * @throws KojisPawnException if the directory or file cannot be written
     */
    public void save(TaskList tasks) throws KojisPawnException {
        try {
            Path parentDirectory = filePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            List<String> taskLines = tasks.getTasks().stream()
                    .map(Task::toDataString)
                    .toList();
            Files.write(filePath, taskLines);
        } catch (IOException exception) {
            throw new KojisPawnException("I could not save the task list to " + filePath + ".");
        }
    }

    /**
     * Reconstructs one task from the format produced by {@link Task#toDataString()}.
     */
    private Task parseTask(String taskLine) {
        String[] fields = taskLine.split(" \\| ", -1);
        Task task = switch (fields[0]) {
        case "T" -> new Todo(fields[2]);
        case "D" -> new Deadline(fields[2], fields[3]);
        case "E" -> new Event(fields[2], fields[3], fields[4]);
        default -> throw new IllegalArgumentException("Unknown stored task type: " + fields[0]);
        };

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
