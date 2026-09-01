package kojispawn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kojispawn.command.CommandType;

/**
 * Tests command responses used by both the console and JavaFX interfaces.
 */
public class KojisPawnTest {
    @TempDir
    private Path tempDirectory;

    @Test
    public void getResponse_taskLifecycle_returnsResponsesAndPersistsChanges() throws Exception {
        Path dataFile = tempDirectory.resolve("data/kojispawn.txt");
        KojisPawn koji = new KojisPawn(dataFile);

        assertEquals(
                "Got it. I've added this task:\n"
                        + "  [T][ ] control the board\n"
                        + "Now you have 1 task in the list.",
                koji.getResponse("todo control the board"));
        assertEquals(CommandType.TODO, koji.getLastCommandType());
        assertEquals("1.[T][ ] control the board", koji.getResponse("list"));
        assertEquals(
                "Another variable falls into place. This task is now complete:\n"
                        + "  [T][X] control the board",
                koji.getResponse("mark 1"));
        assertEquals(CommandType.MARK, koji.getLastCommandType());
        assertEquals(List.of("T | 1 | control the board"), Files.readAllLines(dataFile));
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorAndUnknownType() throws Exception {
        KojisPawn koji = new KojisPawn(tempDirectory.resolve("kojispawn.txt"));

        String response = koji.getResponse("move knight");

        assertEquals(
                "That command was never part of the plan. "
                        + "Try todo, deadline, event, list, mark, unmark, delete, on, find, or bye.",
                response);
        assertEquals(CommandType.UNKNOWN, koji.getLastCommandType());
        assertFalse(koji.isExitRequested());
    }

    @Test
    public void getResponse_bye_returnsFarewellAndRequestsExit() throws Exception {
        KojisPawn koji = new KojisPawn(tempDirectory.resolve("kojispawn.txt"));

        String response = koji.getResponse("bye");

        assertEquals(
                "Leaving already? How predictable. Your return was already part of the plan.",
                response);
        assertEquals(CommandType.BYE, koji.getLastCommandType());
        assertTrue(koji.isExitRequested());
    }
}
