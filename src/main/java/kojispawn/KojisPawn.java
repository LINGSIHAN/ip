package kojispawn;

import java.nio.file.Path;

import kojispawn.command.Command;
import kojispawn.command.Parser;
import kojispawn.exception.KojisPawnException;
import kojispawn.storage.Storage;
import kojispawn.task.Task;
import kojispawn.task.TaskList;
import kojispawn.ui.Ui;

/**
 * Coordinates input parsing, task management, and user interaction.
 */
public class KojisPawn {
    private final Parser parser;
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates the chatbot and loads any tasks saved in the default data file.
     *
     * @throws KojisPawnException If the task data cannot be loaded.
     */
    public KojisPawn() throws KojisPawnException {
        this.parser = new Parser();
        this.storage = new Storage(Path.of("data", "kojispawn.txt"));
        this.tasks = new TaskList(storage.load());
        this.ui = new Ui();
    }

    /**
     * Starts Koji's Pawn.
     *
     * @param args Command-line arguments, which are not used.
     * @throws KojisPawnException If the task data cannot be loaded.
     */
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
     * @param command Command returned by the parser.
     * @return {@code false} when the chatbot should exit, otherwise {@code true}.
     * @throws KojisPawnException If a task number is outside the list.
     */
    private boolean execute(Command command) throws KojisPawnException {
        switch (command.getType()) {
            case TODO, DEADLINE, EVENT:
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
