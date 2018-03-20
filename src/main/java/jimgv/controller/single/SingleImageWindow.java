package jimgv.controller.single;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class SingleImageWindow {
    private static Set<Stage> openedStages = new HashSet<>();
    
    public static void open(Path image, SingleImageController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(SingleImageWindow.class.getResource("/fxml/single-image.fxml"));
            loader.setController(controller);
            
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle(image.getFileName().toString());
            stage.setFullScreenExitHint("");

            controller.init(stage, image);

            stage.show();
            
            openedStages.add(stage);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public static void closeAll() {
        openedStages.forEach(Stage::close);
    }
    
    private SingleImageWindow() {}
}
