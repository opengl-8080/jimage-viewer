package jimgv.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SingleImageController {
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
    
    private static final List<String> IMAGE_EXTENSTIONS = Arrays.asList(".png", ".jpg", ".jpeg", ".gif");
    @FXML
    private ImageView imageView;
    private ContextMenu contextMenu = new ContextMenu();
    private List<Path> imagePathList;
    private int currentSelectedIndex;

    private void setImage(Stage stage, Path imagePath) {
        Path directory = imagePath.getParent();
        try {
            imagePathList = Files.list(directory)
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        String fileName = path.getFileName().toString().toLowerCase();
                        int lastIndex = fileName.lastIndexOf(".");
                        if (lastIndex == -1) {
                            return false;
                        }
                        String extension = fileName.substring(lastIndex);
                        return IMAGE_EXTENSTIONS.contains(extension);
                    })
                    .sorted()
                    .collect(Collectors.toList());
            currentSelectedIndex = imagePathList.indexOf(imagePath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        loadImage(imagePath);
        
        stage.setWidth(1000.0);
        stage.setHeight(600.0);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());
        
        createContextMenu(stage);
    }
    
    private void loadImage(Path imagePath) {
        Image image = new Image(imagePath.toUri().toString());
        imageView.setImage(image);
        resetZoom();
    }
    
    private void resetZoom() {
        imageView.setScaleX(1.0);
        imageView.setScaleY(1.0);
        imageView.setTranslateX(0.0);
        imageView.setTranslateY(0.0);
    }
    
    private void createContextMenu(Stage stage) {
        addMenuItem("全画面", e -> stage.setFullScreen(!stage.isFullScreen()));
        addMenuItem("拡大リセット", e -> resetZoom());
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
        if (!zooming) {
            contextMenu.hide();
            contextMenu.show(imageView, e.getScreenX(), e.getScreenY());
        }
    }
    
    private boolean leftClicked;
    private boolean rightClicked;
    private boolean zooming;
    private double fromX;
    private double fromY;
    private double fromTranslateX;
    private double fromTranslateY;
    
    @FXML
    public void onMousePressed(MouseEvent e) {
        zooming = false;
        contextMenu.hide();
        leftClicked = e.isPrimaryButtonDown();
        if (leftClicked) {
            fromX = e.getScreenX();
            fromY = e.getScreenY();
            fromTranslateX = imageView.getTranslateX();
            fromTranslateY = imageView.getTranslateY();
        }
        rightClicked = e.isSecondaryButtonDown();
    }
    
    @FXML
    public void onMouseReleased() {
        leftClicked = false;
        rightClicked = false;
    }
    
    @FXML
    public void onMouseDragged(MouseEvent e) {
        if (leftClicked) {
            double dx = e.getScreenX() - fromX;
            double dy = e.getScreenY() - fromY;
            imageView.setTranslateX(fromTranslateX + dx);
            imageView.setTranslateY(fromTranslateY + dy);
        }
    }
    
    @FXML
    public void onScroll(ScrollEvent e) {
        if (rightClicked) {
            zooming = true;
            double delta = (e.getDeltaY() / 400.0);
            double scale = imageView.getScaleX() + delta;
            
            if (0.5 <= scale && scale <= 100.0) {
                imageView.setScaleX(scale);
                imageView.setScaleY(scale);
            }
        }
        
        if (!leftClicked && !rightClicked) {
            if (e.getDeltaY() < 0) {
                currentSelectedIndex--;
                if (currentSelectedIndex < 0) {
                    currentSelectedIndex = imagePathList.size() - 1;
                }
            } else {
                currentSelectedIndex++;
                if (imagePathList.size() <= currentSelectedIndex) {
                    currentSelectedIndex = 0;
                }
            }

            Path imagePath = imagePathList.get(currentSelectedIndex);
            loadImage(imagePath);
        }
    }
}
