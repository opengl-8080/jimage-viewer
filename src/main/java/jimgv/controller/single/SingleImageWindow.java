package jimgv.controller.single;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class SingleImageWindow {
    
    public static Stage open(Path image, SingleImageController controller) {
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
            
            return stage;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    private SingleImageWindow() {}
}
