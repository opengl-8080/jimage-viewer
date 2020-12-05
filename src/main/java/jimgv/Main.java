package jimgv;

import gl.javafx.FxWindow;
import gl.javafx.Fxml;
import javafx.application.Application;
import javafx.stage.Stage;
import jimgv.controller.MainController;

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
        primaryStage.setFullScreenExitHint("");

        final Fxml<?, ?> fxml = Fxml.load(MainController.class);
        FxWindow.newWindow(fxml)
                .title(TITLE_BASE)
                .show(primaryStage);

        Main.stage = primaryStage;
    }
}
