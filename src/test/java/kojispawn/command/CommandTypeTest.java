package kojispawn.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests recognition of command words before detailed parsing begins.
 */
public class CommandTypeTest {
    @Test
    public void from_allSupportedCommandWords_returnsMatchingTypes() {
        assertEquals(CommandType.TODO, CommandType.from("todo read book"));
        assertEquals(CommandType.DEADLINE,
                CommandType.from("deadline return book /by 2019-12-02"));
        assertEquals(CommandType.EVENT,
                CommandType.from("event meeting /from 2pm /to 4pm"));
        assertEquals(CommandType.LIST, CommandType.from("list"));
        assertEquals(CommandType.MARK, CommandType.from("mark 1"));
        assertEquals(CommandType.UNMARK, CommandType.from("unmark 1"));
        assertEquals(CommandType.DELETE, CommandType.from("delete 1"));
        assertEquals(CommandType.ON, CommandType.from("on 2019-12-02"));
        assertEquals(CommandType.FIND, CommandType.from("find book"));
        assertEquals(CommandType.BYE, CommandType.from("bye"));
    }

    @Test
    public void from_unrecognizedOrPartialWord_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from("todoodle"));
        assertEquals(CommandType.UNKNOWN, CommandType.from(""));
    }
}
