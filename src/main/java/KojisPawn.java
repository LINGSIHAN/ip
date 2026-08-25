/**
 * Coordinates input parsing, task management, and user interaction.
 */
public class KojisPawn {
    private final Parser parser;
    private final TaskList tasks;
    private final Ui ui;

    public KojisPawn() {
        this.parser = new Parser();
        this.tasks = new TaskList();
        this.ui = new Ui();
    }

    public static void main(String[] args) {
        new KojisPawn().run();
    }

    /**
     * Runs the chatbot until the user enters the {@code bye} command.
     */
    public void run() {
        ui.showGreeting();
        boolean isRunning = true;

        while (isRunning) {
            try {
                Command command = parser.parse(ui.readCommand());
                isRunning = execute(command);
            } catch (KojisPawnException exception) {
                ui.showError(exception.getMessage());
            }
        }

        ui.showExit();
        ui.close();
    }

    /**
     * Executes a validated command.
     *
     * @param command command returned by the parser
     * @return {@code false} when the chatbot should exit, otherwise {@code true}
     * @throws KojisPawnException if a task number is outside the list
     */
    private boolean execute(Command command) throws KojisPawnException {
        switch (command.getType()) {
        case TODO:
        case DEADLINE:
        case EVENT:
            tasks.add(command.getTask());
            ui.showTaskAdded(command.getTask(), tasks.size());
            break;
        case LIST:
            ui.showTaskList(tasks.getTasks());
            break;
        case MARK:
            ui.showTaskMarked(tasks.mark(command.getTaskNumber()));
            break;
        case UNMARK:
            ui.showTaskUnmarked(tasks.unmark(command.getTaskNumber()));
            break;
        case DELETE:
            ui.showTaskDeleted(tasks.delete(command.getTaskNumber()), tasks.size());
            break;
        case BYE:
            return false;
        case UNKNOWN:
            throw new AssertionError("Parser returned an unknown command");
        }
        return true;
    }
}
