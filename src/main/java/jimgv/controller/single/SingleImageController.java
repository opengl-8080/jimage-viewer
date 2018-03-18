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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
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
    private SingleImage singleImage;
    private ContextMenu contextMenu = new ContextMenu();
    private MouseGesture mouseGesture = new MouseGesture();
    private OpenedDirectory openedDirectory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        singleImage = new SingleImage(imageView);
        
        mouseGesture.setLeftDragListener(singleImage::stopTranslate);
        
        mouseGesture.setRightScrollListener(singleImage::startZoom);
        
        mouseGesture.setScrollListener(delta -> {
            Path imagePath = delta < 0 ? openedDirectory.previous() : openedDirectory.next();
            singleImage.load(imagePath);
        });
    }

    private void setImage(Stage stage, Path imagePath) {
        openedDirectory = new OpenedDirectory(imagePath);

        singleImage.load(imagePath);
        
        stage.setWidth(1000.0);
        stage.setHeight(600.0);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());
        
        createContextMenu(stage);
    }
    
    private void createContextMenu(Stage stage) {
        addMenuItem("全画面", e -> stage.setFullScreen(!stage.isFullScreen()));
        addMenuItem("拡大リセット", e -> singleImage.resetZoom());
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
        if (!singleImage.isZooming()) {
            contextMenu.hide();
            contextMenu.show(imageView, e.getScreenX(), e.getScreenY());
        }
    }
    
    @FXML
    public void onMousePressed(MouseEvent e) {
        singleImage.stopZoom();
        contextMenu.hide();

        mouseGesture.onMousePressed(e);
        
        if (e.isPrimaryButtonDown()) {
            singleImage.startTranslate();
        }
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
    
    @FXML
    public void onZoom(ZoomEvent e) {
        singleImage.startZoom(e.getZoomFactor());
    }
    
    @FXML
    public void onZoomFinished() {
        singleImage.stopZoom();
    }
}
