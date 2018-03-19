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
import javafx.scene.input.TouchEvent;
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
    private TouchGesture touchGesture = new TouchGesture();
    private OpenedDirectory openedDirectory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        singleImage = new SingleImage(imageView);
        
        touchGesture.setDoubleTapListener((x, y) -> {
            singleImage.resetZoom();
        });

        touchGesture.setTouchPressedListener((x, y) -> {
            singleImage.startTranslate();
        });
        
        touchGesture.setTouchMoveListener((dx, dy) -> {
            singleImage.stopTranslate(dx, dy);

            if (!singleImage.isZoomed()) {
                double rate = Math.abs(dx) / stage.getWidth();

                double Ys = 0.5;
                double Ye = 0.2;
                double Xs = 0.15;
                double Xe = 0.4;
                double DX = Xe-Xs;
                double DY = Ys-Ye;
                if (rate < Xs) {
                    imageView.setOpacity(1.0);
                } else if (Xs <= rate && rate < Xe) {
                    imageView.setOpacity(DY*(Xe-rate)/DX + Ye);
                } else {
                    imageView.setOpacity(Ye);
                }
            }
        });

        touchGesture.setTouchReleasedListener((dx, dy) -> {
            if (!singleImage.isZoomed()) {
                singleImage.resetZoom();
                double rate = Math.abs(dx) / stage.getWidth();

                double E = 0.15;

                if (E < rate) {
                    if (dx < 0) {
                        Path previous = openedDirectory.previous();
                        singleImage.load(previous);
                    } else {
                        Path next = openedDirectory.next();
                        singleImage.load(next);
                    }
                }
            }
            
            imageView.setOpacity(1.0);
        });
        
        touchGesture.setZoomListener(rate -> {
            singleImage.startZoom(rate);
        });
        
        touchGesture.setZoomFinishedListener(() -> {
            singleImage.stopZoom();
        });
    }

    private Stage stage;

    private void setImage(Stage stage, Path imagePath) {
        openedDirectory = new OpenedDirectory(imagePath);

        this.stage = stage;
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
    public void onTouchPressed(TouchEvent e) {
        contextMenu.hide();
        touchGesture.onTouchPressed(e);
    }

    @FXML
    public void onTouchReleased(TouchEvent e) {
        touchGesture.onTouchReleased(e);
    }
    
    @FXML
    public void onTouchMoved(TouchEvent e) {
        touchGesture.onTouchMoved(e);
    }

    @FXML
    public void onMouseClicked(MouseEvent e) {
        touchGesture.onMouseClicked(e);
    }
    
    @FXML
    public void onZoom(ZoomEvent e) {
        touchGesture.onZoom(e);
    }
    
    @FXML
    public void onZoomFinished() {
        touchGesture.onZoomFinished();
    }
}
