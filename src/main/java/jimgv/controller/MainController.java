package jimgv.controller;

import gl.javafx.FxmlPath;
import gl.javafx.InitializeStage;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jimgv.Config;
import jimgv.controller.book.BookWindow;
import jimgv.controller.single.SingleImageController;
import jimgv.controller.single.SingleImageWindow;
import jimgv.controller.single.mouse.MouseGestureSingleImageController;
import jimgv.controller.single.touch.TouchPanelSingleImageController;

import java.io.File;

@FxmlPath("/fxml/main.fxml")
public class MainController implements InitializeStage {
    private Stage stage;
    
    @FXML
    public void openForMouse() {
        openImage(new MouseGestureSingleImageController());
    }

    @FXML
    public void openForTouchPanel() {
        openImage(new TouchPanelSingleImageController());
    }
    
    private void openImage(SingleImageController controller) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("画像", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        Config.getInstance().getLastOpenedDirectory().ifPresent(lastOpenedDirectory -> {
            chooser.setInitialDirectory(lastOpenedDirectory.toFile());
        });

        File file = chooser.showOpenDialog(this.stage);
        if (file != null) {
            Config.getInstance().setLastOpenedDirectory(file.getParentFile().toPath());
            SingleImageWindow.open(file.toPath(), controller);
        }
    }
    
    @FXML
    public void openBook() {
        DirectoryChooser chooser = new DirectoryChooser();
        Config.getInstance().getLastOpenedBookDirectory().ifPresent(lastOpenedBookDirectory -> {
            chooser.setInitialDirectory(lastOpenedBookDirectory.toFile());
        });

        File directory = chooser.showDialog(this.stage);
        if (directory != null) {
            Config.getInstance().setLastOpenedBookDirectory(directory.toPath());
            BookWindow.open(directory.toPath());
        }
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(e -> {
            SingleImageWindow.closeAll();
            BookWindow.closeAll();
        });
    }
}
