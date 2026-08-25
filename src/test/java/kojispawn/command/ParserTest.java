package kojispawn.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import kojispawn.exception.KojisPawnException;
import kojispawn.task.Deadline;
import kojispawn.task.Todo;

/**
 * Tests the conversion of user input into validated commands.
 */
public class ParserTest {
    private final Parser parser = new Parser();

    @Test
    public void parse_validTodo_returnsTodoCommand() throws KojisPawnException {
        Command command = parser.parse("todo read book");

        assertEquals(CommandType.TODO, command.getType());
        assertInstanceOf(Todo.class, command.getTask());
        assertEquals("[T][ ] read book", command.getTask().toString());
    }

    @Test
    public void parse_validDeadline_returnsDeadlineWithFormattedDate()
            throws KojisPawnException {
        Command command = parser.parse("deadline return book /by 2019-12-02");

        assertEquals(CommandType.DEADLINE, command.getType());
        assertInstanceOf(Deadline.class, command.getTask());
        assertEquals("[D][ ] return book (by: Dec 2 2019)", command.getTask().toString());
    }

    @Test
    public void parse_validOnCommand_returnsQueryDate() throws KojisPawnException {
        Command command = parser.parse("on 2019-12-02");

        assertEquals(CommandType.ON, command.getType());
        assertEquals(LocalDate.of(2019, 12, 2), command.getDate());
    }

    @Test
    public void parse_impossibleDeadlineDate_throwsException() {
        KojisPawnException exception = assertThrows(KojisPawnException.class,
                () -> parser.parse("deadline return book /by 2019-02-30"));

        assertEquals("Deadline dates must use yyyy-MM-dd and describe a real calendar date.",
                exception.getMessage());
    }

    @Test
    public void parse_unknownCommand_throwsException() {
        assertThrows(KojisPawnException.class, () -> parser.parse("dance now"));
    }
}
