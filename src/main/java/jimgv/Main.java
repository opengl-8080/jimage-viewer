package jimgv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
    private static final String TITLE_BASE = "JImage Viewer";
    private static Stage stage;

    public static Stage getStage() {
        return Main.stage;
    }

    public static void setTitle(String title) {
        Main.stage.setTitle(TITLE_BASE + " - " + title);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent fxml = FXMLLoader.load(Main.class.getResource("/fxml/main.fxml"));

        Scene scene = new Scene(fxml);
        primaryStage.setScene(scene);
        Main.stage = primaryStage;
        Main.stage.setTitle(TITLE_BASE);

        primaryStage.show();
    }
}
