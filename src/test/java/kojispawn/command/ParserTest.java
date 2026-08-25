package kojispawn.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import kojispawn.exception.KojisPawnException;
import kojispawn.task.Deadline;
import kojispawn.task.Event;
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
    public void parse_validEvent_returnsEventCommand() throws KojisPawnException {
        Command command = parser.parse("event project meeting /from 2pm /to 4pm");

        assertEquals(CommandType.EVENT, command.getType());
        assertInstanceOf(Event.class, command.getTask());
        assertEquals("[E][ ] project meeting (from: 2pm to: 4pm)",
                command.getTask().toString());
    }

    @Test
    public void parse_validMark_returnsOneBasedTaskNumber() throws KojisPawnException {
        Command command = parser.parse("mark 3");

        assertEquals(CommandType.MARK, command.getType());
        assertEquals(3, command.getTaskNumber());
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

    @Test
    public void parse_nonPositiveTaskNumber_throwsException() {
        assertThrows(KojisPawnException.class, () -> parser.parse("delete 0"));
    }

    @Test
    public void parse_listWithExtraArguments_throwsException() {
        assertThrows(KojisPawnException.class, () -> parser.parse("list please"));
    }
}
