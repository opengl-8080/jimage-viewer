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

public class MouseGestureSingleImageController implements SingleImageController {
    @FXML
    private Node root;
    @FXML
    private ImageView imageView;
    
    private ContextMenu contextMenu = new ContextMenu();
    private MouseGesture mouseGesture = new MouseGesture();
    private ImageViewModel imageViewModel;
    
    @Override
    public void init(Stage stage, Path imagePath) {
        stage.setWidth(1000.0);
        stage.setHeight(600.0);
        
        imageViewModel = new MouseControlImageViewModel(imageView);
        imageViewModel.open(imagePath);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());

        mouseGesture.setLeftDragListener((dx, dy) -> {
            imageViewModel.translate(dx, dy);
        });

        mouseGesture.setRightScrollListener(deltaY -> {
            if (deltaY < 0) {
                imageViewModel.zoomDown();
            } else {
                imageViewModel.zoomUp();
            }
        });

        mouseGesture.setScrollListener(delta -> {
            if (delta < 0) {
                imageViewModel.loadPreviousImage();
            } else {
                imageViewModel.loadNextImage();
            }
        });

        createContextMenu(stage);
        
        root.setOnContextMenuRequested(e -> {
            if (!imageViewModel.isZooming()) {
                contextMenu.hide();
                contextMenu.show(imageView, e.getScreenX(), e.getScreenY());
            }
        });
        
        root.setOnMousePressed(e -> {
            imageViewModel.finishZoom();
            contextMenu.hide();

            mouseGesture.onMousePressed(e);
        });
        
        root.setOnMouseReleased(e -> {
            mouseGesture.onMouseReleased();
        });

        root.setOnMouseDragged(e -> {
            mouseGesture.onMouseDragged(e);
        });

        root.setOnScroll(e -> {
            mouseGesture.onScroll(e);
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
