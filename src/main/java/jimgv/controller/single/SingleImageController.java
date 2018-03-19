package jimgv.controller.single;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class SingleImageController implements Initializable {
    private static final Set<Stage> openedStages = new HashSet<>();

    public static void open(Path image) {
        try {
            FXMLLoader loader = new FXMLLoader(SingleImageController.class.getResource("/fxml/single-image.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle(image.getFileName().toString());
            stage.setFullScreenExitHint("");

            SingleImageController controller = loader.getController();
            controller.setImage(stage, image);

            stage.show();
            
            openedStages.add(stage);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public static void closeAll() {
        openedStages.forEach(Stage::close);
    }
    
    @FXML
    private ImageView imageView;
    private ContextMenu contextMenu = new ContextMenu();
    private MouseGesture mouseGesture = new MouseGesture();
    private ImageViewModel imageViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageViewModel = new MouseControlImageViewModel(imageView);
        
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
    }

    private void setImage(Stage stage, Path imagePath) {
        imageViewModel.open(imagePath);
        
        stage.setWidth(1000.0);
        stage.setHeight(600.0);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());
        
        createContextMenu(stage);
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
    
    @FXML
    public void openContextMenu(ContextMenuEvent e) {
        if (!imageViewModel.isZooming()) {
            contextMenu.hide();
            contextMenu.show(imageView, e.getScreenX(), e.getScreenY());
        }
    }
    
    @FXML
    public void onMousePressed(MouseEvent e) {
        imageViewModel.finishZoom();
        contextMenu.hide();

        mouseGesture.onMousePressed(e);
    }
    
    @FXML
    public void onMouseReleased() {
        mouseGesture.onMouseReleased();
    }
    
    @FXML
    public void onMouseDragged(MouseEvent e) {
        mouseGesture.onMouseDragged(e);
    }
    
    @FXML
    public void onScroll(ScrollEvent e) {
        mouseGesture.onScroll(e);
    }
}
