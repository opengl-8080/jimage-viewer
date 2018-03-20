package jimgv.controller.single;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.nio.file.Path;

public abstract class SingleImageControllerBase implements SingleImageController {
    @FXML
    protected Node root;
    @FXML
    private ImageView imageView;

    protected ContextMenu contextMenu = new ContextMenu();
    protected ImageViewModel imageViewModel;

    protected abstract ImageViewModel createImageViewModel(Path imagePath);
    
    @Override
    public void init(Stage stage, Path imagePath) {
        createContextMenu(stage);
        
        stage.setWidth(1000.0);
        stage.setHeight(600.0);

        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());
        
        imageViewModel = createImageViewModel(imagePath);
        imageViewModel.open(imagePath);
        
        root.setOnContextMenuRequested(e -> {
            if (!imageViewModel.isZooming()) {
                contextMenu.hide();
                contextMenu.show(imageView, e.getScreenX(), e.getScreenY());
            }
        });
    }

    private void createContextMenu(Stage stage) {
        addMenuItem("全画面", e -> stage.setFullScreen(!stage.isFullScreen()));
        addMenuItem("拡大リセット", e -> imageViewModel.reset());
        contextMenu.getItems().add(new SeparatorMenuItem());
        addMenuItem("閉じる", e -> stage.close());
    }

    private void addMenuItem(String text, EventHandler<ActionEvent> handler) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(handler);
        contextMenu.getItems().add(item);
    }
}