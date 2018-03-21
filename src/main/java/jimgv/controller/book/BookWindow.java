package jimgv.controller.book;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jimgv.controller.single.SingleImageWindow;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class BookWindow {
    private static Set<Stage> openedStages = new HashSet<>();
    
    public static void open(Path bookDirectory) {
        try {
            FXMLLoader loader = new FXMLLoader(SingleImageWindow.class.getResource("/fxml/book.fxml"));

            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle(bookDirectory.getFileName().toString());
            stage.setFullScreenExitHint("");

            BookController controller = loader.getController();
            controller.initStage(stage);
            controller.open(bookDirectory);

            stage.show();

            openedStages.add(stage);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public static void closeAll() {
        openedStages.forEach(Stage::close);
    }
    
    private BookWindow() {}
}
