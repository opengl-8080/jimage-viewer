package jimgv.controller.book;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jimgv.model.ImageFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class BookController {
    public void initStage(Stage stage) {
        leftPane.prefWidthProperty().bind(stage.widthProperty().divide(2));
        leftPane.prefHeightProperty().bind(stage.heightProperty());
        leftImageView.fitWidthProperty().bind(leftPane.prefWidthProperty());
        leftImageView.fitHeightProperty().bind(leftPane.prefHeightProperty());

        rightPane.prefWidthProperty().bind(stage.widthProperty().divide(2));
        rightPane.prefHeightProperty().bind(stage.heightProperty());
        rightImageView.fitWidthProperty().bind(rightPane.prefWidthProperty());
        rightImageView.fitHeightProperty().bind(rightPane.prefHeightProperty());
        
        contextMenu = initContextMenu(stage);
        root.setOnContextMenuRequested(e -> {
            contextMenu.show(root, e.getScreenX(), e.getScreenY());
        });

        root.setOnMouseReleased(e -> {
            contextMenu.hide();
        });
    }
    
    private ContextMenu contextMenu;
    
    @FXML
    private Node root;
    @FXML
    private Pane leftPane;
    @FXML
    private Pane rightPane;
    @FXML
    private ImageView leftImageView;
    @FXML
    private ImageView rightImageView;
    
    private List<Path> imageList;
    private int rightIndex;
    
    public void open(Path bookDirectory) {
        try {
            imageList = Files.list(bookDirectory)
                            .filter(ImageFile::isImageFile)
                            .sorted()
                            .collect(Collectors.toList());
            
            if (imageList.isEmpty()) {
                return;
            }
            
            loadImages();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    private void loadImages() {
        rightImageView.setImage(null);
        leftImageView.setImage(null);
        
        loadImage(rightImageView, imageList.get(rightIndex));
        
        int leftIndex = rightIndex + 1;
        if (leftIndex < imageList.size()) {
            loadImage(leftImageView, imageList.get(leftIndex));
        }
    }
    
    private void loadImage(ImageView imageView, Path imagePath) {
        Image image = new Image(imagePath.toUri().toString());
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.autosize();
    }
    
    @FXML
    public void onScroll(ScrollEvent e) {
        double delta = e.getDeltaY();
        if (delta < 0) {
            previousPage();
        } else {
            nextPage();
        }
    }
    
    private void previousPage() {
        rightIndex -= 2;
        if (rightIndex < 0) {
            rightIndex = 0;
        }
        
        loadImages();
    }
    
    private void nextPage() {
        rightIndex += 2;
        if (imageList.size() <= rightIndex) {
            rightIndex = imageList.size() - 1;
        }
        
        loadImages();
    }
    
    private ContextMenu initContextMenu(Stage stage) {
        ContextMenu contextMenu = new ContextMenu();
        
        addMenuItem(contextMenu, "全画面", e -> stage.setFullScreen(!stage.isFullScreen()));
        contextMenu.getItems().add(new SeparatorMenuItem());
        addMenuItem(contextMenu, "閉じる", e -> stage.close());
        
        return contextMenu;
    }

    private void addMenuItem(ContextMenu contextMenu, String text, EventHandler<ActionEvent> handler) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(handler);
        contextMenu.getItems().add(item);
    }
}
