package kojispawn;

import java.nio.file.Path;

/**
 * Coordinates input parsing, task management, and user interaction.
 */
public class KojisPawn {
    private final Parser parser;
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public KojisPawn() throws KojisPawnException {
        this.parser = new Parser();
        this.storage = new Storage(Path.of("data", "kojispawn.txt"));
        this.tasks = new TaskList(storage.load());
        this.ui = new Ui();
    }

    public static void main(String[] args) throws KojisPawnException {
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
            storage.save(tasks);
            ui.showTaskAdded(command.getTask(), tasks.size());
            break;
        case LIST:
            ui.showTaskList(tasks.getTasks());
            break;
        case MARK:
            Task markedTask = tasks.mark(command.getTaskNumber());
            storage.save(tasks);
            ui.showTaskMarked(markedTask);
            break;
        case UNMARK:
            Task unmarkedTask = tasks.unmark(command.getTaskNumber());
            storage.save(tasks);
            ui.showTaskUnmarked(unmarkedTask);
            break;
        case DELETE:
            Task deletedTask = tasks.delete(command.getTaskNumber());
            storage.save(tasks);
            ui.showTaskDeleted(deletedTask, tasks.size());
            break;
        case ON:
            ui.showTasksOnDate(tasks.findOn(command.getDate()), command.getDate());
            break;
        case BYE:
            return false;
        case UNKNOWN:
            throw new AssertionError("Parser returned an unknown command");
        }
        return true;
    }
}
