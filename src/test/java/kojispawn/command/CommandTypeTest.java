package kojispawn.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests recognition of command words before detailed parsing begins.
 */
public class CommandTypeTest {
    @Test
    public void fromCommand_allSupportedCommandWords_returnsMatchingTypes() {
        assertEquals(CommandType.TODO, CommandType.fromCommand("todo read book"));
        assertEquals(CommandType.DEADLINE,
                CommandType.fromCommand("deadline return book /by 2019-12-02"));
        assertEquals(CommandType.EVENT,
                CommandType.fromCommand("event meeting /from 2pm /to 4pm"));
        assertEquals(CommandType.LIST, CommandType.fromCommand("list"));
        assertEquals(CommandType.MARK, CommandType.fromCommand("mark 1"));
        assertEquals(CommandType.UNMARK, CommandType.fromCommand("unmark 1"));
        assertEquals(CommandType.DELETE, CommandType.fromCommand("delete 1"));
        assertEquals(CommandType.ON, CommandType.fromCommand("on 2019-12-02"));
        assertEquals(CommandType.FIND, CommandType.fromCommand("find book"));
        assertEquals(CommandType.BYE, CommandType.fromCommand("bye"));
    }

    @Test
    public void fromCommand_unrecognizedOrPartialWord_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.fromCommand("todoodle"));
        assertEquals(CommandType.UNKNOWN, CommandType.fromCommand(""));
    }
}
