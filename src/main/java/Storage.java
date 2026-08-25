import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
}
