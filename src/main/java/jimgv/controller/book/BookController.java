package jimgv.controller.book;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jimgv.model.book.Book;

import java.nio.file.Path;

public class BookController {
    public void initStage(Stage stage) {
        leftPane.prefWidthProperty().bind(stage.widthProperty().multiply(0.49));
        leftPane.prefHeightProperty().bind(stage.heightProperty());
        leftImageView.fitWidthProperty().bind(leftPane.prefWidthProperty());
        leftImageView.fitHeightProperty().bind(leftPane.prefHeightProperty());

        rightPane.prefWidthProperty().bind(stage.widthProperty().multiply(0.49));
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
    
    private Book book;
    
    public void open(Path bookDirectory) {
        book = new Book(bookDirectory);
        loadImages();
    }
    
    private void loadImages() {
        rightImageView.setImage(null);
        leftImageView.setImage(null);
        
        book.getRightImage().ifPresent(rightImage -> {
            loadImage(rightImageView, rightImage);
        });
        
        book.getLeftImage().ifPresent(leftImage -> {
            loadImage(leftImageView, leftImage);
        });
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
            book.previousPage();
        } else {
            book.nextPage();
        }
        loadImages();
    }
    
    private ContextMenu initContextMenu(Stage stage) {
        ContextMenu contextMenu = new ContextMenu();
        
        addMenuItem(contextMenu, "全画面", e -> stage.setFullScreen(!stage.isFullScreen()));
        contextMenu.getItems().add(new SeparatorMenuItem());

        CheckMenuItem startOnLeftMenuItem = new CheckMenuItem("左ページ始まり");
        startOnLeftMenuItem.selectedProperty().addListener((a, b, startOnLeft) -> {
            book.setStartOnLeft(startOnLeft);
            loadImages();
        });
        contextMenu.getItems().add(startOnLeftMenuItem);
        
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
