package kojispawn.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kojispawn.exception.KojisPawnException;
import kojispawn.task.Deadline;
import kojispawn.task.Event;
import kojispawn.task.Task;
import kojispawn.task.TaskList;
import kojispawn.task.Todo;

/**
 * Tests saving and loading tasks without touching the application's real data file.
 */
public class StorageTest {
    @TempDir
    private Path tempDirectory;

    @Test
    public void load_missingFile_returnsEmptyList() throws KojisPawnException {
        Storage storage = new Storage(tempDirectory.resolve("data/kojispawn.txt"));

        assertTrue(storage.load().isEmpty());
    }

    @Test
    public void saveThenLoad_mixedTasks_preservesTaskData() throws Exception {
        Path dataFile = tempDirectory.resolve("data/kojispawn.txt");
        Storage storage = new Storage(dataFile);
        TaskList originalTasks = new TaskList();
        originalTasks.add(new Todo("borrow book"));
        originalTasks.add(new Deadline("return book", LocalDate.of(2019, 12, 2)));
        originalTasks.add(new Event("project meeting", "Monday 2pm", "4pm"));
        originalTasks.mark(2);

        storage.save(originalTasks);
        List<Task> loadedTasks = storage.load();

        assertEquals(List.of(
                "T | 0 | borrow book",
                "D | 1 | return book | 2019-12-02",
                "E | 0 | project meeting | Monday 2pm | 4pm"),
                Files.readAllLines(dataFile));
        assertEquals(List.of(
                "[T][ ] borrow book",
                "[D][X] return book (by: Dec 2 2019)",
                "[E][ ] project meeting (from: Monday 2pm to: 4pm)"),
                loadedTasks.stream().map(Task::toString).toList());
    }

    @Test
    public void save_existingFile_replacesPreviousContents() throws Exception {
        Path dataFile = tempDirectory.resolve("kojispawn.txt");
        Storage storage = new Storage(dataFile);
        TaskList initialTasks = new TaskList(List.of(
                new Todo("first task"), new Todo("stale task")));
        storage.save(initialTasks);

        storage.save(new TaskList(List.of(new Todo("replacement task"))));

        assertEquals(List.of("T | 0 | replacement task"), Files.readAllLines(dataFile));
    }
}
