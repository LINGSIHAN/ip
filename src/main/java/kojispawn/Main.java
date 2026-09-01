package kojispawn;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kojispawn.exception.KojisPawnException;
import kojispawn.ui.MainWindow;

/**
 * Displays the JavaFX interface for Koji's Pawn.
 */
public class Main extends Application {
    private final KojisPawn koji;

    /**
     * Creates the JavaFX application and its Koji's Pawn response generator.
     */
    public Main() {
        try {
            koji = new KojisPawn();
        } catch (KojisPawnException exception) {
            throw new IllegalStateException("Unable to initialize Koji's Pawn.", exception);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane mainLayout = fxmlLoader.load();
        Scene scene = new Scene(mainLayout);

        stage.setTitle("Koji's Pawn");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/DaDuke.png")));
        stage.setMinHeight(220.0);
        stage.setMinWidth(417.0);
        stage.setScene(scene);
        fxmlLoader.<MainWindow>getController().setKoji(koji);
        stage.show();
    }
}
