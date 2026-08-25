package kojispawn.command;

/**
 * Identifies the command that Koji's Pawn should perform.
 */
public enum CommandType {
    TODO,
    DEADLINE,
    EVENT,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    ON,
    BYE,
    UNKNOWN;

    /**
     * Determines the command type from the first word of normalized user input.
     *
     * @param command Normalized command entered by the user.
     * @return Matching command type, or {@link #UNKNOWN} if the word is not recognized.
     */
    public static CommandType from(String command) {
        int commandWordEnd = 0;
        while (commandWordEnd < command.length()
                && !Character.isWhitespace(command.charAt(commandWordEnd))) {
            commandWordEnd++;
        }
        String commandWord = command.substring(0, commandWordEnd);

        return switch (commandWord) {
        case "todo" -> TODO;
        case "deadline" -> DEADLINE;
        case "event" -> EVENT;
        case "list" -> LIST;
        case "mark" -> MARK;
        case "unmark" -> UNMARK;
        case "delete" -> DELETE;
        case "on" -> ON;
        case "bye" -> BYE;
        default -> UNKNOWN;
        };
    }
}
