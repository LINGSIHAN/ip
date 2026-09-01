package kojispawn.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import kojispawn.exception.KojisPawnException;
import kojispawn.task.Task;
import kojispawn.task.TaskList;

/**
 * Saves tasks to and loads tasks from a data file.
 */
public class Storage {
    private final Path filePath;
    private final TaskDataParser taskDataParser = new TaskDataParser();

    /**
     * Creates storage that writes to the given file.
     *
     * @param filePath Relative path of the task data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file, preserving their types and completion states.
     *
     * @return Tasks stored in the file, or an empty list when the file does not exist.
     * @throws KojisPawnException If the file cannot be read.
     */
    public List<Task> load() throws KojisPawnException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<Task> tasks = new ArrayList<>();
            for (String taskLine : Files.readAllLines(filePath)) {
                if (!taskLine.isBlank()) {
                    tasks.add(taskDataParser.parse(taskLine));
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
     * @param tasks Tasks to save.
     * @throws KojisPawnException If the directory or file cannot be written.
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

}
