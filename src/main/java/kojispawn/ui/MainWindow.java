package kojispawn.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import kojispawn.KojisPawn;
import kojispawn.command.CommandType;

/**
 * Controls the main Koji's Pawn chat window defined in FXML.
 */
public class MainWindow extends AnchorPane {
    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image kojiImage = new Image(getClass().getResourceAsStream("/images/DaDuke.png"));

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private KojisPawn koji;

    /**
     * Configures the dialog area to follow the latest message.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the chatbot used to generate responses.
     *
     * @param koji Koji's Pawn chatbot instance.
     */
    public void setKoji(KojisPawn koji) {
        this.koji = koji;
    }

    /**
     * Adds the user's message and Koji's echo response, then clears the text field.
     */
    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        String kojiText = koji.getResponse(userText);
        CommandType commandType = koji.getLastCommandType();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getKojiDialog(kojiText, kojiImage, commandType));
        userInput.clear();

        if (koji.isExitRequested()) {
            PauseTransition exitDelay = new PauseTransition(Duration.seconds(1.0));
            exitDelay.setOnFinished(event -> Platform.exit());
            exitDelay.play();
        }
    }
}
