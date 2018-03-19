package jimgv.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jimgv.Config;
import jimgv.controller.single.SingleImageController;
import jimgv.controller.single.TouchPanelSingleImageController;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private Stage stage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    @FXML
    public void openImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("画像", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        Config.getInstance().getLastOpenedDirectory().ifPresent(lastOpenedDirectory -> {
            chooser.setInitialDirectory(lastOpenedDirectory.toFile());
        });
        
        File file = chooser.showOpenDialog(this.stage);
        if (file != null) {
            Config.getInstance().setLastOpenedDirectory(file.getParentFile().toPath());
            TouchPanelSingleImageController.open(file.toPath());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(e -> TouchPanelSingleImageController.closeAll());
    }
}
